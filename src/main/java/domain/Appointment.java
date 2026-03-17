package domain;

/**
 * Represents an Appointment in the system.
 * Part of the Domain Layer.
 * * @author [Your Name]
 * @version 2.0
 */
public class Appointment {
    
    /** The unique identifier for the appointment. */
    private String appointmentId;
    
    /** The date and time of the appointment. */
    private String date; 
    
    /** The duration of the appointment in hours. */
    private int durationInHours;
    
    /** The maximum allowed participants for the appointment. */
    private int maxCapacity;
    
    /** The current number of participants booked. */
    private int currentParticipants;
    
    /** The current status of the appointment (e.g., Pending, Confirmed). */
    private String status; 

    /** The type of appointment (e.g., Standard, Urgent). */
    protected String type = "Standard"; 

    /**
     * Constructs an appointment.
     * * @param appointmentId unique ID
     * @param date date and time of the appointment
     * @param durationInHours how long the appointment lasts
     * @param maxCapacity maximum allowed participants
     */
    public Appointment(String appointmentId, String date, int durationInHours, int maxCapacity) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.durationInHours = durationInHours;
        this.maxCapacity = maxCapacity;
        this.currentParticipants = 1; 
        this.status = "Pending"; 
    }

    /**
     * Gets the duration of the appointment.
     * * @return the duration in hours
     */
    public int getDurationInHours() { return durationInHours; }

    /**
     * Gets the maximum capacity.
     * * @return the max capacity
     */
    public int getMaxCapacity() { return maxCapacity; }

    /**
     * Gets the current number of participants.
     * * @return the current participants
     */
    public int getCurrentParticipants() { return currentParticipants; }
    
    /**
     * Gets the current status of the appointment.
     * * @return the status
     */
    public String getStatus() { return status; }

    /**
     * Sets the status of the appointment.
     * * @param status the new status
     */
    public void setStatus(String status) { this.status = status; }
    
    /**
     * Gets the appointment ID.
     * * @return the appointment ID
     */
    public String getAppointmentId() { return appointmentId; }
    
    /**
     * Gets the date of the appointment.
     * * @return the appointment date
     */
    public String getDate() { return date; }

    /**
     * Sets the date of the appointment.
     * * @param date the new date
     */
    public void setDate(String date) { this.date = date; }
    
    /**
     * Gets the type of the appointment.
     * * @return the appointment type
     */
    public String getType() { return type; }
    
    /**
     * Polymorphic method for business rules defining the maximum allowed duration.
     * * @return the maximum allowed duration in hours
     */
    public int getMaxAllowedDuration() { return 2; }
}