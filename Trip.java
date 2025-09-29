import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*
 * Name: Todd Uhl
 * Course: 202610 Software Development I CEN-3024C-14877
 * Date: 9/29/2025
 *
 * Class: Trip
 * This class represents an individual trip with details such as destination, dates, budget, notes,
 * and completion status. It is the core data model for the Travel Tracker application.
 */
public class Trip {
    private int id;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private double budget;
    private String notes;
    private boolean completed;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Constructor initializes a new Trip object with provided details
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

    /*
     * Method: toString
     * Purpose: Returns a formatted string representing this trip for display in menus.
     * Return: String - human-readable representation of trip details.
     */
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

    /*
     * Method: toExportString
     * Purpose: Returns a string suitable for saving to a file, preserving trip details.
     * Return: String - pipe-delimited representation of trip data.
     */
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
