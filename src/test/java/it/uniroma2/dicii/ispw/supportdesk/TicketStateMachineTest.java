package it.uniroma2.dicii.ispw.supportdesk;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.TicketStatus;
import it.uniroma2.dicii.ispw.supportdesk.exception.InvalidTransitionException;
import it.uniroma2.dicii.ispw.supportdesk.model.Ticket;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TicketStateMachineTest {

    private Ticket buildTicket() {
        return new Ticket.Builder(1, "Test Ticket", "Descrizione test", Category.SOFTWARE, Priority.MEDIUM)
                .authorEmail("test@test.it")
                .build();
    }

    @Test
    void percorsoCompleto_OPEN_to_CLOSED() throws InvalidTransitionException {
        Ticket ticket = buildTicket();
        assertEquals(TicketStatus.OPEN, ticket.getStatus());

        ticket.changeStatus(TicketStatus.ASSIGNED);
        assertEquals(TicketStatus.ASSIGNED, ticket.getStatus());

        ticket.changeStatus(TicketStatus.IN_PROGRESS);
        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());

        ticket.changeStatus(TicketStatus.RESOLVED);
        assertEquals(TicketStatus.RESOLVED, ticket.getStatus());

        ticket.changeStatus(TicketStatus.CLOSED);
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
    }

    @Test
    void percorsoRiapertura_RESOLVED_REOPENED_ASSIGNED() throws InvalidTransitionException {
        Ticket ticket = buildTicket();
        ticket.changeStatus(TicketStatus.ASSIGNED);
        ticket.changeStatus(TicketStatus.IN_PROGRESS);
        ticket.changeStatus(TicketStatus.RESOLVED);

        ticket.changeStatus(TicketStatus.REOPENED);
        assertEquals(TicketStatus.REOPENED, ticket.getStatus());

        ticket.changeStatus(TicketStatus.ASSIGNED);
        assertEquals(TicketStatus.ASSIGNED, ticket.getStatus());
    }

    @Test
    void transizioneNonValida_OPEN_RESOLVED_lancia_eccezione() {
        Ticket ticket = buildTicket();
        assertThrows(InvalidTransitionException.class,
                () -> ticket.changeStatus(TicketStatus.RESOLVED));
    }

    @Test
    void statoCLOSED_nessunaTranisizioneConsentita() throws InvalidTransitionException {
        Ticket ticket = buildTicket();
        ticket.changeStatus(TicketStatus.ASSIGNED);
        ticket.changeStatus(TicketStatus.IN_PROGRESS);
        ticket.changeStatus(TicketStatus.RESOLVED);
        ticket.changeStatus(TicketStatus.CLOSED);

        assertThrows(InvalidTransitionException.class, () -> ticket.changeStatus(TicketStatus.OPEN));
        assertThrows(InvalidTransitionException.class, () -> ticket.changeStatus(TicketStatus.ASSIGNED));
    }
}
