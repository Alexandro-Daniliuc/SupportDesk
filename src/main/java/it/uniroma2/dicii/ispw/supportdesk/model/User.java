package it.uniroma2.dicii.ispw.supportdesk.model;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;

/**
 * Utente del sistema. Dumb data holder: nessuna logica di dominio.
 * Il campo passwordHash non contiene mai la password in chiaro.
 */
public class User {

    private final int id;
    private final String name;
    private final String surname;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private String specialization;

    public User(int id, String name, String surname, String email, String passwordHash, Role role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
