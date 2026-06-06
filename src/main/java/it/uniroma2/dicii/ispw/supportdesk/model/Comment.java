package it.uniroma2.dicii.ispw.supportdesk.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Comment {

    private final int id;
    private final int ticketId;
    private final String authorEmail;
    private final String text;
    private final LocalDateTime createdAt;

    public Comment(int id, int ticketId, String authorEmail, String text) {
        this.id          = id;
        this.ticketId    = ticketId;
        this.authorEmail = authorEmail;
        this.text        = text;
        this.createdAt   = LocalDateTime.now(ZoneId.systemDefault());
    }

    public Comment(int id, int ticketId, String authorEmail, String text, LocalDateTime createdAt) {
        this.id          = id;
        this.ticketId    = ticketId;
        this.authorEmail = authorEmail;
        this.text        = text;
        this.createdAt   = createdAt;
    }

    public int getId()            { return id; }
    public int getTicketId()      { return ticketId; }
    public String getAuthorEmail(){ return authorEmail; }
    public String getText()       { return text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
