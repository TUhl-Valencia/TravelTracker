import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Handles logic and storage of trips
public class TripService {
    private List<Trip> trips = new ArrayList<>(); // All trips stored here
    private int nextId = 1; // Keeps trip IDs unique

    // Create a new trip and add to list
    public Trip createTrip(String destination, LocalDate startDate, LocalDate endDate, double budget, String notes) {
        Trip trip = new Trip(nextId++, destination, startDate, endDate, budget, notes);
        trips.add(trip);
        return trip;
    }

    // Add trip when importing from file (preserves ID)
    public void addImportedTrip(Trip trip) {
        trips.add(trip);
        nextId = Math.max(nextId, trip.getId() + 1);
    }

    // Get all trips
    public List<Trip> getTrips() {
        return trips;
    }

    // Find trip by ID
    public Trip findTrip(int id) {
        return trips.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    // Delete trip by ID
    public boolean deleteTrip(int id) {
        return trips.removeIf(t -> t.getId() == id);
    }

    // Sort methods
    public List<Trip> sortById() {
        return trips.stream().sorted(Comparator.comparingInt(Trip::getId)).collect(Collectors.toList());
    }

    public List<Trip> sortByStartDate() {
        return trips.stream().sorted(Comparator.comparing(Trip::getStartDate)).collect(Collectors.toList());
    }

    public List<Trip> sortByDestination() {
        return trips.stream().sorted(Comparator.comparing(Trip::getDestination)).collect(Collectors.toList());
    }

    public List<Trip> sortByBudgetLowToHigh() {
        return trips.stream().sorted(Comparator.comparingDouble(Trip::getBudget)).collect(Collectors.toList());
    }

    public List<Trip> sortByBudgetHighToLow() {
        return trips.stream().sorted(Comparator.comparingDouble(Trip::getBudget).reversed()).collect(Collectors.toList());
    }
}
