import domain.Appointment;
import domain.User;
import service.BookingService;
import service.rules.DurationRuleStrategy;
import service.rules.CapacityRuleStrategy;
import service.notifications.EmailNotifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    
    private BookingService bookingService;
    private User currentUser;

    public MainGUI() {
        // 1. Turn on the engine and add the rules & observers!
        bookingService = new BookingService();
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        bookingService.addObserver(new EmailNotifier());
        
        // Dummy user for testing
        currentUser = new User("U1", "Test User", "mayshamayel78@gmail.com");
    }

    public void start() {
        // 2. Build the Window (JFrame)
        JFrame frame = new JFrame("Appointment Scheduling System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 400);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // 3. Create Text Boxes and Labels
        JTextField idField = new JTextField(15);
        JTextField dateField = new JTextField(15);
        JTextField durationField = new JTextField(15);
        
        JTextArea consoleOutput = new JTextArea(8, 25);
        consoleOutput.setEditable(false);
        consoleOutput.setText("Welcome! Enter details to book.");

        // 4. Create Buttons
        JButton bookBtn = new JButton("Book Appointment");
        JButton viewBtn = new JButton("View Total Bookings");

        // 5. Tell the "Book" button what to do when clicked
        bookBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = idField.getText();
                    String date = dateField.getText();
                    int duration = Integer.parseInt(durationField.getText());

                    // Create appointment and send it to the gauntlet!
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
            }
        });

        // 6. Tell the "View" button what to do
        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int total = bookingService.getSavedAppointments().size();
                consoleOutput.setText("System currently has " + total + " confirmed appointment(s).");
            }
        });

        // 7. Add everything to the window
        frame.add(new JLabel("Appointment ID (e.g., A1):"));
        frame.add(idField);
        frame.add(new JLabel("Date (e.g., 2023-12-01 10:00):"));
        frame.add(dateField);
        frame.add(new JLabel("Duration in Hours (e.g., 1):"));
        frame.add(durationField);
        frame.add(bookBtn);
        frame.add(viewBtn);
        frame.add(new JScrollPane(consoleOutput));

        // 8. Make it visible!
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // This is the starting point of the app
        MainGUI myApp = new MainGUI();
        myApp.start();
    }
}