package domain;

/**
 * Represents a Group Appointment in the system.
 * Inherits from Appointment and overrides standard rules to support polymorphism.
 * * @author [Your Name]
 * @version 1.0
 */
public class GroupAppointment extends Appointment {

    /**
     * Constructs a new Group Appointment.
     * * @param appointmentId the unique ID for the appointment
     * @param date the date and time of the appointment
     * @param durationInHours the requested duration in hours
     * @param maxCapacity the maximum number of participants allowed
     */
    public GroupAppointment(String appointmentId, String date, int durationInHours, int maxCapacity) {
        super(appointmentId, date, durationInHours, maxCapacity);
        this.type = "Group";
    }

    /**
     * Polymorphic method defining the maximum allowed duration for a group appointment.
     * * @return the maximum allowed duration in hours (3 for groups)
     */
    @Override
    public int getMaxAllowedDuration() {
        return 3; 
    }
}