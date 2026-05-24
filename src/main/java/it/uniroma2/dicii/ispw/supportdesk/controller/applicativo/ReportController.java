package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.exception.ReportException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.builder.ReportBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);


    public ReportBuilder.Report generateReport(String title, LocalDateTime start, LocalDateTime end)
            throws ReportException, DAOException {
        if (title == null || title.isBlank()) {
            throw new ReportException("Titolo del report Ã¨ obbligatorio");
        }
        List<Ticket> all = PersistenceLayerFactory.getInstance().findAllTickets();
        List<Ticket> filtered = filterByPeriod(all, start, end);
        log.info("Report generato: {} ticket", filtered.size());
        return new ReportBuilder()
                .withTitle(title)
                .withPeriod(start, end)
                .withTickets(filtered)
                .build();
    }

    private List<Ticket> filterByPeriod(List<Ticket> tickets, LocalDateTime start, LocalDateTime end) {
        return tickets.stream()
                .filter(t -> isInPeriod(t.getDataApertura(), start, end))
                .toList();
    }

    private boolean isInPeriod(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        boolean afterStart = start == null || !date.isBefore(start);
        boolean beforeEnd  = end   == null || !date.isAfter(end);
        return afterStart && beforeEnd;
    }
}

