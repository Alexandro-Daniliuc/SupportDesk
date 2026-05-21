package it.uniroma2.dicii.ispw.supportdesk.bean;

/**
 * Bean per il form di login. Validazione sintattica via isValid().
 * Nessuna logica di dominio: solo trasporto dati dalla boundary al controller.
 */
public class LoginBean {

    private String email;
    private String password;

    public LoginBean() {
        // no-arg required by bean contract
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static final String EMAIL_REGEX = "^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";

    /** Validazione sintattica: formato email corretto e password non vuota. */
    public boolean isValid() {
        return email != null && email.matches(EMAIL_REGEX)
                && password != null && !password.isBlank();
    }
}
