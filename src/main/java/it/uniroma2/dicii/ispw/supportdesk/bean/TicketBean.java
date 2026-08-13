package it.uniroma2.dicii.ispw.supportdesk.bean;

import it.uniroma2.dicii.ispw.supportdesk.enumerator.Category;
import it.uniroma2.dicii.ispw.supportdesk.enumerator.Priority;


public class TicketBean {

    private int    id;
    private String title;
    private String description;
    private Category category;
    private Priority priority;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public TicketBean() {
        // no-arg required by bean contract
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**  tutti i campi obbligatori presenti e non vuoti. */
    public boolean isValid() {
        return title != null && !title.isBlank()
                && description != null && !description.isBlank()
                && category != null
                && priority != null;
    }
}

