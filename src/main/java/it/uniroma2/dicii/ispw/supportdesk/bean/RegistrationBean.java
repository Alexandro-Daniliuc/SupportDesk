package it.uniroma2.dicii.ispw.supportdesk.bean;

public class RegistrationBean {

    private static final String EMAIL_REGEX = "^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";

    private String name;
    private String surname;
    private String email;
    private String password;
    private String confirmPassword;

    public String getName()            { return name; }
    public void   setName(String v)    { this.name = v; }

    public String getSurname()         { return surname; }
    public void   setSurname(String v) { this.surname = v; }

    public String getEmail()           { return email; }
    public void   setEmail(String v)   { this.email = v; }

    public String getPassword()        { return password; }
    public void   setPassword(String v){ this.password = v; }

    public String getConfirmPassword()         { return confirmPassword; }
    public void   setConfirmPassword(String v) { this.confirmPassword = v; }

    public boolean isValid() {
        return name != null && !name.isBlank()
                && surname != null && !surname.isBlank()
                && email != null && email.matches(EMAIL_REGEX)
                && password != null && !password.isBlank()
                && password.equals(confirmPassword);
    }
}
