import domain.Appointment;
import domain.User;
import service.BookingService;
import service.rules.DurationRuleStrategy;
import service.rules.CapacityRuleStrategy;
import service.notifications.EmailNotifier;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookingService bookingService = new BookingService();
        
        // Setup engine
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        bookingService.addObserver(new EmailNotifier());
        
        User guest = new User("U1", "Guest User", "guest@test.com");

        System.out.println("=== Welcome to the Appointment Scheduling System ===");
        while(true) {
            System.out.println("\n1. Book Appointment\n2. View Appointments\n3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter Appt ID (e.g., A1): ");
                String id = scanner.nextLine();
                System.out.print("Enter Date (e.g., 2023-12-01 10:00): ");
                String date = scanner.nextLine();
                System.out.print("Enter Duration in hours: ");
                int duration = Integer.parseInt(scanner.nextLine());
                
                Appointment appt = new Appointment(id, date, duration, 5);
                bookingService.bookAppointment(appt, guest);
            } else if (choice.equals("2")) {
                System.out.println("Saved Appointments: " + bookingService.getSavedAppointments().size());
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                break;
            }
        }
        scanner.close();
    }
}