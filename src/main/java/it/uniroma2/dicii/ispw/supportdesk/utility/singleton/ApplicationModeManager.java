package it.uniroma2.dicii.ispw.supportdesk.utility.singleton;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.utility.configloader.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("java:S6548")
public final class ApplicationModeManager {

    private static final String MODE_KEY = "app.mode";

    private static final Logger log = LoggerFactory.getLogger(ApplicationModeManager.class);

    private volatile ApplicationMode mode;

    private ApplicationModeManager() {
        String raw = ConfigLoader.getInstanceSingleton().get(MODE_KEY, ApplicationMode.DEMO.name());
        mode = parseMode(raw);
    }

    private static final class Holder {
        private static final ApplicationModeManager INSTANCE = new ApplicationModeManager();
    }

    public static ApplicationModeManager getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public ApplicationMode getMode() {
        return mode;
    }

    public void setMode(ApplicationMode mode) {
        this.mode = mode;
    }

    private ApplicationMode parseMode(String raw) {
        try {
            return ApplicationMode.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Valore app.mode non riconosciuto: {} — uso DEMO", raw);
            return ApplicationMode.DEMO;
        }
    }
}
