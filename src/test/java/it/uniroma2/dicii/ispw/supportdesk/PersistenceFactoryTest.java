package it.uniroma2.dicii.ispw.supportdesk;

import it.uniroma2.dicii.ispw.supportdesk.dao.factory.DAOAbstractFactory;
import it.uniroma2.dicii.ispw.supportdesk.dao.factory.DAOFactoryDB;
import it.uniroma2.dicii.ispw.supportdesk.dao.factory.DAOFactoryDemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.factory.DAOFactoryFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.KnowledgeBaseDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.NotificationDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.TicketDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.db.UserDAODB;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.TicketDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.demo.UserDAODemo;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.KnowledgeBaseDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.NotificationDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.TicketDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.dao.file.UserDAOFile;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.ApplicationMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PersistenceFactoryTest {

    static Stream<ApplicationMode> demoModeProvider() {
        return Stream.of(ApplicationMode.DEMO);
    }

    static Stream<ApplicationMode> fullDbModeProvider() {
        return Stream.of(ApplicationMode.FULL_DB);
    }

    static Stream<ApplicationMode> fullFileModeProvider() {
        return Stream.of(ApplicationMode.FULL_FILE);
    }

    @ParameterizedTest
    @MethodSource("demoModeProvider")
    void demoMode_ritornaFactoryEDaoCorretti(ApplicationMode mode) {
        DAOAbstractFactory factory = DAOAbstractFactory.getFactory(mode);
        assertInstanceOf(DAOFactoryDemo.class, factory);
        assertInstanceOf(TicketDAODemo.class, factory.createTicketDAO());
        assertInstanceOf(UserDAODemo.class,   factory.createUserDAO());
    }

    @ParameterizedTest
    @MethodSource("fullDbModeProvider")
    void fullDbMode_ritornaFactoryEDaoCorretti(ApplicationMode mode) {
        DAOAbstractFactory factory = DAOAbstractFactory.getFactory(mode);
        assertInstanceOf(DAOFactoryDB.class,       factory);
        assertInstanceOf(TicketDAODB.class,         factory.createTicketDAO());
        assertInstanceOf(UserDAODB.class,            factory.createUserDAO());
        assertInstanceOf(KnowledgeBaseDAODB.class,  factory.createKnowledgeBaseDAO());
        assertInstanceOf(NotificationDAODB.class,   factory.createNotificationDAO());
    }

    @ParameterizedTest
    @MethodSource("fullFileModeProvider")
    void fullFileMode_ritornaFactoryEDaoCorretti(ApplicationMode mode) {
        DAOAbstractFactory factory = DAOAbstractFactory.getFactory(mode);
        assertInstanceOf(DAOFactoryFile.class,       factory);
        assertInstanceOf(TicketDAOFile.class,         factory.createTicketDAO());
        assertInstanceOf(UserDAOFile.class,            factory.createUserDAO());
        assertInstanceOf(KnowledgeBaseDAOFile.class,  factory.createKnowledgeBaseDAO());
        assertInstanceOf(NotificationDAOFile.class,   factory.createNotificationDAO());
    }
}
