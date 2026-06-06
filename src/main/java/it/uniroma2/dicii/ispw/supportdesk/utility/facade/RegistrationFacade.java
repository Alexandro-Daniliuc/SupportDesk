package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.bean.RegistrationBean;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.RegistrationController;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;

public final class RegistrationFacade {

    private final RegistrationController registrationController;

    private RegistrationFacade() {
        registrationController = new RegistrationController();
    }

    private static final class Holder {
        private static final RegistrationFacade INSTANCE = new RegistrationFacade();
    }

    public static RegistrationFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public void register(RegistrationBean bean) throws ValidationException, DAOException {
        registrationController.register(bean);
    }
}
