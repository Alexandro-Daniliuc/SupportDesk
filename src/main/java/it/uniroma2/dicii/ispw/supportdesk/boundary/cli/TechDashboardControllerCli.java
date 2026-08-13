package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.KnowledgeBaseException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.exception.TicketNotFoundException;
import it.uniroma2.dicii.ispw.supportdesk.record.KnowledgeEntryRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.KnowledgeBaseFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.LoginFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;


@SuppressWarnings("java:S106")
final class TechDashboardControllerCli {

    private static final Logger log = LoggerFactory.getLogger(TechDashboardControllerCli.class);

    void run(Scanner sc) {
        boolean inDashboard = true;
        while (inDashboard) {
            System.out.println("\n=== Dashboard Tecnico - " + UserSession.getInstanceSingleton().getName() + " ===");
            System.out.println("1) Visualizza tutti i ticket");
            System.out.println("2) Dettaglio ticket e commenti");
            System.out.println("3) Prendi in carico");
            System.out.println("4) Segna come risolto");
            System.out.println("5) Cambia priorita");
            System.out.println("6) Cerca knowledge base");
            System.out.println("7) Aggiungi voce knowledge base");
            System.out.println("8) Logout");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> listTickets();
                case "2" -> showDetail(sc);
                case "3" -> takeCharge(sc);
                case "4" -> resolve(sc);
                case "5" -> changePriority(sc);
                case "6" -> searchKb(sc);
                case "7" -> addKbEntry(sc);
                case "8" -> inDashboard = false;
                default -> System.out.println(CliFormatter.MSG_SCELTA_NON_VALIDA);
            }
        }
        LoginFacade.getInstanceSingleton().logout();
        System.out.println("Logout effettuato.");
    }

    private void listTickets() {
        try {
            CliFormatter.printTicketTable(ViewTicketsFacade.getInstanceSingleton().getAllTickets());
        } catch (DAOException e) {
            log.error("Errore caricamento ticket tecnico", e);
            System.out.println("Impossibile caricare i ticket.");
        }
    }

    private void showDetail(Scanner sc) {
        int id = CliFormatter.readInt(CliFormatter.PROMPT_ID_TICKET, sc);
        TicketRecord t = findTicket(id);
        if (t == null) {
            return;
        }
        CliFormatter.printTicketDetail(t);
        try {
            CliFormatter.printComments(ViewTicketsFacade.getInstanceSingleton().getCommentsForTicket(id));
        } catch (DAOException e) {
            log.error("Errore caricamento commenti ticket {}", id, e);
            System.out.println("Impossibile caricare i commenti.");
        }
    }

    private void takeCharge(Scanner sc) {
        int id = CliFormatter.readInt(CliFormatter.PROMPT_ID_TICKET, sc);
        TicketRecord t = findTicket(id);
        if (t == null) {
            return;
        }
        if (t.status() == TicketStatus.IN_PROGRESS) {
            System.out.println("Ticket gia' preso in carico.");
            return;
        }
        try {
            if (t.status() == TicketStatus.OPEN || t.status() == TicketStatus.REOPENED) {
                ViewTicketsFacade.getInstanceSingleton()
                        .assignTechnician(id, UserSession.getInstanceSingleton().getEmail());
            }
            ViewTicketsFacade.getInstanceSingleton().changeStatus(id, TicketStatus.IN_PROGRESS);
            System.out.println("Ticket preso in carico.");
        } catch (DAOException e) {
            log.error("Errore presa in carico ticket {}", id, e);
            CliFormatter.printInternalError();
        } catch (SupportDeskException e) {
            CliFormatter.printError(e.getMessage());
        }
    }

    private void resolve(Scanner sc) {
        int id = CliFormatter.readInt(CliFormatter.PROMPT_ID_TICKET, sc);
        TicketRecord t = findTicket(id);
        if (t == null) {
            return;
        }
        if (t.status() == TicketStatus.RESOLVED) {
            System.out.println("Ticket gia' risolto.");
            return;
        }
        try {
            ViewTicketsFacade.getInstanceSingleton().changeStatus(id, TicketStatus.RESOLVED);
            System.out.println("Ticket segnato come risolto.");
        } catch (DAOException e) {
            log.error("Errore risoluzione ticket {}", id, e);
            CliFormatter.printInternalError();
        } catch (SupportDeskException e) {
            CliFormatter.printError(e.getMessage());
        }
    }

    private void changePriority(Scanner sc) {
        int id = CliFormatter.readInt(CliFormatter.PROMPT_ID_TICKET, sc);
        if (findTicket(id) == null) {
            return;
        }
        Priority priority = choosePriority(sc);
        try {
            ViewTicketsFacade.getInstanceSingleton().changePriority(id, priority.name());
            System.out.println("Priorita' aggiornata.");
        } catch (DAOException | TicketNotFoundException e) {
            log.error("Errore aggiornamento priorita' ticket {}", id, e);
            CliFormatter.printInternalError();
        }
    }

    private void searchKb(Scanner sc) {
        System.out.print("Parola chiave: ");
        String keyword = sc.nextLine().trim();
        try {
            List<KnowledgeEntryRecord> results = KnowledgeBaseFacade.getInstanceSingleton().searchEntries(keyword);
            CliFormatter.printKbEntries(results);
        } catch (KnowledgeBaseException e) {
            CliFormatter.printError(e.getMessage());
        } catch (DAOException e) {
            log.error("Errore ricerca knowledge base", e);
            CliFormatter.printInternalError();
        }
    }

    private void addKbEntry(Scanner sc) {
        System.out.print("Titolo: ");
        String title = sc.nextLine().trim();
        System.out.print("Contenuto: ");
        String content = sc.nextLine().trim();
        try {
            KnowledgeBaseFacade.getInstanceSingleton()
                    .addEntry(title, content, UserSession.getInstanceSingleton().getEmail());
            System.out.println("Voce aggiunta alla knowledge base.");
        } catch (KnowledgeBaseException e) {
            CliFormatter.printError(e.getMessage());
        } catch (DAOException e) {
            log.error("Errore aggiunta voce knowledge base", e);
            CliFormatter.printInternalError();
        }
    }

    private TicketRecord findTicket(int id) {
        if (id <= 0) {
            System.out.println(CliFormatter.MSG_ID_NON_VALIDO);
            return null;
        }
        try {
            for (TicketRecord t : ViewTicketsFacade.getInstanceSingleton().getAllTickets()) {
                if (t.id() == id) {
                    return t;
                }
            }
            System.out.println("Ticket non trovato.");
            return null;
        } catch (DAOException e) {
            log.error("Errore ricerca ticket {}", id, e);
            System.out.println("Impossibile recuperare il ticket.");
            return null;
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
            System.out.println(CliFormatter.MSG_SCELTA_NON_VALIDA);
        }
    }
}
