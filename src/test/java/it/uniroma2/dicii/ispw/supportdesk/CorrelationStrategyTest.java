package it.uniroma2.dicii.ispw.supportdesk;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import it.uniroma2.dicii.ispw.supportdesk.utility.singleton.CorrelationEngine;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CategoryAwareStrategy;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CorrelationContext;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.CorrelationStrategy;
import it.uniroma2.dicii.ispw.supportdesk.utility.strategy.TextSimilarityStrategy;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Correlation Strategy Tests")
class CorrelationStrategyTest {

    // -------------------------------------------------------------------------
    // CategoryAwareStrategy — verifica i 3 criteri di correlazione
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("CategoryAwareStrategy")
    class CategoryAwareStrategyTests {

        private final CorrelationStrategy strategy = new CategoryAwareStrategy();

        @Test
        @DisplayName("Similarità testuale alta (≥ 0.30): correlati indipendentemente dalla categoria")
        void altaSimilaritaTestuale_correlati() {
            CorrelationContext ctx = new CorrelationContext(0.35, false, 0.0);
            assertTrue(strategy.isCorrelated(ctx));
        }

        @Test
        @DisplayName("Stessa categoria e similarità testuale moderata (≥ 0.10): correlati")
        void stessaCategoria_similaritaModerata_correlati() {
            CorrelationContext ctx = new CorrelationContext(0.15, true, 0.0);
            assertTrue(strategy.isCorrelated(ctx));
        }

        @Test
        @DisplayName("Stessa categoria e keyword in comune (es. 'email'): correlati anche con bassa similarità TF-IDF")
        void stessaCategoria_keywordInComune_correlati() {
            CorrelationContext ctx = new CorrelationContext(0.05, true, 0.08);
            assertTrue(strategy.isCorrelated(ctx));
        }

        @Test
        @DisplayName("Categoria diversa, bassa similarità testuale, nessuna keyword: non correlati")
        void categoriaDiversa_bassaSimilarita_nonCorrelati() {
            CorrelationContext ctx = new CorrelationContext(0.05, false, 0.0);
            assertFalse(strategy.isCorrelated(ctx));
        }

        @Test
        @DisplayName("Stessa categoria ma similarità e keyword entrambi zero: non correlati")
        void stessaCategoria_nessunaMetrica_nonCorrelati() {
            CorrelationContext ctx = new CorrelationContext(0.0, true, 0.0);
            assertFalse(strategy.isCorrelated(ctx));
        }
    }

    // -------------------------------------------------------------------------
    // TextSimilarityStrategy — verifica la soglia testuale pura
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("TextSimilarityStrategy")
    class TextSimilarityStrategyTests {

        private final CorrelationStrategy strategy = new TextSimilarityStrategy();

        @Test
        @DisplayName("Similarità testuale esattamente a soglia (0.30): correlati")
        void similaritaASoglia_correlati() {
            CorrelationContext ctx = new CorrelationContext(0.30, false, 0.0);
            assertTrue(strategy.isCorrelated(ctx));
        }

        @Test
        @DisplayName("Similarità testuale bassa: non correlati anche con stessa categoria e keyword")
        void bassaSimilarita_nonCorrelati_ancheConCategoria() {
            CorrelationContext ctx = new CorrelationContext(0.05, true, 0.50);
            assertFalse(strategy.isCorrelated(ctx));
        }
    }

    // -------------------------------------------------------------------------
    // CorrelationEngine — integrazione end-to-end con dati concreti
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("CorrelationEngine — integrazione")
    class CorrelationEngineIntegrationTests {

        @Test
        @DisplayName("Ticket SOFTWARE con 'email' in comune: trovati come correlati")
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
        @DisplayName("Ticket NETWORK e SOFTWARE senza testo in comune: non correlati")
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
        @DisplayName("Ticket Outlook e Outlook: trovati come correlati per alta similarità testuale")
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
}
