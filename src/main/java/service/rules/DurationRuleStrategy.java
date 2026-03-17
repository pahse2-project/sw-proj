package service.rules;

import domain.Appointment;

/**
 * Enforces the maximum duration rule using Polymorphism (US5.2).
 * @author [Your Name]
 * @version 1.0
 */
public class DurationRuleStrategy implements BookingRuleStrategy {
    
    /**
     * Validates if the appointment's duration is within the allowed limit for its specific type.
     * @param appointment the appointment to validate
     * @return true if the duration is valid, false if it exceeds the limit
     */
    @Override
    public boolean isValid(Appointment appointment) {
        int limit = appointment.getMaxAllowedDuration();
        
        if (appointment.getDurationInHours() > limit) {
            System.out.println("Error: " + appointment.getType() + " appointments cannot exceed " + limit + " hours.");
            return false;
        }
        return true;
    }
}