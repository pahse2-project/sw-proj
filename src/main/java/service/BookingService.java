package service;

import domain.Appointment;
import service.rules.BookingRuleStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for coordinating appointment bookings.
 * Evaluates business rules using the Strategy Pattern before confirming.
 * @author [Your Name]
 * @version 1.0
 */
public class BookingService {
    
    private List<Appointment> savedAppointments;
    private List<BookingRuleStrategy> bookingRules;

    /**
     * Initializes the BookingService with an empty database and rule list.
     */
    public BookingService() {
        this.savedAppointments = new ArrayList<>();
        this.bookingRules = new ArrayList<>();
    }

    /**
     * Adds a business rule strategy to the service.
     * @param rule the booking rule to enforce
     */
    public void addRule(BookingRuleStrategy rule) {
        this.bookingRules.add(rule);
    }

    /**
     * Attempts to book an appointment by validating it against all business rules.
     * Fulfills US2.1, US2.2, and US2.3.
     * @param appointment the appointment to book
     * @return true if successfully booked and confirmed, false otherwise
     */
    public boolean bookAppointment(Appointment appointment) {
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
        return true;
    }

    /**
     * Retrieves all saved appointments.
     * @return list of confirmed appointments
     */
    public List<Appointment> getSavedAppointments() {
        return new ArrayList<>(savedAppointments);
    }
}