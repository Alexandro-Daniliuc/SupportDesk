package it.uniroma2.dicii.ispw.supportdesk.utility.singleton;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

@SuppressWarnings("java:S6548")
public final class UserSession {

    private String email;
    private String name;
    private String surname;
    private Role   role;

    private UserSession() {}

    private static final class LazyHolder {
        private static final UserSession INSTANCE = new UserSession();
    }

    public static UserSession getInstanceSingleton() {
        return LazyHolder.INSTANCE;
    }

    public void setUser(User user) {
        this.email   = user.getEmail();
        this.name    = user.getName();
        this.surname = user.getSurname();
        this.role    = user.getRole();
    }

    public void clear() {
        this.email   = null;
        this.name    = null;
        this.surname = null;
        this.role    = null;
    }

    public String getEmail()   { return email; }
    public String getName()    { return name; }
    public String getSurname() { return surname; }
    public Role   getRole()    { return role; }

    public boolean isLoggedIn() {
        return email != null;
    }
}
