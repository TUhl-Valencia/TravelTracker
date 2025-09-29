import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/*
 * Name: Todd Uhl
 * Course: 202610 Software Development I CEN-3024C-14877
 * Date: 9/29/2025
 *
 * Class: TripService
 * This class manages all business logic for handling trips.
 * It provides methods to create, find, sort, and delete trips.
 */
public class TripService {
    private List<Trip> trips = new ArrayList<>(); // All trips stored here
    private int nextId = 1; // Keeps trip IDs unique

    /*
     * Method: createTrip
     * Purpose: Creates a new Trip object with a unique ID and adds it to the trip list.
     * Arguments:
     *  - destination (String): The trip destination
     *  - startDate (LocalDate): Start date of the trip
     *  - endDate (LocalDate): End date of the trip
     *  - budget (double): Budget for the trip
     *  - notes (String): Notes about the trip
     * Return: Trip - the newly created Trip object
     */
    public Trip createTrip(String destination, LocalDate startDate, LocalDate endDate, double budget, String notes) {
        Trip trip = new Trip(nextId++, destination, startDate, endDate, budget, notes);
        trips.add(trip);
        return trip;
    }

    /*
     * Method: addImportedTrip
     * Purpose: Adds a Trip object from an imported file to the list, preserving its ID.
     * Arguments:
     *  - trip (Trip): Trip object being imported
     * Return: void
     */
    public void addImportedTrip(Trip trip) {
        trips.add(trip);
        nextId = Math.max(nextId, trip.getId() + 1);
    }

    /*
     * Method: getTrips
     * Purpose: Returns the list of all trips.
     * Return: List<Trip> - all trips currently stored
     */
    public List<Trip> getTrips() {
        return trips;
    }

    /*
     * Method: findTrip
     * Purpose: Finds and returns a trip by its ID.
     * Arguments:
     *  - id (int): The ID of the trip to search for
     * Return: Trip - the trip with the matching ID or null if not found
     */
    public Trip findTrip(int id) {
        return trips.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /*
     * Method: deleteTrip
     * Purpose: Deletes a trip by its ID.
     * Arguments:
     *  - id (int): The ID of the trip to delete
     * Return: boolean true if the trip was removed or false if not found
     */
    public boolean deleteTrip(int id) {
        return trips.removeIf(t -> t.getId() == id);
    }

    /*
     * Methods: sortById, sortByStartDate, sortByDestination, sortByBudgetLowToHigh, sortByBudgetHighToLow
     * Purpose: Return sorted lists of trips based on different criteria.
     * Return: List<Trip> - sorted list according to selected method
     */
    public List<Trip> sortById() { return trips.stream().sorted(Comparator.comparingInt(Trip::getId)).collect(Collectors.toList()); }
    public List<Trip> sortByStartDate() { return trips.stream().sorted(Comparator.comparing(Trip::getStartDate)).collect(Collectors.toList()); }
    public List<Trip> sortByDestination() { return trips.stream().sorted(Comparator.comparing(Trip::getDestination)).collect(Collectors.toList()); }
    public List<Trip> sortByBudgetLowToHigh() { return trips.stream().sorted(Comparator.comparingDouble(Trip::getBudget)).collect(Collectors.toList()); }
    public List<Trip> sortByBudgetHighToLow() { return trips.stream().sorted(Comparator.comparingDouble(Trip::getBudget).reversed()).collect(Collectors.toList()); }
}
