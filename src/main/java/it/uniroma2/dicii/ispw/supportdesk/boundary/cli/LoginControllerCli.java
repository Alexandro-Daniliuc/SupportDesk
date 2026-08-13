package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.SupportDeskException;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.facade.LoginFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


final class LoginControllerCli {

    private static final Logger log = LoggerFactory.getLogger(LoginControllerCli.class);

    private LoginControllerCli() {
    }


    static LoginRecord login(Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine();

        LoginBean bean = new LoginBean();
        bean.setEmail(email);
        bean.setPassword(password);

        try {
            LoginRecord record = LoginFacade.getInstanceSingleton().login(bean);
            System.out.println("Accesso effettuato. Benvenuto, " + record.name() + " " + record.surname() + ".");
            return record;
        } catch (DAOException e) {
            log.error("Errore DAO durante il login", e);
            System.out.println("Errore interno del sistema. Riprovare.");
            return null;
        } catch (SupportDeskException e) {
            System.out.println("Errore: " + e.getMessage());
            return null;
        }
    }
}
