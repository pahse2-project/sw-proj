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
 * * @author [Your Name]
 * @version 2.0
 */
public class BookingService {
    
    /** List of successfully confirmed and saved appointments. */
    private List<Appointment> savedAppointments;
    
    /** List of business rules to enforce via Strategy Pattern. */
    private List<BookingRuleStrategy> bookingRules;
    
    /** List of observers to notify via Observer Pattern. */
    private List<Observer> notificationObservers;

    /**
     * Initializes the BookingService with empty databases and rule lists.
     */
    public BookingService() {
        this.savedAppointments = new ArrayList<>();
        this.bookingRules = new ArrayList<>();
        this.notificationObservers = new ArrayList<>();
    }

    /**
     * Adds a business rule strategy to the service.
     * * @param rule the booking rule to enforce
     */
    public void addRule(BookingRuleStrategy rule) {
        this.bookingRules.add(rule);
    }

    /**
     * Adds an observer to the notification list.
     * * @param observer the notifier to add
     */
    public void addObserver(Observer observer) {
        this.notificationObservers.add(observer);
    }
    
    /**
     * Attempts to book an appointment by validating it against all business rules.
     * * @param appointment the appointment to book
     * @param user the user requesting the booking
     * @return true if successfully booked and confirmed, false otherwise
     */
    public boolean bookAppointment(Appointment appointment, User user) {
        for (BookingRuleStrategy rule : bookingRules) {
            if (!rule.isValid(appointment)) {
                System.out.println("Booking failed for Appointment ID: " + appointment.getAppointmentId());
                return false;
            }
        }
        
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
     * * @param appointmentId the ID of the appointment to cancel
     * @param user the user requesting the cancellation
     * @return true if successfully canceled, false if not found
     */
    public boolean cancelAppointment(String appointmentId, User user) {
        for (Appointment appointment : savedAppointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointment.setStatus("Canceled");
                System.out.println("Appointment " + appointmentId + " has been canceled.");
                
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
     * Modifies the date of an existing appointment.
     * * @param appointmentId the ID of the appointment to modify
     * @param newDate the new date and time
     * @param user the user requesting the modification
     * @return true if successfully modified, false otherwise
     */
    public boolean modifyAppointment(String appointmentId, String newDate, User user) {
        for (Appointment appointment : savedAppointments) {
            if (appointment.getAppointmentId().equals(appointmentId) && !appointment.getStatus().equals("Canceled")) {
                appointment.setDate(newDate); 
                System.out.println("Appointment " + appointmentId + " has been modified to " + newDate);
                
                String updateMessage = "Update: Your appointment " + appointmentId + " is now scheduled for " + newDate;
                for (Observer observer : notificationObservers) {
                    observer.notify(user, updateMessage);
                }
                return true;
            }
        }
        System.out.println("Error: Cannot modify. Appointment " + appointmentId + " not found or is canceled.");
        return false;
    }

    /**
     * Administrator override to cancel any appointment.
     * * @param appointmentId the ID of the appointment to cancel
     * @param admin the administrator performing the override
     * @return true if canceled, false if not found
     */
    public boolean adminCancelAppointment(String appointmentId, domain.Administrator admin) {
        System.out.println("Admin Override: " + admin.getName() + " is forcing cancellation of " + appointmentId);
        return cancelAppointment(appointmentId, admin);
    }
    
    /**
     * Administrator override to modify any appointment.
     * * @param appointmentId the ID of the appointment to modify
     * @param newDate the new date and time
     * @param admin the administrator performing the override
     * @return true if modified, false if not found
     */
    public boolean adminModifyAppointment(String appointmentId, String newDate, domain.Administrator admin) {
        System.out.println("Admin Override: " + admin.getName() + " is forcing modification of " + appointmentId);
        return modifyAppointment(appointmentId, newDate, admin); 
    }
    
    /**
     * Retrieves all saved appointments.
     * * @return a list of confirmed appointments
     */
    public List<Appointment> getSavedAppointments() {
        return new ArrayList<>(savedAppointments);
    }
}