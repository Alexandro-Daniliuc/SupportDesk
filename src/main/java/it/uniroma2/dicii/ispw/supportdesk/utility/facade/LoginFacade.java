package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.LoginController;
import it.uniroma2.dicii.ispw.supportdesk.exception.AuthenticationException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;

@SuppressWarnings("java:S6548")
public final class LoginFacade {

    private final LoginController loginController;

    private LoginFacade() {
        loginController = new LoginController();
    }

    private static final class Holder {
        private static final LoginFacade INSTANCE = new LoginFacade();
    }

    public static LoginFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public LoginRecord login(LoginBean bean)
            throws ValidationException, AuthenticationException, DAOException {
        return loginController.authenticate(bean);
    }
}
