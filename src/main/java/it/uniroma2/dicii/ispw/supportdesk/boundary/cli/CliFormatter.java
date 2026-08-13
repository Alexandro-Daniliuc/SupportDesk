package it.uniroma2.dicii.ispw.supportdesk.boundary.cli;

import it.uniroma2.dicii.ispw.supportdesk.record.CommentRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.KnowledgeEntryRecord;
import it.uniroma2.dicii.ispw.supportdesk.record.TicketRecord;

import java.util.List;
import java.util.Scanner;


final class CliFormatter {

    private CliFormatter() {
    }

    static void printTicketTable(List<TicketRecord> tickets) {
        if (tickets.isEmpty()) {
            System.out.println("(nessun ticket)");
            return;
        }
        System.out.printf("%-4s %-25s %-10s %-9s %-11s %-20s %-20s%n",
                "ID", "Titolo", "Categoria", "Priorita", "Stato", "Scadenza SLA", "Tecnico");
        for (TicketRecord t : tickets) {
            System.out.printf("%-4d %-25s %-10s %-9s %-11s %-20s %-20s%n",
                    t.id(), truncate(t.title(), 25), t.getCategory(), t.getPriority(),
                    t.getStatus(), t.getScadenzaSla(),
                    t.getAssignedTechnicianName().isBlank() ? "Non assegnato" : t.getAssignedTechnicianName());
        }
    }

    static void printTicketDetail(TicketRecord t) {
        System.out.println("--- Dettaglio ticket #" + t.id() + " ---");
        System.out.println("Titolo:        " + t.title());
        System.out.println("Descrizione:   " + t.description());
        System.out.println("Categoria:     " + t.getCategory());
        System.out.println("Priorita:      " + t.getPriority());
        System.out.println("Stato:         " + t.getStatus());
        System.out.println("Apertura:      " + t.getDataApertura());
        System.out.println("Scadenza SLA:  " + t.getScadenzaSla());
        System.out.println("Tecnico:       " + (t.getAssignedTechnicianName().isBlank()
                ? "Non assegnato" : t.getAssignedTechnicianName()));
    }

    static void printComments(List<CommentRecord> comments) {
        if (comments.isEmpty()) {
            System.out.println("(nessun commento)");
            return;
        }
        for (CommentRecord c : comments) {
            System.out.println("  [" + c.authorEmail() + "] " + c.text());
        }
    }

    static void printKbEntries(List<KnowledgeEntryRecord> entries) {
        if (entries.isEmpty()) {
            System.out.println("(nessuna voce trovata)");
            return;
        }
        for (KnowledgeEntryRecord e : entries) {
            System.out.println("  #" + e.id() + " " + e.title() + " (" + e.authorName() + ")");
            System.out.println("      " + e.content());
        }
    }

    /** Legge un intero da console; ritorna -1 se l'input non e' un numero valido (mai un'eccezione). */
    static int readInt(String prompt, Scanner sc) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt);
        }
        String raw = sc.nextLine().trim();
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() <= max ? s : s.substring(0, max - 1) + "~";
    }
}
