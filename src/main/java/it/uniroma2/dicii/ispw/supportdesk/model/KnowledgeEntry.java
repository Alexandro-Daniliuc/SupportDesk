package it.uniroma2.dicii.ispw.supportdesk.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Articolo della knowledge base — soluzione documentata a un problema ricorrente.
 * data holder.
 */
public class KnowledgeEntry {

    private final int id;
    private final String title;
    private final String content;
    private final User author;
    private final LocalDateTime createdAt;

    public KnowledgeEntry(int id, String title, String content, User author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now(ZoneId.systemDefault());
    }

    public KnowledgeEntry(int id, String title, String content, User author, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
