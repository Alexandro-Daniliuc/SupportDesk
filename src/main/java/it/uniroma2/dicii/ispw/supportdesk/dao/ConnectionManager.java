package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionManager {
    private static final String DB_PROPS_FILE = "db.properties";
    private static final String KEY_URL  = "db.url";
    private static final String KEY_USER = "db.user";
    private static final String KEY_CRED = "db.credential";

    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private final Properties props;

    private ConnectionManager() {
        props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(DB_PROPS_FILE)) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            log.error("Impossibile caricare {}", DB_PROPS_FILE, e);
        }
    }

    private static final class Holder {
        private static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    public static ConnectionManager getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() throws DAOException {
        String url  = props.getProperty(KEY_URL,  System.getProperty(KEY_URL));
        String user = props.getProperty(KEY_USER, System.getProperty(KEY_USER));
        String cred = props.getProperty(KEY_CRED, System.getProperty(KEY_CRED, ""));
        try {
            return DriverManager.getConnection(url, user, cred);
        } catch (SQLException e) {
            throw new DAOException("Impossibile connettersi al database", e);
        }
    }
}
