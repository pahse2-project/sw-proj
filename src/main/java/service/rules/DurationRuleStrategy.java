package service.rules;

import domain.Appointment;

/**
 * Enforces the maximum duration rule (US2.2).
 * Maximum allowed duration is 2 hours.
 */
public class DurationRuleStrategy implements BookingRuleStrategy {
    
    private static final int MAX_DURATION = 2;

    @Override
    public boolean isValid(Appointment appointment) {
        if (appointment.getDurationInHours() > MAX_DURATION) {
            System.out.println("Error: Appointment duration exceeds the maximum limit of " + MAX_DURATION + " hours.");
            return false;
        }
        return true;
    }
}