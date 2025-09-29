import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/*
 * Name: Todd Uhl
 * Course: 202610 Software Development I CEN-3024C-14877
 * Date: 9/29/2025
 *
 * Class: MainApp
 * This is the main driver class for the Travel Tracker program.
 * It handles all user interaction, displays menus, calls TripService methods,
 * and manages importing and exporting trips to files.
 *
 * This program allows users to create, view, update, delete, and manage their trips.
 * It also supports marking trips as complete, sorting by various criteria, and importing
 * and exporting trip data.
 */
public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TripService tripService = new TripService();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /*
     * Method: main
     * Purpose: Entry point of the program, displays the main menu and handles user selections in a loop.
     * Arguments:
     *  - args (String[]): Command-line arguments (not used)
     * Return: void
     */
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Travel Tracker ---");
            System.out.println("1. Add Trip");
            System.out.println("2. View Trips");
            System.out.println("3. Update Trip");
            System.out.println("4. Delete Trip");
            System.out.println("5. Mark Trip Complete");
            System.out.println("6. Sorting Options");
            System.out.println("7. Import/Export List");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addTrip();
                case "2" -> viewTrips(tripService.getTrips());
                case "3" -> updateTrip();
                case "4" -> deleteTrip();
                case "5" -> toggleTripCompletion();
                case "6" -> sortMenu();
                case "7" -> fileMenu();
                case "8" -> { System.out.println("Thank You for Using Travel Tracker"); return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /*
     * Method: addTrip
     * Purpose: Prompts the user for trip details creates a new trip object and displays confirmation.
     */
    private static void addTrip() {
        System.out.println("\n--- Add Trip ---");
        System.out.print("Destination: ");
        String destination = scanner.nextLine();

        LocalDate startDate = askForDate("Start Date (mm/dd/yyyy): ");
        LocalDate endDate = askForDate("End Date (mm/dd/yyyy): ");

        System.out.print("Budget: $");
        double budget = Double.parseDouble(scanner.nextLine());

        System.out.print("Notes: ");
        String notes = scanner.nextLine();

        Trip trip = tripService.createTrip(destination, startDate, endDate, budget, notes);
        System.out.println("Trip added: " + trip);
    }

    /*
     * Method: viewTrips
     * Purpose: Displays all trips or a provided sorted list of trips.
     * Arguments:
     *  - trips (List<Trip>): List of trips to display
     * Return: void
     */
    private static void viewTrips(List<Trip> trips) {
        if (trips.isEmpty()) {
            System.out.println("No trips found.");
            return;
        }
        trips.forEach(System.out::println);
    }

    /*
     * Method: updateTrip
     * Purpose: Allows user to update details of an existing trip by its ID.
     */
    private static void updateTrip() {
        System.out.print("Enter Trip ID to update (or 0000 to cancel): ");
        String input = scanner.nextLine();
        if (input.equals("0000")) return;

        int id = Integer.parseInt(input);
        Trip trip = tripService.findTrip(id);
        if (trip == null) {
            System.out.println("Trip not found.");
            return;
        }

        System.out.println("Updating Trip: " + trip);
        System.out.println("Type 0000 at any field to cancel update.");

        System.out.print("New Destination: ");
        String destination = scanner.nextLine();
        if (destination.equals("0000")) return;
        trip.setDestination(destination);

        trip.setStartDate(askForDate("New Start Date (mm/dd/yyyy): "));
        trip.setEndDate(askForDate("New End Date (mm/dd/yyyy): "));

        System.out.print("New Budget: ");
        String budgetInput = scanner.nextLine();
        if (budgetInput.equals("0000")) return;
        trip.setBudget(Double.parseDouble(budgetInput));

        System.out.print("New Notes: ");
        String notes = scanner.nextLine();
        if (notes.equals("0000")) return;
        trip.setNotes(notes);

        System.out.println("Trip updated: " + trip);
    }

    /*
     * Method: deleteTrip
     * Purpose: Deletes a trip based on the entered ID.
     */
    private static void deleteTrip() {
        System.out.print("Enter Trip ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (tripService.deleteTrip(id)) {
            System.out.println("Trip deleted.");
        } else {
            System.out.println("Trip not found.");
        }
    }

    /*
     * Method: toggleTripCompletion
     * Purpose: Toggles the completion status of a trip.
     */
    private static void toggleTripCompletion() {
        System.out.print("Enter Trip ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Trip trip = tripService.findTrip(id);
        if (trip == null) {
            System.out.println("Trip not found.");
            return;
        }
        trip.setCompleted(!trip.isCompleted());
        System.out.println("Trip marked as " + (trip.isCompleted() ? "completed." : "planning."));
    }

    /*
     * Method: sortMenu
     * Purpose: Displays sort options and shows trips in the chosen order.
     */
    private static void sortMenu() {
        System.out.println("\n--- Sorting Options ---");
        System.out.println("1. By Trip ID");
        System.out.println("2. By Start Date");
        System.out.println("3. By Destination");
        System.out.println("4. By Budget Low to High");
        System.out.println("5. By Budget High to Low");
        System.out.print("Choose sort: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> viewTrips(tripService.sortById());
            case "2" -> viewTrips(tripService.sortByStartDate());
            case "3" -> viewTrips(tripService.sortByDestination());
            case "4" -> viewTrips(tripService.sortByBudgetLowToHigh());
            case "5" -> viewTrips(tripService.sortByBudgetHighToLow());
            default -> System.out.println("Invalid choice.");
        }
    }

    /*
     * Method: fileMenu
     * Purpose: Handles import/export options and prompts for filename.
     */
    private static void fileMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Export Trips to File");
        System.out.println("2. Import Trips from File");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();

        if (choice.equals("1")) exportTrips(filename);
        else if (choice.equals("2")) importTrips(filename);
        else System.out.println("Invalid choice.");
    }

    /*
     * Method: exportTrips
     * Purpose: Writes all trips to a specified file in text format .txt
     * Arguments:
     *  - filename (String): Name of file to save to
     * Return: void
     */
    private static void exportTrips(String filename) {
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Trip trip : tripService.getTrips()) {
                writer.println(trip.toExportString());
            }
            System.out.println("Trips exported to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting trips: " + e.getMessage());
        }
    }

    /*
     * Method: importTrips
     * Purpose: Reads a file of trips, parses them, and adds them to the trip list.
     * Arguments:
     *  - filename (String): Name of file to read from
     * Return: void
     */
    private static void importTrips(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length != 7) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                try {
                    int tripNumber = Integer.parseInt(parts[0]);
                    String destination = parts[1];
                    LocalDate startDate = LocalDate.parse(parts[2], formatter);
                    LocalDate endDate = LocalDate.parse(parts[3], formatter);
                    double budget = Double.parseDouble(parts[4]);
                    String notes = parts[5];
                    boolean completed = Boolean.parseBoolean(parts[6]);

                    Trip trip = new Trip(tripNumber, destination, startDate, endDate, budget, notes);
                    trip.setCompleted(completed);
                    tripService.addImportedTrip(trip);

                } catch (Exception e) {
                    System.out.println("Skipping line due to data format error: " + line);
                }
            }
            System.out.println("Trips imported successfully from " + filename);
        } catch (IOException e) {
            System.out.println("Error importing trips: " + e.getMessage());
        }
    }

    /*
     * Method: askForDate
     * Purpose: Prompts the user for a date and validates format before returning it.
     * Arguments:
     *  - prompt (String): Text to display to user
     * Return: LocalDate - parsed date entered by user
     */
    private static LocalDate askForDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use mm/dd/yyyy.");
            }
        }
    }
}
