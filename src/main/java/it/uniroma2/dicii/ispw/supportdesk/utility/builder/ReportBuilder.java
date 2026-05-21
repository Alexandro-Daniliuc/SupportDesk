package it.uniroma2.dicii.ispw.supportdesk.utility.builder;

import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ReportBuilder {

    private String title = "";
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private final List<Ticket> tickets = new ArrayList<>();

    public ReportBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ReportBuilder withPeriod(LocalDateTime start, LocalDateTime end) {
        this.periodStart = start;
        this.periodEnd   = end;
        return this;
    }

    public ReportBuilder withTickets(List<Ticket> tickets) {
        this.tickets.addAll(tickets);
        return this;
    }

    public Report build() {
        return new Report(title, periodStart, periodEnd, List.copyOf(tickets));
    }

    public record Report(
            String title,
            LocalDateTime periodStart,
            LocalDateTime periodEnd,
            List<Ticket> tickets
    ) {
        public int totalTickets() { return tickets.size(); }
    }
}
