package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.bean.RegistrationBean;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.RegistrationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


final class RegistrationControllerCli {

    private static final Logger log = LoggerFactory.getLogger(RegistrationControllerCli.class);

    private RegistrationControllerCli() {
    }

    static void register(Scanner sc) {
        System.out.println("\n--- Registrazione ---");
        System.out.print("Nome: ");
        String name = sc.nextLine().trim();
        System.out.print("Cognome: ");
        String surname = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine();
        System.out.print("Conferma password: ");
        String confirm = sc.nextLine();

        RegistrationBean bean = new RegistrationBean();
        bean.setName(name);
        bean.setSurname(surname);
        bean.setEmail(email);
        bean.setPassword(password);
        bean.setConfirmPassword(confirm);

        try {
            RegistrationFacade.getInstanceSingleton().register(bean);
            System.out.println("Registrazione completata. Ora puoi effettuare il login.");
        } catch (DAOException e) {
            log.error("Errore DAO durante la registrazione", e);
            System.out.println("Errore interno del sistema. Riprovare.");
        } catch (SupportDeskException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}
