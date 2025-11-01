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

    /*
     * Name: Todd Uhl
     * Course: 202610 Software Development I CEN-3024C-14877
     * Date: 10/31/2025
     *
     * Class: Trip
     * This class represents an individual trip with details such as destination, dates, budget, notes,
     * and completion status. It is the core data model for the Travel Tracker application.
     */

    public Trip(int id, String destination, LocalDate startDate, LocalDate endDate, double budget, String notes, boolean completed) {
        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.notes = notes;
        this.completed = completed;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public String toExportString() {
        return id + "|" + destination + "|" +
                startDate.format(formatter) + "|" +
                endDate.format(formatter) + "|" +
                budget + "|" +
                notes + "|" +
                completed;
    }
}
