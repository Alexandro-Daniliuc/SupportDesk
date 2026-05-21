package it.uniroma2.dicii.ispw.supportdesk.record;

import java.time.LocalDateTime;

public record CommentRecord(int id, String authorEmail, String text, LocalDateTime createdAt) {}
