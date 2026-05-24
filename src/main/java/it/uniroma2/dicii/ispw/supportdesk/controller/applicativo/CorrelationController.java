package it.uniroma2.dicii.ispw.supportdesk.controller.applicativo;

import it.uniroma2.dicii.ispw.supportdesk.dao.PersistenceLayerFactory;
import it.uniroma2.dicii.ispw.supportdesk.exception.CorrelationEngineException;
import it.uniroma2.dicii.ispw.supportdesk.exception.DAOException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.CorrelationEngine;

import java.util.List;

public class CorrelationController {

    public List<Ticket> findCorrelations(Ticket target)
            throws CorrelationEngineException, DAOException {
        List<Ticket> all = PersistenceLayerFactory.getInstance().findAllTickets();
        List<Ticket> candidates = all.stream()
                .filter(t -> t.getId() != target.getId())
                .toList();
        try {
            return CorrelationEngine.getInstanceSingleton()
                    .findCorrelatedTickets(target, candidates);
        } catch (Exception e) {
            throw new CorrelationEngineException(
                    "Errore nell'analisi di correlazione per ticket " + target.getId(), e);
        }
    }
}
