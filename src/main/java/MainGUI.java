import domain.Appointment;
import domain.User;
import service.BookingService;
import service.rules.DurationRuleStrategy;
import service.rules.CapacityRuleStrategy;
import service.notifications.EmailNotifier;

import javax.swing.*;
import java.awt.*;

public class MainGUI {

    private BookingService bookingService;
    private User currentUser;

    public MainGUI() {
        bookingService = new BookingService();
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        bookingService.addObserver(new EmailNotifier());

        currentUser = new User("U1", "Test User", "mayshamayel78@gmail.com");
    }

    public void start() {
        JFrame frame = new JFrame("Appointment Scheduling System");
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(350, 400);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JTextField idField = new JTextField(15);
        JTextField dateField = new JTextField(15);
        JTextField durationField = new JTextField(15);

        JTextArea consoleOutput = new JTextArea(8, 25);
        consoleOutput.setEditable(false);
        consoleOutput.setText("Welcome! Enter details to book.");

        JButton bookBtn = new JButton("Book Appointment");
        JButton viewBtn = new JButton("View Total Bookings");

        // Lambda instead of anonymous inner class (ln 50)
        bookBtn.addActionListener(e -> {
            try {
                String id = idField.getText();
                String date = dateField.getText();
                int duration = Integer.parseInt(durationField.getText());

                Appointment appt = new Appointment(id, date, duration, 5);
                boolean success = bookingService.bookAppointment(appt, currentUser);

                if (success) {
                    consoleOutput.setText("SUCCESS!\nAppointment " + id + " is Confirmed.\n(Check Eclipse console for email notification)");
                } else {
                    consoleOutput.setText("FAILED!\nA business rule was violated.\n(e.g., Duration too long)");
                }
            } catch (Exception ex) {
                consoleOutput.setText("ERROR: Please enter valid numbers\nfor the duration!");
            }
        });

        // Lambda instead of anonymous inner class (ln 73)
        viewBtn.addActionListener(e -> {
            int total = bookingService.getSavedAppointments().size();
            consoleOutput.setText("System currently has " + total + " confirmed appointment(s).");
        });

        frame.add(new JLabel("Appointment ID (e.g., A1):"));
        frame.add(idField);
        frame.add(new JLabel("Date (e.g., 2023-12-01 10:00):"));
        frame.add(dateField);
        frame.add(new JLabel("Duration in Hours (e.g., 1):"));
        frame.add(durationField);
        frame.add(bookBtn);
        frame.add(viewBtn);
        frame.add(new JScrollPane(consoleOutput));

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        MainGUI myApp = new MainGUI();
        myApp.start();
    }
}