package it.uniroma2.dicii.ispw.supportdesk.record;

import java.time.LocalDateTime;
import java.util.List;

public record ReportRecord(
        String title,
        LocalDateTime periodStart,
        LocalDateTime periodEnd,
        int totalTickets,
        List<TicketRecord> tickets
) {}
