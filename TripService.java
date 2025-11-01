import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Name: Todd Uhl
 * Course: 202610 Software Development I CEN-3024C-14877
 * Date: 10/31/2025
 *
 * Class: TripService
 * This class manages a collection of Trip objects, providing functionality to add, find, delete,
 * import, and sort trips. It acts as the business logic layer between the MainApp and the Trip model.
 */

public class TripService {
    private final Connection conn;
    private final String dbPath;

    public TripService(String dbPath) throws SQLException {
        this.dbPath = dbPath;
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        createTableIfNotExists();
    }

    public String getDbPath() {
        return dbPath;
    }



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

    public List<Trip> getAllTrips() throws SQLException {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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

    public void deleteTrip(int id) throws SQLException {
        String sql = "DELETE FROM trips WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void exportToFile(String filename) throws IOException, SQLException {
        List<Trip> trips = getAllTrips();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Trip t : trips) {
                writer.println(t.toExportString());
            }
        }
    }
}
