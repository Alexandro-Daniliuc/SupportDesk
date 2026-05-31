package it.uniroma2.dicii.ispw.supportdesk;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.CorrelationEngine;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CategoryAwareStrategy;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CorrelationContext;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CorrelationStrategy;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.TextSimilarityStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationStrategyTest {

    private final CorrelationStrategy categoryStrategy = new CategoryAwareStrategy();
    private final CorrelationStrategy textStrategy     = new TextSimilarityStrategy();

    @Test
    void altaSimilaritaTestuale_correlati() {
        CorrelationContext ctx = new CorrelationContext(0.35, false, 0.0);
        assertTrue(categoryStrategy.isCorrelated(ctx));
    }

    @Test
    void stessaCategoria_similaritaModerata_correlati() {
        CorrelationContext ctx = new CorrelationContext(0.15, true, 0.0);
        assertTrue(categoryStrategy.isCorrelated(ctx));
    }

    @Test
    void stessaCategoria_keywordInComune_correlati() {
        CorrelationContext ctx = new CorrelationContext(0.05, true, 0.08);
        assertTrue(categoryStrategy.isCorrelated(ctx));
    }

    @Test
    void categoriaDiversa_bassaSimilarita_nonCorrelati() {
        CorrelationContext ctx = new CorrelationContext(0.05, false, 0.0);
        assertFalse(categoryStrategy.isCorrelated(ctx));
    }

    @Test
    void stessaCategoria_nessunaMetrica_nonCorrelati() {
        CorrelationContext ctx = new CorrelationContext(0.0, true, 0.0);
        assertFalse(categoryStrategy.isCorrelated(ctx));
    }

    @Test
    void similaritaASoglia_correlati() {
        CorrelationContext ctx = new CorrelationContext(0.30, false, 0.0);
        assertTrue(textStrategy.isCorrelated(ctx));
    }

    @Test
    void bassaSimilarita_nonCorrelati_ancheConCategoria() {
        CorrelationContext ctx = new CorrelationContext(0.05, true, 0.50);
        assertFalse(textStrategy.isCorrelated(ctx));
    }

    @Test
    void ticketEmailSoftware_trovatiCorrelati() {
        Ticket impossibileInviare = new Ticket.Builder(3,
                "Impossibile inviare email",
                "Errore SMTP 550 durante l'invio",
                Category.SOFTWARE, Priority.MEDIUM).build();

        Ticket configurazioneEmail = new Ticket.Builder(4,
                "Configurazione account email",
                "Account IMAP da riconfigurare dopo migrazione",
                Category.SOFTWARE, Priority.LOW).build();

        List<Ticket> correlated = CorrelationEngine.getInstanceSingleton()
                .findCorrelatedTickets(impossibileInviare, List.of(configurazioneEmail));

        assertFalse(correlated.isEmpty(), "Il ticket 4 dovrebbe risultare correlato al ticket 3");
    }

    @Test
    void ticketCategorieDiverse_nonCorrelati() {
        Ticket network = new Ticket.Builder(5,
                "Connessione di rete assente",
                "PC non ottiene indirizzo IP dal DHCP",
                Category.NETWORK, Priority.CRITICAL).build();

        Ticket software = new Ticket.Builder(9,
                "Aggiornamento Windows bloccato",
                "Aggiornamento bloccato al trenta percento da quarantotto ore",
                Category.SOFTWARE, Priority.LOW).build();

        List<Ticket> correlated = CorrelationEngine.getInstanceSingleton()
                .findCorrelatedTickets(network, List.of(software));

        assertTrue(correlated.isEmpty());
    }

    @Test
    void ticketOutlook_altaSimilarita_correlati() {
        Ticket outlook1 = new Ticket.Builder(1,
                "Outlook non si apre",
                "Outlook non risponde all'avvio",
                Category.SOFTWARE, Priority.HIGH).build();

        Ticket outlook2 = new Ticket.Builder(2,
                "Outlook non funziona su laptop",
                "Crash all'apertura della casella di posta",
                Category.SOFTWARE, Priority.HIGH).build();

        List<Ticket> correlated = CorrelationEngine.getInstanceSingleton()
                .findCorrelatedTickets(outlook1, List.of(outlook2));

        assertFalse(correlated.isEmpty(), "I due ticket Outlook dovrebbero risultare correlati");
    }
}
