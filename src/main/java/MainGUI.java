import domain.Appointment;
import domain.User;
import domain.Administrator;
import service.BookingService;
import service.rules.DurationRuleStrategy;
import service.rules.CapacityRuleStrategy;
import service.notifications.EmailNotifier;
import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainGUI {

    private final BookingService bookingService;
    private final User guestUser;
    private User currentUser;
    private final Administrator systemAdmin;

    public MainGUI() {
        bookingService = new BookingService();
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        bookingService.addObserver(new EmailNotifier());

        guestUser = new User("U1", "Guest User", "mayshamayel78@gmail.com"); // Put your email here!
        
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String adminPass = dotenv.get("ADMIN_PASSWORD", "admin123"); 
        systemAdmin = new Administrator("A1", "System Admin", "admin@test.com", adminPass);
        
        currentUser = guestUser; 
        
        // Let's pre-load some available slots so the user has something to pick!
        bookingService.addAvailableSlot(new Appointment("A1", "2023-12-01 10:00", 1, 5));
        bookingService.addAvailableSlot(new Appointment("A2", "2023-12-01 11:00", 1, 5));
    }

    // --- MAIN GUEST WINDOW ---
    public void start() {
        JFrame frame = new JFrame("Appointment Scheduling System");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(450, 400); 
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create Dropdown for available appointments
        JComboBox<String> availableDropdown = new JComboBox<>();
        updateDropdown(availableDropdown); // Fill it with data

        JTextField cancelField = new JTextField(10);
        JTextArea consoleOutput = new JTextArea(8, 35);
        consoleOutput.setEditable(false);
        consoleOutput.setText("Welcome! Select an available slot to book.");

        JButton bookBtn = new JButton("Book Selected Slot");
        JButton viewMyBtn = new JButton("View My Bookings");
        JButton cancelUserBtn = new JButton("Cancel My Booking");
        JButton loginBtn = new JButton("Admin Login");

        // USER: Book Appointment
        bookBtn.addActionListener(e -> {
            String selected = (String) availableDropdown.getSelectedItem();
            if (selected != null) {
                String apptId = selected.split(" ")[0]; // Extract ID from "A1 - 2023-12-01..."
                
                boolean success = bookingService.bookAppointment(apptId, currentUser);
                if (success) {
                    consoleOutput.setText("SUCCESS!\nYou booked appointment " + apptId + ".");
                    updateDropdown(availableDropdown); // Refresh list
                } else {
                    consoleOutput.setText("FAILED!\nSlot is no longer available or rule violated.");
                }
            }
        });

        // USER: View My Bookings
        viewMyBtn.addActionListener(e -> {
            List<Appointment> myBookings = bookingService.getMyBookings(currentUser);
            StringBuilder sb = new StringBuilder("Your Bookings:\n");
            for(Appointment a : myBookings) {
                sb.append(a.getAppointmentId()).append(" at ").append(a.getDate()).append("\n");
            }
            if(myBookings.isEmpty()) sb.append("You have no bookings.");
            consoleOutput.setText(sb.toString());
        });

        // USER: Cancel Booking
        cancelUserBtn.addActionListener(e -> {
            String idToCancel = cancelField.getText().trim();
            if (bookingService.cancelUserAppointment(idToCancel, currentUser)) {
                consoleOutput.setText("SUCCESS: Your booking " + idToCancel + " was cancelled.\nIt is now available for others.");
                updateDropdown(availableDropdown);
            } else {
                consoleOutput.setText("ERROR: Could not cancel. Did you book this ID?");
            }
        });

        // ADMIN LOGIN
        loginBtn.addActionListener(e -> {
            String pass = JOptionPane.showInputDialog(frame, "Enter Admin Password:");
            if (pass != null && pass.equals(systemAdmin.getPassword())) {
                currentUser = systemAdmin;
                openAdminDashboard(frame, availableDropdown); 
            } else {
                consoleOutput.setText("Admin Login Failed: Invalid Credentials.");
            }
        });

        frame.add(new JLabel("Available Slots:"));
        frame.add(availableDropdown);
        frame.add(bookBtn);
        
        frame.add(new JSeparator(SwingConstants.HORIZONTAL));
        frame.add(new JLabel("Cancel your booking ID:"));
        frame.add(cancelField);
        frame.add(cancelUserBtn);
        frame.add(viewMyBtn);
        
        frame.add(new JSeparator(SwingConstants.HORIZONTAL));
        frame.add(loginBtn);
        frame.add(new JScrollPane(consoleOutput));

        frame.setVisible(true);
    }

    private void updateDropdown(JComboBox<String> dropdown) {
        dropdown.removeAllItems();
        for (Appointment a : bookingService.getAvailableAppointments()) {
            dropdown.addItem(a.getAppointmentId() + " - " + a.getDate() + " (" + a.getDurationInHours() + "h)");
        }
    }

    // --- ADVANCED ADMIN DASHBOARD ---
    private void openAdminDashboard(JFrame parentFrame, JComboBox<String> userDropdown) {
        parentFrame.setVisible(false);

        JFrame adminFrame = new JFrame("Administrator Dashboard");
        adminFrame.setSize(600, 500);
        adminFrame.setLayout(new BorderLayout(10, 10));

        String[] columns = {"ID", "Date", "Duration", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable appointmentTable = new JTable(tableModel);
        
        Runnable refreshTable = () -> {
            tableModel.setRowCount(0);
            for (Appointment a : bookingService.getAllAppointments()) {
                tableModel.addRow(new Object[]{a.getAppointmentId(), a.getDate(), a.getDurationInHours(), a.getStatus()});
            }
        };
        refreshTable.run();

        JPanel topPanel = new JPanel();
        JButton deleteBtn = new JButton("Delete Slot (System Wide)");
        JButton logoutBtn = new JButton("Logout Admin");
        topPanel.add(deleteBtn);
        topPanel.add(logoutBtn);

        deleteBtn.addActionListener(e -> {
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                String cancelId = tableModel.getValueAt(selectedRow, 0).toString();
                bookingService.adminDeleteSlot(cancelId);
                refreshTable.run();
                updateDropdown(userDropdown); // Update user view behind the scenes
            }
        });

        logoutBtn.addActionListener(e -> {
            currentUser = guestUser;
            adminFrame.dispose(); 
            parentFrame.setVisible(true); 
        });

        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Add Available Slot (Admin)"));
        
        JTextField newId = new JTextField();
        JTextField newDate = new JTextField();
        JTextField newDuration = new JTextField();
        JButton addBtn = new JButton("Add Slot");

        bottomPanel.add(new JLabel("ID:"));
        bottomPanel.add(new JLabel("Date:"));
        bottomPanel.add(new JLabel("Duration:"));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(newId);
        bottomPanel.add(newDate);
        bottomPanel.add(newDuration);
        bottomPanel.add(addBtn);

        addBtn.addActionListener(e -> {
            try {
                String id = newId.getText();
                String date = newDate.getText();
                int duration = Integer.parseInt(newDuration.getText());

                bookingService.addAvailableSlot(new Appointment(id, date, duration, 5));
                refreshTable.run();
                updateDropdown(userDropdown); // Update user view behind the scenes
                newId.setText(""); newDate.setText(""); newDuration.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(adminFrame, "Invalid duration.");
            }
        });

        adminFrame.add(topPanel, BorderLayout.NORTH);
        adminFrame.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);
        adminFrame.add(bottomPanel, BorderLayout.SOUTH);
        adminFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI myApp = new MainGUI();
            myApp.start();
        });
    }
}