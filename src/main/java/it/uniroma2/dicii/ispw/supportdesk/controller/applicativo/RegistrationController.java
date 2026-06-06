package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.bean.RegistrationBean;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationController {

    public void register(RegistrationBean bean) throws ValidationException, DAOException {
        if (!bean.isValid()) {
            throw new ValidationException("Dati mancanti, email non valida o password non coincidenti");
        }
        if (PersistenceLayerFactory.getInstance().findUserByEmail(bean.getEmail()) != null) {
            throw new ValidationException("email", "Email già registrata");
        }
        String hash = sha256(bean.getPassword());
        User user = new User(0, bean.getName(), bean.getSurname(), bean.getEmail(), hash, Role.USER);
        PersistenceLayerFactory.getInstance().insertUser(user);
    }

    private String sha256(String input) throws ValidationException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ValidationException("Errore interno durante la registrazione", e);
        }
    }
}
