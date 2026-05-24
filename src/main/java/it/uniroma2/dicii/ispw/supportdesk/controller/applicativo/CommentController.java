package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ValidationException;
import it.uniroma2.dicii.ispw.supportdesk.model.Comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    public void addComment(int ticketId, String authorEmail, String text)
            throws ValidationException, DAOException {
        if (text == null || text.isBlank()) {
            throw new ValidationException("Il testo del commento non puÃ² essere vuoto");
        }
        int nextId = PersistenceLayerFactory.getInstance().findAllComments()
                .stream().mapToInt(Comment::getId).max().orElse(0) + 1;
        Comment comment = new Comment(nextId, ticketId, authorEmail, text);
        PersistenceLayerFactory.getInstance().saveComment(comment);
        if (log.isInfoEnabled()) {
            log.info("Commento {} aggiunto al ticket {} da {}", nextId, ticketId, authorEmail);
        }
    }

    public List<Comment> getCommentsForTicket(int ticketId) throws DAOException {
        return PersistenceLayerFactory.getInstance().findCommentsByTicketId(ticketId);
    }
}

