package it.uniroma2.dicii.ispw.supportdesk.utility.configloader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@SuppressWarnings("java:S6548")
public final class ConfigLoader {

    private static final String DB_PROPERTIES = "db.properties";

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);

    private final Properties properties = new Properties();

    private ConfigLoader() {
        load();
    }

    private static final class Holder {
        private static final ConfigLoader INSTANCE = new ConfigLoader();
    }

    public static ConfigLoader getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    private void load() {
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(DB_PROPERTIES)) {
            if (in != null) {
                properties.load(in);
            } else {
                log.warn("File {} non trovato nel classpath", DB_PROPERTIES);
            }
        } catch (IOException e) {
            log.error("Errore caricamento {}", DB_PROPERTIES, e);
        }
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
