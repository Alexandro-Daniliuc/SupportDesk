package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayer;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.UserRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.EventType;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TicketObserver;
import it.uniroma2.dicii.ispw.supportdesk.utility.observer.TicketSubject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TicketController implements TicketSubject {

    private static final Logger log             = LoggerFactory.getLogger(TicketController.class);
    private static final long   SLA_WARNING_HOURS = 2;
    private static final ScheduledExecutorService SLA_SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    private final List<TicketObserver> observers = new CopyOnWriteArrayList<>();

    @Override
    public void attach(TicketObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(TicketObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(EventType eventType) {
        for (TicketObserver o : observers) {
            o.update(eventType);
        }
    }

    public TicketRecord openTicket(TicketBean bean, String authorEmail)
            throws ValidationException, DAOException {
        if (!bean.isValid()) {
            throw new ValidationException("Dati ticket non validi");
        }
        List<Ticket> all = PersistenceLayer.getInstanceSingleton().findAllTickets();
        int nextId = all.stream().mapToInt(Ticket::getId).max().orElse(0) + 1;
        Ticket ticket = new Ticket.Builder(nextId, bean.getTitle(), bean.getDescription(),
                bean.getCategory(), bean.getPriority())
                .authorEmail(authorEmail)
                .build();
        notifyObservers(EventType.TICKET_OPEN);
        PersistenceLayer.getInstanceSingleton().saveTicket(ticket);
        launchBackgroundTasks(ticket);
        log.info("Ticket {} aperto da {}", ticket.getId(), authorEmail);
        return toRecord(ticket);
    }

    public TicketRecord getTicket(int id) throws DAOException, TicketNotFoundException {
        return toRecord(PersistenceLayer.getInstanceSingleton().getTicketById(id));
    }

    public List<TicketRecord> getAllTickets() throws DAOException {
        return PersistenceLayer.getInstanceSingleton().findAllTickets()
                .stream().map(TicketController::toRecord).toList();
    }

    public List<TicketRecord> getTicketsByUser(String email) throws DAOException {
        return PersistenceLayer.getInstanceSingleton().getTicketsByUser(email)
                .stream().map(TicketController::toRecord).toList();
    }

    public TicketRecord changeStatus(int id, TicketStatus newStatus)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        if (newStatus == TicketStatus.REOPENED) {
            User caller = UserSession.getInstanceSingleton().getCurrentUser();
            if (caller == null || caller.obtainRole() != Role.USER) {
                throw new InvalidTransitionException("Solo l'utente può riaprire un ticket");
            }
        }
        Ticket ticket = PersistenceLayer.getInstanceSingleton().getTicketById(id);
        ticket.cambiaStato(newStatus);
        PersistenceLayer.getInstanceSingleton().updateTicket(ticket);
        notifyObservers(EventType.TICKET_CAMBIO_STATO);
        if (newStatus == TicketStatus.RESOLVED) {
            notifyObservers(EventType.TICKET_RISOLTO);
        }
        log.info("Ticket {} passato a stato {}", id, newStatus);
        return toRecord(ticket);
    }

    public void schedulaSlaTimer(Ticket ticket) {
        LocalDateTime now = LocalDateTime.now();
        long msToExpiry = Duration.between(now, ticket.getScadenzaSla()).toMillis();
        long msToWarning = Duration.between(now, ticket.getScadenzaSla().minusHours(SLA_WARNING_HOURS)).toMillis();

        if (msToExpiry <= 0) {
            notifyObservers(EventType.SLA_VIOLATO);
            return;
        }

        if (msToWarning > 0) {
            SLA_SCHEDULER.schedule(() -> notifyObservers(EventType.SLA_IN_SCADENZA),
                    msToWarning, TimeUnit.MILLISECONDS);
        } else {
            notifyObservers(EventType.SLA_IN_SCADENZA);
        }

        SLA_SCHEDULER.schedule(() -> {
                try {
                    Ticket current = PersistenceLayer.getInstanceSingleton().getTicketById(ticket.getId());
                    if (!isTerminated(current)) {
                        notifyObservers(EventType.SLA_VIOLATO);
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
        return PersistenceLayer.getInstanceSingleton()
                .findUsersByRole(Role.TECHNICIAN)
                .stream()
                .map(u -> new UserRecord(u.obtainId(), u.obtainName(), u.obtainSurname(),
                        u.obtainEmail(), u.obtainRole(), u.obtainSpecialization()))
                .toList();
    }

    public void changePriority(int ticketId, it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority newPriority)
            throws DAOException, TicketNotFoundException {
        Ticket t = PersistenceLayer.getInstanceSingleton().getTicketById(ticketId);
        Ticket updated = new Ticket.Builder(t.getId(), t.getTitle(), t.getDescription(), t.getCategory(), newPriority)
                .authorEmail(t.getAuthorEmail())
                .dataApertura(t.getDataApertura())
                .status(t.getStatus())
                .build();
        updated.setAssignedTechnician(t.getAssignedTechnician());
        PersistenceLayer.getInstanceSingleton().updateTicket(updated);
        log.info("Priorità ticket {} aggiornata a {}", ticketId, newPriority);
    }

    public TicketRecord assignTechnician(int ticketId, String techEmail)
            throws DAOException, TicketNotFoundException, InvalidTransitionException {
        Ticket ticket = PersistenceLayer.getInstanceSingleton().getTicketById(ticketId);
        User tech = PersistenceLayer.getInstanceSingleton().findUserByEmail(techEmail);
        ticket.setAssignedTechnician(tech);
        if (ticket.getStatus() == TicketStatus.OPEN || ticket.getStatus() == TicketStatus.REOPENED) {
            ticket.cambiaStato(TicketStatus.ASSIGNED);
        }
        PersistenceLayer.getInstanceSingleton().updateTicket(ticket);
        notifyObservers(EventType.ASSEGNAZIONE_MANUALE);
        log.info("Ticket {} assegnato a {}", ticketId, techEmail);
        return toRecord(ticket);
    }

    public static TicketRecord toRecord(Ticket t) {
        String techName = t.getAssignedTechnician() != null
                ? t.getAssignedTechnician().obtainName() + " " + t.getAssignedTechnician().obtainSurname()
                : null;
        return new TicketRecord(t.getId(), t.getTitle(), t.getDescription(),
                t.getCategory(), t.getPriority(), t.getStatus(),
                t.getDataApertura(), t.getScadenzaSla(), techName);
    }
}
