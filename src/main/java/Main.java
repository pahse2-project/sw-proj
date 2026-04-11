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

@SuppressWarnings("java:S106") // SonarCloud: Safe to ignore System.out in this specific CLI class
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookingService bookingService = new BookingService();
        
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        bookingService.addObserver(new EmailNotifier());
        
        // Put your real email here for testing!
        User guest = new User("U1", "Guest User", "sweproj.test2026@gmail.com");
        
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String adminPass = dotenv.get("ADMIN_PASSWORD", "admin123");
        Administrator systemAdmin = new Administrator("A1", "System Admin", "admin@test.com", adminPass);
        
        User currentUser = guest; 

        // Pre-load some available slots so the user has something to pick
        bookingService.addAvailableSlot(new Appointment("A1", "2023-12-01 10:00", 1, 5));
        bookingService.addAvailableSlot(new Appointment("A2", "2023-12-01 11:00", 1, 5));

        System.out.println("=== Welcome to the Appointment Scheduling System ===");
        while(true) {
            System.out.println("\n--- Current User: " + currentUser.getName() + " ---");
            
            if (currentUser instanceof Administrator) {
                // ADMIN MENU
                System.out.println("1. View ALL Appointments (Admin)");
                System.out.println("2. Add New Available Slot (Admin)");
                System.out.println("3. Delete Slot System-Wide (Admin)");
                System.out.println("4. Logout");
            } else {
                // GUEST MENU
                System.out.println("1. View Available Slots & Book");
                System.out.println("2. View My Bookings");
                System.out.println("3. Cancel My Booking");
                System.out.println("4. Admin Login");
            }
            
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            // --- GUEST ACTIONS ---
            if (!(currentUser instanceof Administrator)) {
                if (choice.equals("1")) {
                    List<Appointment> available = bookingService.getAvailableAppointments();
                    if (available.isEmpty()) {
                        System.out.println("No slots currently available.");
                    } else {
                        System.out.println("--- Available Slots ---");
                        for (Appointment a : available) {
                            System.out.println("ID: " + a.getAppointmentId() + " | Date: " + a.getDate() + " | Duration: " + a.getDurationInHours() + "h");
                        }
                        System.out.print("Enter ID to book (or press enter to cancel): ");
                        String idToBook = scanner.nextLine();
                        if (!idToBook.isEmpty()) {
                            boolean success = bookingService.bookAppointment(idToBook, currentUser);
                            System.out.println(success ? "SUCCESS! Appointment booked." : "FAILED! Slot taken or rule violated.");
                        }
                    }
                } else if (choice.equals("2")) {
                    List<Appointment> myBookings = bookingService.getMyBookings(currentUser);
                    System.out.println("--- My Bookings ---");
                    if (myBookings.isEmpty()) System.out.println("You have no bookings.");
                    for (Appointment a : myBookings) {
                        System.out.println("ID: " + a.getAppointmentId() + " | Date: " + a.getDate());
                    }
                } else if (choice.equals("3")) {
                    System.out.print("Enter your Booking ID to cancel: ");
                    String cancelId = scanner.nextLine();
                    boolean success = bookingService.cancelUserAppointment(cancelId, currentUser);
                    System.out.println(success ? "SUCCESS! Booking cancelled. It is now available for others." : "ERROR: Not found or not your booking.");
                } else if (choice.equals("4")) {
                    System.out.print("Enter Admin Password: ");
                    String pass = scanner.nextLine();
                    if (pass.equals(systemAdmin.getPassword())) {
                        currentUser = systemAdmin;
                        System.out.println("Login Success! Welcome Admin.");
                    } else {
                        System.out.println("Error: Invalid credentials.");
                    }
                } else if (choice.equals("0")) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice.");
                }
            } 
            // --- ADMIN ACTIONS ---
            else {
                if (choice.equals("1")) {
                    List<Appointment> all = bookingService.getAllAppointments();
                    System.out.println("--- ALL System Appointments ---");
                    for (Appointment a : all) {
                        System.out.println("ID: " + a.getAppointmentId() + " | Date: " + a.getDate() + " | Status: " + a.getStatus());
                    }
                } else if (choice.equals("2")) {
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
                } else if (choice.equals("3")) {
                    System.out.print("Enter Appt ID to Delete System-Wide: ");
                    String deleteId = scanner.nextLine();
                    boolean removed = bookingService.adminDeleteSlot(deleteId);
                    System.out.println(removed ? "Admin Action: Slot permanently deleted." : "Admin Error: Slot not found.");
                } else if (choice.equals("4")) {
                    currentUser = guest;
                    System.out.println("Logged out securely.");
                } else if (choice.equals("0")) {
                    System.out.println("Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        }
        scanner.close();
    }
}