package it.uniroma2.dicii.ispw.supportdesk.boundary.javafx;

import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;

public final class SessionContext {

    private static LoginRecord currentUser;

    private SessionContext() {}

    public static synchronized void setCurrentUser(LoginRecord user) {
        currentUser = user;
    }

    public static synchronized LoginRecord getCurrentUser() {
        return currentUser;
    }

    public static synchronized void clear() {
        currentUser = null;
    }
}
