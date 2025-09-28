import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Trip {
    private int id;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private double budget;
    private String notes;
    private boolean completed;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Trip(int id, String destination, LocalDate startDate, LocalDate endDate, double budget, String notes) {
        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.notes = notes;
        this.completed = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // String for display in menu
    @Override
    public String toString() {
        return String.format("Trip #%d | %s | %s - %s | $%.2f | %s | %s",
                id,
                destination,
                startDate.format(formatter),
                endDate.format(formatter),
                budget,
                notes,
                completed ? "Completed" : "Planning");
    }

    // String for export/import (keep same format)
    public String toExportString() {
        return String.format("%d|%s|%s|%s|%.2f|%s|%b",
                id,
                destination,
                startDate.format(formatter),
                endDate.format(formatter),
                budget,
                notes,
                completed);
    }
}
