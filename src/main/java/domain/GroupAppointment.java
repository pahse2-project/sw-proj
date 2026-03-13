package domain;

public class GroupAppointment extends Appointment {

    public GroupAppointment(String appointmentId, String date, int durationInHours, int maxCapacity) {
        super(appointmentId, date, durationInHours, maxCapacity);
        this.type = "Group";
    }

    @Override
    public int getMaxAllowedDuration() {
        return 3; // Groups get more time!
    }
}