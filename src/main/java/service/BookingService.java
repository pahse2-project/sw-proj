package service;

import domain.Appointment;
import domain.User;
import service.notifications.Observer;
import service.rules.BookingRuleStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for coordinating appointment bookings.
 * Evaluates business rules using the Strategy Pattern before confirming.
 * Uses Observer pattern to send notifications.
 * @author [Your Name]
 * @version 1.0
 */
public class BookingService {
    
    private List<Appointment> savedAppointments;
    private List<BookingRuleStrategy> bookingRules;
    private List<Observer> notificationObservers;

    /**
     * Initializes the BookingService with an empty database and rule list.
     */
    public BookingService() {
        this.savedAppointments = new ArrayList<>();
        this.bookingRules = new ArrayList<>();
        this.notificationObservers = new ArrayList<>();
    }

    /**
     * Adds a business rule strategy to the service.
     * @param rule the booking rule to enforce
     */
    public void addRule(BookingRuleStrategy rule) {
        this.bookingRules.add(rule);
    }

    /**
     * Adds an observer to the notification list.
     * @param observer the notifier to add
     */
    public void addObserver(Observer observer) {
        this.notificationObservers.add(observer);
    }
    
    
    /**
     * Attempts to book an appointment by validating it against all business rules.
     * Fulfills US2.1, US2.2, and US2.3.
     * @param appointment the appointment to book
     * @return true if successfully booked and confirmed, false otherwise
     */
    public boolean bookAppointment(Appointment appointment, User user) {
        // Enforce all rules (Strategy Pattern)
        for (BookingRuleStrategy rule : bookingRules) {
            if (!rule.isValid(appointment)) {
                System.out.println("Booking failed for Appointment ID: " + appointment.getAppointmentId());
                return false;
            }
        }
        
        // If all rules pass, save and confirm (US2.1)
        appointment.setStatus("Confirmed");
        savedAppointments.add(appointment);
        System.out.println("Booking successful! Appointment " + appointment.getAppointmentId() + " is Confirmed.");
        
        String reminderMessage = "Reminder: Your appointment " + appointment.getAppointmentId() + " is confirmed.";
        for (Observer observer : notificationObservers) {
            observer.notify(user, reminderMessage);
        }
        
        return true;
    }
    
    
    
    /**
     * Cancels an existing appointment.
     * Fulfills Sprint 4 cancellation requirements.
     * @param appointmentId the ID of the appointment to cancel
     * @param user the user requesting the cancellation
     * @return true if successfully canceled, false if not found
     */
    public boolean cancelAppointment(String appointmentId, User user) {
        for (Appointment appointment : savedAppointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointment.setStatus("Canceled");
                System.out.println("Appointment " + appointmentId + " has been canceled.");
                
                // Trigger Notifications for the cancellation
                String cancelMessage = "Notice: Your appointment " + appointmentId + " has been canceled.";
                for (Observer observer : notificationObservers) {
                    observer.notify(user, cancelMessage);
                }
                return true;
            }
        }
        
        System.out.println("Error: Appointment " + appointmentId + " not found.");
        return false;
    }
    
    

    /**
     * Retrieves all saved appointments.
     * @return list of confirmed appointments
     */
    public List<Appointment> getSavedAppointments() {
        return new ArrayList<>(savedAppointments);
    }
}