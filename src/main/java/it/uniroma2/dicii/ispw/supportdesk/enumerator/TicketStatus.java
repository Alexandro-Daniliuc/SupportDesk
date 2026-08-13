package it.uniroma2.dicii.ispw.supportdesk.enumerator;

import java.util.Set;


public enum TicketStatus {
    OPEN {
        @Override
        public Set<TicketStatus> nextStates() {
            return Set.of(ASSIGNED);
        }
    },
    ASSIGNED {
        @Override
        public Set<TicketStatus> nextStates() {
            return Set.of(IN_PROGRESS);
        }
    },
    IN_PROGRESS {
        @Override
        public Set<TicketStatus> nextStates() {
            return Set.of(RESOLVED);
        }
    },
    RESOLVED {
        @Override
        public Set<TicketStatus> nextStates() {
            return Set.of(CLOSED, REOPENED);
        }
    },
    CLOSED {
        @Override
        public Set<TicketStatus> nextStates() {
            return Set.of();
        }
    },
    REOPENED {
        @Override
        public Set<TicketStatus> nextStates() {
            return Set.of(ASSIGNED);
        }
    };

    /**
     * Restituisce gli stati verso cui è lecito transitare da questo stato.
     */
    public abstract Set<TicketStatus> nextStates();

    /**
     * Verifica se la transizione verso {@code next} è consentita.
     */
    public boolean canTransitionTo(TicketStatus next) {
        return nextStates().contains(next);
    }
}