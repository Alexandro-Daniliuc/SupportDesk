package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.bean.CommentBean;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.LoginFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.ViewTicketsFacade;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;


final class UserDashboardControllerCli {

    private static final Logger log = LoggerFactory.getLogger(UserDashboardControllerCli.class);

    void run(Scanner sc) {
        boolean inDashboard = true;
        while (inDashboard) {
            System.out.println("\n=== Dashboard Utente - " + UserSession.getInstanceSingleton().getName() + " ===");
            System.out.println("1) Visualizza i miei ticket");
            System.out.println("2) Dettaglio ticket e commenti");
            System.out.println("3) Aggiungi commento");
            System.out.println("4) Riapri ticket risolto");
            System.out.println("5) Apri nuovo ticket");
            System.out.println("6) Logout");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> listMyTickets();
                case "2" -> showDetail(sc);
                case "3" -> addComment(sc);
                case "4" -> reopenTicket(sc);
                case "5" -> OpenTicketControllerCli.openTicket(sc);
                case "6" -> inDashboard = false;
                default -> System.out.println("Scelta non valida.");
            }
        }
        LoginFacade.getInstanceSingleton().logout();
        System.out.println("Logout effettuato.");
    }

    private void listMyTickets() {
        try {
            List<TicketRecord> tickets = ViewTicketsFacade.getInstanceSingleton()
                    .getTicketsByUser(UserSession.getInstanceSingleton().getEmail());
            CliFormatter.printTicketTable(tickets);
        } catch (DAOException e) {
            log.error("Errore caricamento ticket utente", e);
            System.out.println("Impossibile caricare i ticket.");
        }
    }

    private void showDetail(Scanner sc) {
        int id = CliFormatter.readInt("ID ticket: ", sc);
        TicketRecord t = findMyTicket(id);
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

    private void addComment(Scanner sc) {
        int id = CliFormatter.readInt("ID ticket: ", sc);
        if (findMyTicket(id) == null) {
            return;
        }
        System.out.print("Commento: ");
        String text = sc.nextLine();

        CommentBean bean = new CommentBean();
        bean.setTicketId(id);
        bean.setAuthorEmail(UserSession.getInstanceSingleton().getEmail());
        bean.setText(text);
        try {
            ViewTicketsFacade.getInstanceSingleton().addComment(bean);
            System.out.println("Commento aggiunto.");
        } catch (DAOException e) {
            log.error("Errore aggiunta commento al ticket {}", id, e);
            System.out.println("Impossibile salvare il commento.");
        } catch (SupportDeskException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void reopenTicket(Scanner sc) {
        int id = CliFormatter.readInt("ID ticket da riaprire: ", sc);
        TicketRecord t = findMyTicket(id);
        if (t == null) {
            return;
        }
        if (t.status() != TicketStatus.RESOLVED) {
            System.out.println("Solo un ticket risolto puo' essere riaperto.");
            return;
        }
        try {
            ViewTicketsFacade.getInstanceSingleton().changeStatus(id, TicketStatus.REOPENED);
            System.out.println("Ticket riaperto.");
        } catch (DAOException e) {
            log.error("Errore riapertura ticket {}", id, e);
            System.out.println("Errore interno del sistema.");
        } catch (SupportDeskException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private TicketRecord findMyTicket(int id) {
        if (id <= 0) {
            System.out.println("ID non valido.");
            return null;
        }
        try {
            List<TicketRecord> mine = ViewTicketsFacade.getInstanceSingleton()
                    .getTicketsByUser(UserSession.getInstanceSingleton().getEmail());
            for (TicketRecord t : mine) {
                if (t.id() == id) {
                    return t;
                }
            }
            System.out.println("Ticket non trovato tra i tuoi ticket.");
            return null;
        } catch (DAOException e) {
            log.error("Errore ricerca ticket {}", id, e);
            System.out.println("Impossibile recuperare il ticket.");
            return null;
        }
    }
}
