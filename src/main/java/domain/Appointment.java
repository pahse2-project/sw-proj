package domain;

/**
 * Represents an Appointment in the system.
 * Part of the Domain Layer. Updated for Sprint 2.
 * @author [Your Name]
 * @version 2.0
 */
public class Appointment {
    private String appointmentId;
    private String date; // E.g., "2023-11-01 10:00"
    private int durationInHours;
    private int maxCapacity;
    private int currentParticipants;
    private String status; 

    /**
     * Constructs an appointment.
     * @param appointmentId unique ID
     * @param date date and time of the appointment
     * @param durationInHours how long the appointment lasts
     * @param maxCapacity maximum allowed participants
     */
    public Appointment(String appointmentId, String date, int durationInHours, int maxCapacity) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.durationInHours = durationInHours;
        this.maxCapacity = maxCapacity;
        this.currentParticipants = 1; // Assuming the person booking counts as 1
        this.status = "Pending"; 
    }

    // --- Getters and Setters ---
    
    public int getDurationInHours() { return durationInHours; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentParticipants() { return currentParticipants; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getAppointmentId() { return appointmentId; }
    
    public String getDate() { 
        return date; 
    }

    public void setDate(String date) { 
        this.date = date; 
    }
}