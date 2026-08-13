package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.exception.CorrelationEngineException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.UserRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.CorrelationFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.LoginFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.SlaFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;


final class ManagerDashboardControllerCli {

    private static final Logger log = LoggerFactory.getLogger(ManagerDashboardControllerCli.class);

    void run(Scanner sc) {
        boolean inDashboard = true;
        while (inDashboard) {
            System.out.println("\n=== Dashboard Manager - " + UserSession.getInstanceSingleton().getName() + " ===");
            System.out.println("1) Visualizza tutti i ticket");
            System.out.println("2) Ticket in scadenza SLA");
            System.out.println("3) Ticket correlati (per ID)");
            System.out.println("4) Assegna tecnico");
            System.out.println("5) Cambia priorita");
            System.out.println("6) Logout");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> listAllTickets();
                case "2" -> listExpiringSla();
                case "3" -> findCorrelated(sc);
                case "4" -> assignTechnician(sc);
                case "5" -> changePriority(sc);
                case "6" -> inDashboard = false;
                default -> System.out.println("Scelta non valida.");
            }
        }
        LoginFacade.getInstanceSingleton().logout();
        System.out.println("Logout effettuato.");
    }

    private void listAllTickets() {
        try {
            CliFormatter.printTicketTable(ViewTicketsFacade.getInstanceSingleton().getAllTickets());
        } catch (DAOException e) {
            log.error("Errore caricamento tutti i ticket", e);
            System.out.println("Impossibile caricare i ticket.");
        }
    }

    private void listExpiringSla() {
        try {
            CliFormatter.printTicketTable(SlaFacade.getInstanceSingleton().getTicketsWithSlaExpiringSoon());
        } catch (DAOException e) {
            log.error("Errore controllo SLA", e);
            System.out.println("Errore interno del sistema.");
        }
    }

    private void findCorrelated(Scanner sc) {
        int id = CliFormatter.readInt("ID ticket: ", sc);
        if (id <= 0) {
            System.out.println("ID non valido.");
            return;
        }
        try {
            List<TicketRecord> correlated = CorrelationFacade.getInstanceSingleton().findCorrelations(id);
            CliFormatter.printTicketTable(correlated);
        } catch (TicketNotFoundException | CorrelationEngineException e) {
            System.out.println("Errore: " + e.getMessage());
        } catch (DAOException e) {
            log.error("Errore ricerca ticket correlati", e);
            System.out.println("Errore interno del sistema.");
        }
    }

    private void assignTechnician(Scanner sc) {
        int id = CliFormatter.readInt("ID ticket: ", sc);
        if (id <= 0) {
            System.out.println("ID non valido.");
            return;
        }
        List<UserRecord> technicians;
        try {
            technicians = ViewTicketsFacade.getInstanceSingleton().getAvailableTechnicians();
        } catch (DAOException e) {
            log.error("Errore caricamento tecnici", e);
            System.out.println("Impossibile caricare i tecnici disponibili.");
            return;
        }
        if (technicians.isEmpty()) {
            System.out.println("Nessun tecnico disponibile.");
            return;
        }
        System.out.println("Tecnici disponibili:");
        for (int i = 0; i < technicians.size(); i++) {
            UserRecord u = technicians.get(i);
            System.out.printf("  %d) %s %s (%s)%n", i + 1, u.name(), u.surname(), u.specialization());
        }
        int idx = CliFormatter.readInt("> ", sc) - 1;
        if (idx < 0 || idx >= technicians.size()) {
            System.out.println("Scelta non valida.");
            return;
        }
        try {
            ViewTicketsFacade.getInstanceSingleton().assignTechnician(id, technicians.get(idx).email());
            System.out.println("Ticket assegnato con successo.");
        } catch (TicketNotFoundException e) {
            System.out.println("Ticket non trovato.");
        } catch (InvalidTransitionException e) {
            System.out.println("Transizione non valida per questo ticket.");
        } catch (DAOException e) {
            log.error("Errore assegnazione ticket {}", id, e);
            System.out.println("Errore interno del sistema.");
        }
    }

    private void changePriority(Scanner sc) {
        int id = CliFormatter.readInt("ID ticket: ", sc);
        if (id <= 0) {
            System.out.println("ID non valido.");
            return;
        }
        Priority priority = choosePriority(sc);
        try {
            ViewTicketsFacade.getInstanceSingleton().changePriority(id, priority.name());
            System.out.println("Priorita' aggiornata.");
        } catch (DAOException | TicketNotFoundException e) {
            log.error("Errore aggiornamento priorita' ticket {}", id, e);
            System.out.println("Errore interno del sistema.");
        }
    }

    private Priority choosePriority(Scanner sc) {
        while (true) {
            System.out.println("Nuova priorita':");
            Priority[] values = Priority.values();
            for (int i = 0; i < values.length; i++) {
                System.out.printf("  %d) %s%n", i + 1, values[i]);
            }
            int idx = CliFormatter.readInt("> ", sc) - 1;
            if (idx >= 0 && idx < values.length) {
                return values[idx];
            }
            System.out.println("Scelta non valida.");
        }
    }
}
