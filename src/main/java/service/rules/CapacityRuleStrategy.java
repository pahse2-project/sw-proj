package service.rules;

import domain.Appointment;

/**
 * Enforces the participant capacity rule (US2.3).
 */
public class CapacityRuleStrategy implements BookingRuleStrategy {

    @Override
    public boolean isValid(Appointment appointment) {
        if (appointment.getCurrentParticipants() > appointment.getMaxCapacity()) {
            System.out.println("Error: Number of participants exceeds the maximum capacity.");
            return false;
        }
        return true;
    }
}