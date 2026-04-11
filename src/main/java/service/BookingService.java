package service;

import domain.Appointment;
import domain.User;
import service.rules.BookingRuleStrategy;
import service.notifications.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingService {

    private List<Appointment> savedAppointments;
    private List<BookingRuleStrategy> rules;
    private List<Observer> observers;
    
    // Tracks which user booked which appointment
    private Map<String, User> userBookings;

    public BookingService() {
        this.savedAppointments = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.userBookings = new HashMap<>();
    }

    public void addRule(BookingRuleStrategy rule) { rules.add(rule); }
    public void addObserver(Observer observer) { observers.add(observer); }

    // --- ADMIN METHODS ---
    public boolean addAvailableSlot(Appointment appt) {
        appt.setStatus("Available"); // Set default status
        return savedAppointments.add(appt);
    }

    public boolean adminDeleteSlot(String apptId) {
        userBookings.remove(apptId);
        return savedAppointments.removeIf(a -> a.getAppointmentId().equals(apptId));
    }

    // --- USER METHODS (WITH CONCURRENCY FIX) ---
    
    // The 'synchronized' keyword fixes the conflict! Only one thread can enter at a time.
    public synchronized boolean bookAppointment(String apptId, User user) {
        for (Appointment a : savedAppointments) {
            if (a.getAppointmentId().equals(apptId)) {
                // Check if it's actually available
                if (!"Available".equals(a.getStatus())) {
                    return false; // Someone else beat them to it!
                }
                
                // Validate Business Rules
                for (BookingRuleStrategy rule : rules) {
                    if (!rule.isValid(a)) return false;
                }

                // Confirm booking
                a.setStatus("Confirmed");
                userBookings.put(apptId, user);
                notifyObservers(user, "Your appointment " + apptId + " is Confirmed.");
                return true;
            }
        }
        return false;
    }

    public boolean cancelUserAppointment(String apptId, User user) {
        // Ensure the user actually owns this booking
        if (user.equals(userBookings.get(apptId))) {
            for (Appointment a : savedAppointments) {
                if (a.getAppointmentId().equals(apptId)) {
                    a.setStatus("Available"); // Make it available for others again
                    userBookings.remove(apptId);
                    notifyObservers(user, "Your appointment " + apptId + " was Cancelled.");
                    return true;
                }
            }
        }
        return false;
    }

    // --- GETTERS ---
    public List<Appointment> getAvailableAppointments() {
        List<Appointment> available = new ArrayList<>();
        for (Appointment a : savedAppointments) {
            if ("Available".equals(a.getStatus())) {
                available.add(a);
            }
        }
        return available;
    }

    public List<Appointment> getMyBookings(User user) {
        List<Appointment> mine = new ArrayList<>();
        for (Appointment a : savedAppointments) {
            if (user.equals(userBookings.get(a.getAppointmentId()))) {
                mine.add(a);
            }
        }
        return mine;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(savedAppointments); // Return a copy for UI
    }

    private void notifyObservers(User user, String message) {
        for (Observer obs : observers) {
            obs.notify(user, message);
        }
    }
 // Add this to the USER METHODS section in BookingService.java
    public boolean modifyAppointment(String apptId, String newDate, User user) {
        if (user.equals(userBookings.get(apptId))) {
            for (Appointment a : savedAppointments) {
                if (a.getAppointmentId().equals(apptId)) {
                    a.setDate(newDate);
                    notifyObservers(user, "Your appointment " + apptId + " was Updated.");
                    return true;
                }
            }
        }
        return false;
    }

    // Add this to the ADMIN METHODS section in BookingService.java
    public boolean adminModifyAppointment(String apptId, String newDate, domain.Administrator admin) {
        for (Appointment a : savedAppointments) {
            if (a.getAppointmentId().equals(apptId)) {
                a.setDate(newDate);
                return true;
            }
        }
        return false;
    }
}