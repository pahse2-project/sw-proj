package service.rules;

import domain.Appointment;

/**
 * Strategy interface for appointment booking rules.
 * @author [Your Name]
 * @version 1.0
 */
public interface BookingRuleStrategy {
    /**
     * Checks if the given appointment is valid according to the rule.
     * @param appointment the appointment to check
     * @return true if valid, false otherwise
     */
    boolean isValid(Appointment appointment);
}