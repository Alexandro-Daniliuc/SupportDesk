package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.bean.LoginBean;
import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.exception.AuthenticationException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.User;
import it.uniroma2.dicii.ispw.supportdesk.record.LoginRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.UserSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String SHA_256                  = "SHA-256";
    private static final String ERR_CREDENZIALI          = "Credenziali non valide";
    private static final String ERR_CREDENZIALI_MISSING  = "Credenziali mancanti o non valide";

    public LoginRecord authenticate(LoginBean bean)
            throws ValidationException, AuthenticationException, DAOException {
        if (!bean.isValid()) {
            throw new ValidationException(ERR_CREDENZIALI_MISSING);
        }
        String hash = sha256(bean.getPassword());
        User user = PersistenceLayerFactory.getInstance().login(bean.getEmail(), hash);
        if (user == null) {
            throw new AuthenticationException(ERR_CREDENZIALI);
        }
        UserSession.getInstanceSingleton().setUser(user);
        log.info("Autenticazione riuscita per {}", bean.getEmail());
        return toRecord(user);
    }

    private String sha256(String input) throws AuthenticationException {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_256);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationException("Errore interno di autenticazione", e);
        }
    }

    public boolean isUserLogged() {
        return UserSession.getInstanceSingleton().isLoggedIn();
    }

    private LoginRecord toRecord(User user) {
        return new LoginRecord(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getRole()
        );
    }
}
