package service.rules;

import domain.Appointment;

/**
 Polymorphism (US5.2).
 **/
public class DurationRuleStrategy implements BookingRuleStrategy {
    
    @Override
    public boolean isValid(Appointment appointment) {
        // Look here! It asks the appointment for its specific limit!
        int limit = appointment.getMaxAllowedDuration();
        
        if (appointment.getDurationInHours() > limit) {
            System.out.println("Error: " + appointment.getType() + " appointments cannot exceed " + limit + " hours.");
            return false;
        }
        return true;
    }
}