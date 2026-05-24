package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Role;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Notification;
import it.uniroma2.dicii.ispw.supportdesk.record.NotificationRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private static final AtomicInteger idGen = new AtomicInteger(1);

    public NotificationRecord createNotification(String message, Role targetRole, int ticketId)
            throws DAOException {
        Notification n = new Notification(idGen.getAndIncrement(), message, targetRole, ticketId);
        PersistenceLayerFactory.getInstance().saveNotification(n);
        log.info("Notifica creata per ticket {}", ticketId);
        return toRecord(n);
    }

    public List<NotificationRecord> getNotificationsForRole(Role targetRole) throws DAOException {
        return PersistenceLayerFactory.getInstance().findNotificationsByRole(targetRole)
                .stream().map(this::toRecord).toList();
    }

    public void markAsRead(int notificationId) throws DAOException {
        PersistenceLayerFactory.getInstance().markNotificationAsRead(notificationId);
    }

    private NotificationRecord toRecord(Notification n) {
        return new NotificationRecord(n.getId(), n.getMessage(), n.getTargetRole(),
                n.getTicketId(), n.getCreatedAt(), n.isRead());
    }
}
