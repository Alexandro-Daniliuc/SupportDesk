package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.bean.NotificationBean;
import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.UserRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.EventType;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.Subject;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SubmitTicketController extends Subject {

    private static final Logger log             = LoggerFactory.getLogger(SubmitTicketController.class);
    private static final long   SLA_WARNING_HOURS = 2;
    private static final ScheduledExecutorService SLA_SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    private final LoginController loginController = new LoginController();

    private NotificationBean buildNotification(EventType eventType) {
        String message = switch (eventType) {
            case TICKET_OPEN          -> "Nuovo ticket aperto - in attesa di assegnazione.";
            case TICKET_IN_CARICO     -> "Il ticket e stato preso in carico da un tecnico.";
            case TICKET_CAMBIO_STATO  -> "Cambio stato ticket rilevato.";
            case TICKET_RISOLTO       -> "Ticket risolto - notifica inviata all'utente richiedente.";
            case SLA_IN_SCADENZA      -> "SLA in scadenza rilevato.";
            case SLA_VIOLATO          -> "SLA VIOLATO rilevato.";
            case ASSEGNAZIONE_MANUALE -> "Ticket richiede assegnazione manuale.";
            default                   -> eventType.name();
        };
        return new NotificationBean(eventType, message);
    }

    public TicketRecord openTicket(TicketBean bean, String authorEmail)
            throws ValidationException, DAOException {
        if (!loginController.isUserLogged()) {
            throw new ValidationException("Utente non autenticato");
        }
        String cat = bean.getCategory() != null ? bean.getCategory().name() : null;
        String pri = bean.getPriority() != null ? bean.getPriority().name() : null;
        if (!validateInput(bean.getTitle(), bean.getDescription(), cat, pri)) {
            if (bean.getTitle() == null || bean.getTitle().isBlank())
                throw new ValidationException("title", "Il titolo è obbligatorio");
            if (bean.getDescription() == null || bean.getDescription().isBlank())
                throw new ValidationException("description", "La descrizione è obbligatoria");
            if (bean.getCategory() == null)
                throw new ValidationException("category", "La categoria è obbligatoria");
            throw new ValidationException("priority", "La priorità è obbligatoria");
        }
        User author = PersistenceLayerFactory.getInstance().findUserByEmail(authorEmail);
        if (author == null) {
            throw new DAOException("Utente non trovato: " + authorEmail);
        }
        Ticket ticket = createTicket(author, bean.getTitle(), bean.getDescription(),
                bean.getCategory(), bean.getPriority());
        notifyObservers(EventType.TICKET_OPEN, buildNotification(EventType.TICKET_OPEN));
        launchBackgroundTasks(ticket);
        log.info("Ticket {} aperto da {}", ticket.getId(), authorEmail);
        return toRecord(ticket);
    }

    public TicketRecord getTicket(int id) throws DAOException, TicketNotFoundException {
        return toRecord(PersistenceLayerFactory.getInstance().getTicketById(id));
    }

    public List<TicketRecord> getAllTickets() throws DAOException {
        return PersistenceLayerFactory.getInstance().findAllTickets()
                .stream().map(SubmitTicketController::toRecord).toList();
    }

    public List<TicketRecord> getTicketsByUser(String email) throws DAOException {
        return PersistenceLayerFactory.getInstance().getTicketsByUser(email)
                .stream().map(SubmitTicketController::toRecord).toList();
    }

    public void changeStatus(int ticketId, TicketStatus newStatus)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        if (newStatus == TicketStatus.REOPENED) {
            if (!UserSession.getInstanceSingleton().isLoggedIn()
                    || UserSession.getInstanceSingleton().getRole() != Role.USER) {
                throw new InvalidTransitionException("Solo l'utente puo riaprire un ticket");
            }
        }
        Ticket ticket = PersistenceLayerFactory.getInstance().getTicketById(ticketId);
        ticket.changeStatus(newStatus);
        PersistenceLayerFactory.getInstance().updateTicket(ticket);
        notifyObservers(EventType.TICKET_CAMBIO_STATO, buildNotification(EventType.TICKET_CAMBIO_STATO));
        if (newStatus == TicketStatus.IN_PROGRESS) {
            notifyObservers(EventType.TICKET_IN_CARICO, buildNotification(EventType.TICKET_IN_CARICO));
        } else if (newStatus == TicketStatus.RESOLVED) {
            notifyObservers(EventType.TICKET_RISOLTO, buildNotification(EventType.TICKET_RISOLTO));
        }
        log.info("Ticket {} passato a stato {}", ticketId, newStatus);
    }

    public void schedulaSlaTimer(Ticket ticket) {
        LocalDateTime now = LocalDateTime.now();
        long msToExpiry = Duration.between(now, ticket.getScadenzaSla()).toMillis();
        long msToWarning = Duration.between(now, ticket.getScadenzaSla().minusHours(SLA_WARNING_HOURS)).toMillis();

        if (msToExpiry <= 0) {
            notifyObservers(EventType.SLA_VIOLATO, buildNotification(EventType.SLA_VIOLATO));
            return;
        }

        if (msToWarning > 0) {
            SLA_SCHEDULER.schedule(
                () -> notifyObservers(EventType.SLA_IN_SCADENZA, buildNotification(EventType.SLA_IN_SCADENZA)),
                msToWarning, TimeUnit.MILLISECONDS);
        } else {
            notifyObservers(EventType.SLA_IN_SCADENZA, buildNotification(EventType.SLA_IN_SCADENZA));
        }

        SLA_SCHEDULER.schedule(() -> {
            try {
                Ticket current = PersistenceLayerFactory.getInstance().getTicketById(ticket.getId());
                if (!isTerminated(current)) {
                    notifyObservers(EventType.SLA_VIOLATO, buildNotification(EventType.SLA_VIOLATO));
                }
            } catch (Exception e) {
                log.warn("Controllo SLA fallito per ticket {}", ticket.getId());
            }
        }, msToExpiry, TimeUnit.MILLISECONDS);
    }

    private boolean isTerminated(Ticket t) {
        return t.getStatus() == TicketStatus.RESOLVED || t.getStatus() == TicketStatus.CLOSED;
    }

    private void launchBackgroundTasks(Ticket ticket) {
        int id = ticket.getId();
        schedulaSlaTimer(ticket);

        Thread correlation = new Thread(() -> {
            try {
                new CorrelationController().findCorrelations(ticket);
            } catch (Exception e) {
                log.info("Correlazione non disponibile per ticket {}", id);
            }
        });
        correlation.start();
    }

    public List<UserRecord> getAvailableTechnicians() throws DAOException {
        return PersistenceLayerFactory.getInstance()
                .findUsersByRole(Role.TECHNICIAN)
                .stream()
                .map(u -> new UserRecord(u.getId(), u.getName(), u.getSurname(),
                        u.getEmail(), u.getRole(), u.getSpecialization()))
                .toList();
    }

    public boolean validateInput(String title, String description, String category, String priority) {
        return title != null && !title.isBlank()
            && description != null && !description.isBlank()
            && category != null && !category.isBlank()
            && priority != null && !priority.isBlank();
    }

    public Ticket createTicket(User user, String title, String description, Category category, Priority priority)
            throws DAOException {
        List<Ticket> all = PersistenceLayerFactory.getInstance().findAllTickets();
        int nextId = all.stream().mapToInt(Ticket::getId).max().orElse(0) + 1;
        Ticket ticket = new Ticket.Builder(nextId, title, description, category, priority)
                .authorEmail(user.getEmail())
                .build();
        PersistenceLayerFactory.getInstance().saveTicket(ticket);
        log.info("Ticket {} creato da {}", ticket.getId(), user.getEmail());
        return ticket;
    }

    public void changePriority(int ticketId, Priority newPriority)
            throws DAOException, TicketNotFoundException {
        Ticket t = PersistenceLayerFactory.getInstance().getTicketById(ticketId);
        Ticket updated = new Ticket.Builder(t.getId(), t.getTitle(), t.getDescription(), t.getCategory(), newPriority)
                .authorEmail(t.getAuthorEmail())
                .dataApertura(t.getDataApertura())
                .status(t.getStatus())
                .build();
        updated.setAssignedTechnician(t.getAssignedTechnician());
        PersistenceLayerFactory.getInstance().updateTicket(updated);
        log.info("Priorita ticket {} aggiornata a {}", ticketId, newPriority);
    }

    public TicketRecord assignTechnician(int ticketId, String techEmail)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        Ticket ticket = PersistenceLayerFactory.getInstance().getTicketById(ticketId);
        User tech = PersistenceLayerFactory.getInstance().findUserByEmail(techEmail);
        ticket.setAssignedTechnician(tech);
        if (ticket.getStatus() == TicketStatus.OPEN || ticket.getStatus() == TicketStatus.REOPENED) {
            ticket.changeStatus(TicketStatus.ASSIGNED);
        }
        PersistenceLayerFactory.getInstance().updateTicket(ticket);
        notifyObservers(EventType.ASSEGNAZIONE_MANUALE, buildNotification(EventType.ASSEGNAZIONE_MANUALE));
        log.info("Ticket {} assegnato a {}", ticketId, techEmail);
        return toRecord(ticket);
    }

    public void rescheduleAllSlaTimers() throws DAOException {
        PersistenceLayerFactory.getInstance().findAllTickets().stream()
                .filter(t -> t.getStatus() != TicketStatus.RESOLVED && t.getStatus() != TicketStatus.CLOSED)
                .forEach(this::schedulaSlaTimer);
    }

    public static TicketRecord toRecord(Ticket t) {
        String techName = t.getAssignedTechnician() != null
                ? t.getAssignedTechnician().getName() + " " + t.getAssignedTechnician().getSurname()
                : null;
        return new TicketRecord(t.getId(), t.getTitle(), t.getDescription(),
                t.getCategory(), t.getPriority(), t.getStatus(),
                t.getDataApertura(), t.getScadenzaSla(), techName);
    }
}
