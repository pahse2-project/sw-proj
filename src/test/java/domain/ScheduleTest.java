package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * Unit tests for Schedule fulfilling US1.3 requirements.
 * @author [Your Name]
 * @version 1.0
 */
public class ScheduleTest {
    
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = new Schedule();
    }

    @Test
    void testGetAvailableSlots() {
        // Acceptance: Only available slots are displayed
        List<String> slots = schedule.getAvailableSlots();
        
        // Verify the list is successfully retrieved and not empty
        assertNotNull(slots, "Available slots list should not be null");
        assertFalse(slots.isEmpty(), "Available slots list should contain our initial slots");
        
        // Verify it contains our mock data
        assertTrue(slots.contains("09:00 AM"), "Should contain the 9:00 AM slot");
        assertTrue(slots.contains("10:00 AM"), "Should contain the 10:00 AM slot");
    }
}