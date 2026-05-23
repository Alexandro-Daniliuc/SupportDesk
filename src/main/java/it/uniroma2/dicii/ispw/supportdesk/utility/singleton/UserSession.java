package it.uniroma2.dicii.ispw.supportdesk.utility.singleton;

import it.uniroma2.dicii.ispw.supportdesk.model.User;

public final class UserSession {

    private User currentUser;

    private UserSession() {}

    private static final class Holder {
        private static final UserSession INSTANCE = new UserSession();
    }

    public static UserSession getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
