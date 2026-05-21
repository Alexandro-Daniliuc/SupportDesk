package it.uniroma2.dicii.ispw.supportdesk.record;

import java.time.LocalDateTime;

/**
 * Snapshot immutabile di un articolo della knowledge base restituito alla boundary.
 */
public record KnowledgeEntryRecord(
        int id,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt
) {}
