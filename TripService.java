import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * Name: Todd Uhl
 * Course: 202610 Software Development I CEN-3024C-14877
 * Date: 9/29/2025
 *
 * Class: TripService
 * This class manages a collection of Trip objects, providing functionality to add, find, delete,
 * import, and sort trips. It acts as the business logic layer between the MainApp and the Trip model.
 */
public class TripService {
    private final List<Trip> trips;       // List of all trips
    private int nextId;                   // Auto-incrementing ID counter

    // Constructor initializes empty list and sets ID counter
    public TripService() {
        this.trips = new ArrayList<>();
        this.nextId = 1;
    }

    /*
     * Method: createTrip
     * Purpose: Creates a new trip with an auto-generated ID and adds it to the list.
     * Arguments:
     *  - destination (String)
     *  - startDate (LocalDate)
     *  - endDate (LocalDate)
     *  - budget (double)
     *  - notes (String)
     * Return: Trip - the newly created trip object
     */

    /*
     * Note: Trip IDs are auto-incrementing and unique.
     * Deleted trip IDs are not reused to maintain consistent references.
     * This design choice ensures each trip has a distinct identifier,
     * similar to how primary keys work in databases.
     */

    public Trip createTrip(String destination, LocalDate startDate, LocalDate endDate, double budget, String notes) {
        Trip trip = new Trip(nextId++, destination, startDate, endDate, budget, notes, false);
        trips.add(trip);
        return trip;
    }

    /*
     * Method: addImportedTrip
     * Purpose: Adds a trip from a file import.
     * Arguments:
     *  - trip (Trip): The trip object loaded from file
     * Return: void
     */
    public void addImportedTrip(Trip trip) {
        trips.add(trip);
        if (trip.getId() >= nextId) {
            nextId = trip.getId() + 1;


        }
    }

    /*
     * Method: getTrips
     * Purpose: Returns the full list of trips.
     * Return: List<Trip> - all stored trips
     */
    public List<Trip> getTrips() {
        return trips;
    }

    /*
     * Method: findTrip
     * Purpose: Finds a trip by ID.
     * Arguments:
     *  - id (int): Trip ID
     * Return: Trip - matching trip or null if not found
     */
    public Trip findTrip(int id) {
        return trips.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /*
     * Method: deleteTrip
     * Purpose: Deletes a trip by ID.
     * Arguments:
     *  - id (int): Trip ID
     * Return: boolean - true if trip deleted, false if not found
     */
    public boolean deleteTrip(int id) {
        return trips.removeIf(t -> t.getId() == id);
    }

    /*
     * Sorting methods
     * Purpose: Return new lists of trips sorted by different criteria.
     */
    public List<Trip> sortById() {
        return trips.stream().sorted(Comparator.comparingInt(Trip::getId)).toList();
    }

    public List<Trip> sortByStartDate() {
        return trips.stream().sorted(Comparator.comparing(Trip::getStartDate)).toList();
    }

    public List<Trip> sortByDestination() {
        return trips.stream().sorted(Comparator.comparing(Trip::getDestination, String.CASE_INSENSITIVE_ORDER)).toList();
    }

    public List<Trip> sortByBudgetLowToHigh() {
        return trips.stream().sorted(Comparator.comparingDouble(Trip::getBudget)).toList();
    }

    public List<Trip> sortByBudgetHighToLow() {
        return trips.stream().sorted(Comparator.comparingDouble(Trip::getBudget).reversed()).toList();
    }
}
