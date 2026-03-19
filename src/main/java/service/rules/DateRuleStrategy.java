package service.rules;

import domain.Appointment;
import service.time.TimeProvider;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enforces that appointments cannot be booked in the past.
 * Uses a TimeProvider to allow for Mockito time-manipulation in tests.
 */
public class DateRuleStrategy implements BookingRuleStrategy {
    
    private TimeProvider timeProvider;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DateRuleStrategy(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public boolean isValid(Appointment appointment) {
        try {
            LocalDateTime appointmentDate = LocalDateTime.parse(appointment.getDate(), formatter);
            
            // Check if the appointment date is before the current time provided by the TimeProvider
            if (appointmentDate.isBefore(timeProvider.now())) {
                System.out.println("Error: Cannot book an appointment in the past.");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd HH:mm");
            return false;
        }
    }
}