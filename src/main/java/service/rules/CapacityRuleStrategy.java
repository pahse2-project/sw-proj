package service.rules;

import domain.Appointment;

/**
 * Enforces the participant capacity rule (US2.3).
 * @author [Your Name]
 * @version 1.0
 */
public class CapacityRuleStrategy implements BookingRuleStrategy {

    /**
     * Validates if the appointment's current participants exceed the maximum allowed capacity.
     * @param appointment the appointment to validate
     * @return true if capacity is respected, false if it is exceeded
     */
    @Override
    public boolean isValid(Appointment appointment) {
        if (appointment.getCurrentParticipants() > appointment.getMaxCapacity()) {
            System.out.println("Error: Number of participants exceeds the maximum capacity.");
            return false;
        }
        return true;
    }
}