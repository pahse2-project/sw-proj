package domain;

/**
 * Represents an Urgent Appointment.
 * Demonstrates Polymorphism for Sprint 5.
 */
public class UrgentAppointment extends Appointment {

    public UrgentAppointment(String appointmentId, String date, int durationInHours) {
        // Urgent appointments always have a max capacity of 1
        super(appointmentId, date, durationInHours, 1);
        this.type = "Urgent"; // US5.1: Store type
    }

    /**
     * Polymorphism in action! US5.2: Apply different rules per type.
     * Urgent appointments cannot exceed 1 hour.
     */
    @Override
    public int getMaxAllowedDuration() {
        return 1; 
    }
}