import domain.Appointment;
import domain.User;
import domain.Administrator;
import service.BookingService;
import service.rules.DurationRuleStrategy;
import service.rules.CapacityRuleStrategy;
import service.notifications.EmailNotifier;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("java:S106")
public class Main {

    // FIX #3: Extract duplicate literal into a constant
    private static final String DATE_SEPARATOR = " | Date: ";

    private static BookingService bookingService;
    private static Scanner scanner;
    private static User guest;
    private static Administrator systemAdmin;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        bookingService = new BookingService();

        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        bookingService.addObserver(new EmailNotifier());

        guest = new User("U1", "Guest User", "sweproj.test2026@gmail.com");

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String adminPass = dotenv.get("ADMIN_PASSWORD", "admin123");
        systemAdmin = new Administrator("A1", "System Admin", "admin@test.com", adminPass);

        User currentUser = guest;

        bookingService.addAvailableSlot(new Appointment("A1", "2023-12-01 10:00", 1, 5));
        bookingService.addAvailableSlot(new Appointment("A2", "2023-12-01 11:00", 1, 5));

        System.out.println("=== Welcome to the Appointment Scheduling System ===");

        // FIX #2: Boolean flag replaces while(true) + multiple breaks
        boolean running = true;
        while (running) {
            System.out.println("\n--- Current User: " + currentUser.getName() + " ---");
            printMenu(currentUser);

            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("Goodbye!");
                running = false;
            } else if (currentUser instanceof Administrator) {
                currentUser = handleAdminChoice(choice, (Administrator) currentUser);
            } else {
                currentUser = handleGuestChoice(choice, currentUser);
            }
        }

        scanner.close();
    }

    // FIX #1: Extracted from main() to reduce Cognitive Complexity
    private static void printMenu(User currentUser) {
        if (currentUser instanceof Administrator) {
            System.out.println("1. View ALL Appointments (Admin)");
            System.out.println("2. Add New Available Slot (Admin)");
            System.out.println("3. Delete Slot System-Wide (Admin)");
            System.out.println("4. Logout");
        } else {
            System.out.println("1. View Available Slots & Book");
            System.out.println("2. View My Bookings");
            System.out.println("3. Cancel My Booking");
            System.out.println("4. Admin Login");
        }
    }

    // FIX #1: Extracted guest actions from main()
    private static User handleGuestChoice(String choice, User currentUser) {
        if (choice.equals("1")) {
            showAndBookSlots(currentUser);
        } else if (choice.equals("2")) {
            showMyBookings(currentUser);
        } else if (choice.equals("3")) {
            cancelBooking(currentUser);
        } else if (choice.equals("4")) {
            return attemptAdminLogin();
        } else {
            System.out.println("Invalid choice.");
        }
        return currentUser;
    }

    // FIX #1: Extracted admin actions from main()
    private static User handleAdminChoice(String choice, Administrator admin) {
        if (choice.equals("1")) {
            viewAllAppointments();
        } else if (choice.equals("2")) {
            addNewSlot();
        } else if (choice.equals("3")) {
            deleteSlot();
        } else if (choice.equals("4")) {
            System.out.println("Logged out securely.");
            return guest;
        } else {
            System.out.println("Invalid choice.");
        }
        return admin;
    }

    private static void showAndBookSlots(User currentUser) {
        List<Appointment> available = bookingService.getAvailableAppointments();
        if (available.isEmpty()) {
            System.out.println("No slots currently available.");
        } else {
            System.out.println("--- Available Slots ---");
            for (Appointment a : available) {
                // FIX #3: Using constant instead of repeating the literal
                System.out.println("ID: " + a.getAppointmentId() + DATE_SEPARATOR + a.getDate() + " | Duration: " + a.getDurationInHours() + "h");
            }
            System.out.print("Enter ID to book (or press enter to cancel): ");
            String idToBook = scanner.nextLine();
            if (!idToBook.isEmpty()) {
                boolean success = bookingService.bookAppointment(idToBook, currentUser);
                System.out.println(success ? "SUCCESS! Appointment booked." : "FAILED! Slot taken or rule violated.");
            }
        }
    }

    private static void showMyBookings(User currentUser) {
        List<Appointment> myBookings = bookingService.getMyBookings(currentUser);
        System.out.println("--- My Bookings ---");
        if (myBookings.isEmpty()) {
            System.out.println("You have no bookings.");
        }
        for (Appointment a : myBookings) {
            // FIX #3: Using constant
            System.out.println("ID: " + a.getAppointmentId() + DATE_SEPARATOR + a.getDate());
        }
    }

    private static void cancelBooking(User currentUser) {
        System.out.print("Enter your Booking ID to cancel: ");
        String cancelId = scanner.nextLine();
        boolean success = bookingService.cancelUserAppointment(cancelId, currentUser);
        System.out.println(success ? "SUCCESS! Booking cancelled. It is now available for others." : "ERROR: Not found or not your booking.");
    }

    private static User attemptAdminLogin() {
        System.out.print("Enter Admin Password: ");
        String pass = scanner.nextLine();
        if (pass.equals(systemAdmin.getPassword())) {
            System.out.println("Login Success! Welcome Admin.");
            return systemAdmin;
        } else {
            System.out.println("Error: Invalid credentials.");
            return guest;
        }
    }

    private static void viewAllAppointments() {
        List<Appointment> all = bookingService.getAllAppointments();
        System.out.println("--- ALL System Appointments ---");
        for (Appointment a : all) {
            // FIX #3: Using constant
            System.out.println("ID: " + a.getAppointmentId() + DATE_SEPARATOR + a.getDate() + " | Status: " + a.getStatus());
        }
    }

    private static void addNewSlot() {
        System.out.print("Enter New Appt ID (e.g., A3): ");
        String id = scanner.nextLine();
        System.out.print("Enter Date (e.g., 2023-12-01 10:00): ");
        String date = scanner.nextLine();
        System.out.print("Enter Duration (hrs): ");
        try {
            int duration = Integer.parseInt(scanner.nextLine());
            bookingService.addAvailableSlot(new Appointment(id, date, duration, 5));
            System.out.println("Admin Success: Slot added to system.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid duration input.");
        }
    }

    private static void deleteSlot() {
        System.out.print("Enter Appt ID to Delete System-Wide: ");
        String deleteId = scanner.nextLine();
        boolean removed = bookingService.adminDeleteSlot(deleteId);
        System.out.println(removed ? "Admin Action: Slot permanently deleted." : "Admin Error: Slot not found.");
    }
}
