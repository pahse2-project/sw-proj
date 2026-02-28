package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the schedule and manages available time slots.
 * Part of the Domain Layer (US1.3).
 * @author [Your Name]
 * @version 1.0
 */
public class Schedule {
    private List<String> availableSlots;

    /**
     * Initializes a schedule with default available slots.
     */
    public Schedule() {
        this.availableSlots = new ArrayList<>();
        // Mock data for Sprint 1
        availableSlots.add("09:00 AM");
        availableSlots.add("10:00 AM");
        availableSlots.add("02:00 PM");
    }

    /**
     * Returns a list of available slots.
     * Acceptance: Only available slots are displayed.
     * @return List of time slots
     */
    public List<String> getAvailableSlots() {
        return new ArrayList<>(availableSlots);
    }
}