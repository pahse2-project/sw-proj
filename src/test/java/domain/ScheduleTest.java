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
    
    /** The schedule instance used for testing. */
    private Schedule schedule;

    /**
     * Sets up the test environment by initializing the schedule before each test.
     */
    @BeforeEach
    void setUp() {
        schedule = new Schedule();
    }

    /**
     * Tests the retrieval of available slots to ensure only valid, non-empty slots are returned.
     */
    @Test
    void testGetAvailableSlots() {
        List<String> slots = schedule.getAvailableSlots();
        
        assertNotNull(slots, "Available slots list should not be null");
        assertFalse(slots.isEmpty(), "Available slots list should contain our initial slots");
        
        assertTrue(slots.contains("09:00 AM"), "Should contain the 9:00 AM slot");
        assertTrue(slots.contains("10:00 AM"), "Should contain the 10:00 AM slot");
    }
}