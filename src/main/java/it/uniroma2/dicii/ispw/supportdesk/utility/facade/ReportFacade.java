package it.uniroma2.dicii.ispw.supportdesk.utility.facade;

import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.ReportController;
import it.uniroma2.dicii.ispw.supportdesk.controller.applicativo.TicketController;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ReportException;
import it.uniroma2.dicii.ispw.supportdesk.record.ReportRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;
import it.uniroma2.dicii.ispw.supportdesk.utility.builder.ReportBuilder;

import java.time.LocalDateTime;
import java.util.List;

public final class ReportFacade {

    private final ReportController reportController;

    private ReportFacade() {
        reportController = new ReportController();
    }

    private static final class Holder {
        private static final ReportFacade INSTANCE = new ReportFacade();
    }

    public static ReportFacade getInstanceSingleton() {
        return Holder.INSTANCE;
    }

    public ReportRecord generateReport(String title, LocalDateTime start, LocalDateTime end)
            throws ReportException, DAOException {
        ReportBuilder.Report raw = reportController.generateReport(title, start, end);
        List<TicketRecord> ticketRecords = raw.tickets().stream()
                .map(TicketController::toRecord)
                .toList();
        return new ReportRecord(raw.title(), raw.periodStart(), raw.periodEnd(),
                raw.totalTickets(), ticketRecords);
    }
}
