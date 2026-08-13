package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.bean.TicketBean;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.TicketFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


final class OpenTicketControllerCli {

    private static final Logger log = LoggerFactory.getLogger(OpenTicketControllerCli.class);

    private OpenTicketControllerCli() {
    }

    static void openTicket(Scanner sc) {
        System.out.println("\n--- Apertura nuovo ticket ---");
        while (true) {
            System.out.print("Titolo: ");
            String title = sc.nextLine().trim();
            System.out.print("Descrizione: ");
            String description = sc.nextLine().trim();
            Category category = chooseEnum(sc, "Categoria", Category.values());
            Priority priority = chooseEnum(sc, "Priorita", Priority.values());

            TicketBean bean = new TicketBean();
            bean.setTitle(title);
            bean.setDescription(description);
            bean.setCategory(category);
            bean.setPriority(priority);

            try {
                TicketBean result = TicketFacade.getInstanceSingleton().openTicketWithWorkflow(bean);
                System.out.println("Ticket #" + result.getId() + " registrato nel sistema.");
                return;
            } catch (DAOException e) {
                log.error("Errore DAO apertura ticket", e);
                System.out.println("Errore interno del sistema. Riprovare piu' tardi.");
                return;
            } catch (SupportDeskException e) {
                System.out.println("Errore di validazione: " + e.getMessage());
                System.out.println("Correggi i dati e riprova.");
            }
        }
    }

    private static <E extends Enum<E>> E chooseEnum(Scanner sc, String label, E[] values) {
        while (true) {
            System.out.println(label + ":");
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
