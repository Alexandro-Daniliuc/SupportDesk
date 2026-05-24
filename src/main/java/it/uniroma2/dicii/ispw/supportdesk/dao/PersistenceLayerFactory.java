package it.uniroma2.dicii.ispw.supportdesk.dao;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.ApplicationModeManager;

public class PersistenceLayerFactory {

    private PersistenceLayerFactory() {}

    private static final class Holder {
        private static final PersistenceLayer INSTANCE = createInstance();

        private static PersistenceLayer createInstance() {
            if (ApplicationModeManager.getInstanceSingleton().getMode() == ApplicationMode.DEMO) {
                return new PersistenceLayerDemo();
            }
            return new PersistenceLayerFull();
        }
    }

    public static PersistenceLayer getInstance() {
        return Holder.INSTANCE;
    }
}
