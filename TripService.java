import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Name: Todd Uhl
 * Course: 202610 Software Development I CEN-3024C-14877
 * Date: 11/3/2025
 *
 * Class: TripService
 * This class manages a collection of Trip objects, providing functionality to add, find, delete,
 * import, and sort trips. It acts as the business logic layer between the MainApp and the Trip model.
 */


/**
 * TripService manages all database operations related to Trip objects.
 * Provides methods to create, read, update, delete, and export trips.
 * Acts as the business logic layer between the GUI and the Trip data model.
 */
public class TripService {

    private final Connection conn;
    private final String dbPath;

    /**
     * Constructs a TripService and connects to the SQLite database.
     * Creates the trips table if it does not exist.
     *
     * @param dbPath path to the SQLite database file
     * @throws SQLException if a database access error occurs
     */
    public TripService(String dbPath) throws SQLException {
        this.dbPath = dbPath;
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        createTableIfNotExists();
    }

    /**
     * Returns the database path.
     *
     * @return the SQLite database file path
     */
    public String getDbPath() {
        return dbPath;
    }

    /**
     * Creates the trips table if it does not exist.
     *
     * @throws SQLException if a database access error occurs
     */
    private void createTableIfNotExists() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS trips (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                destination TEXT NOT NULL,
                start_date TEXT NOT NULL,
                end_date TEXT NOT NULL,
                budget REAL NOT NULL,
                notes TEXT,
                completed INTEGER NOT NULL
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Creates a new trip and inserts it into the database.
     *
     * @param dest  destination of the trip
     * @param start start date
     * @param end   end date
     * @param budget budget for the trip
     * @param notes optional notes
     * @return the created Trip object
     * @throws SQLException if a database access error occurs
     */
    public Trip createTrip(String dest, LocalDate start, LocalDate end, double budget, String notes) throws SQLException {
        String sql = "INSERT INTO trips(destination, start_date, end_date, budget, notes, completed) VALUES (?, ?, ?, ?, ?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dest);
            ps.setString(2, start.toString());
            ps.setString(3, end.toString());
            ps.setDouble(4, budget);
            ps.setString(5, notes);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = rs.next() ? rs.getInt(1) : -1;
            return new Trip(id, dest, start, end, budget, notes, false);
        }
    }

    /**
     * Returns all trips in the database.
     *
     * @return list of all trips
     * @throws SQLException if a database access error occurs
     */
    public List<Trip> getAllTrips() throws SQLException {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trips.add(new Trip(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date")),
                        rs.getDouble("budget"),
                        rs.getString("notes"),
                        rs.getInt("completed") == 1
                ));
            }
        }
        return trips;
    }

    /**
     * Updates an existing trip in the database.
     *
     * @param trip trip object with updated values
     * @throws SQLException if a database access error occurs
     */
    public void updateTrip(Trip trip) throws SQLException {
        String sql = "UPDATE trips SET destination=?, start_date=?, end_date=?, budget=?, notes=?, completed=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trip.getDestination());
            ps.setString(2, trip.getStartDate().toString());
            ps.setString(3, trip.getEndDate().toString());
            ps.setDouble(4, trip.getBudget());
            ps.setString(5, trip.getNotes());
            ps.setInt(6, trip.isCompleted() ? 1 : 0);
            ps.setInt(7, trip.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a trip from the database by ID.
     *
     * @param id ID of the trip to delete
     * @throws SQLException if a database access error occurs
     */
    public void deleteTrip(int id) throws SQLException {
        String sql = "DELETE FROM trips WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Exports all trips to a text file in a pipe-delimited format.
     *
     * @param filename destination file path
     * @throws IOException  if an I/O error occurs
     * @throws SQLException if a database access error occurs
     */
    public void exportToFile(String filename) throws IOException, SQLException {
        List<Trip> trips = getAllTrips();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Trip t : trips) {
                writer.println(t.toExportString());
            }
        }
    }
}
