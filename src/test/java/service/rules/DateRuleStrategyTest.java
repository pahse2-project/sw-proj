package service.rules;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Appointment;
import service.time.TimeProvider;
import java.time.LocalDateTime;

/**
 * Unit tests for DateRuleStrategy.
 * Demonstrates time and date mocking using Mockito as required.
 */
public class DateRuleStrategyTest {
    
    private TimeProvider mockTimeProvider;
    private DateRuleStrategy dateRule;

    @BeforeEach
    void setUp() {
        mockTimeProvider = mock(TimeProvider.class);
        dateRule = new DateRuleStrategy(mockTimeProvider);
    }

    @Test
    void testFutureAppointmentIsValid() {
        when(mockTimeProvider.now()).thenReturn(LocalDateTime.of(2023, 1, 1, 12, 0));        
        Appointment futureAppt = new Appointment("A1", "2023-12-01 10:00", 1, 5);
        assertTrue(dateRule.isValid(futureAppt), "Future appointment should be valid");
    }

    @Test
    void testPastAppointmentIsInvalid() {
        when(mockTimeProvider.now()).thenReturn(LocalDateTime.of(2024, 1, 1, 12, 0));
        
        Appointment pastAppt = new Appointment("A2", "2023-12-01 10:00", 1, 5);
        assertFalse(dateRule.isValid(pastAppt), "Past appointment should be rejected");
    }
    
    
    @Test
    void testAppointmentAtExactCurrentTime() {
        // Mock the time to be EXACTLY the same as the appointment time
        when(mockTimeProvider.now()).thenReturn(LocalDateTime.of(2023, 12, 1, 10, 0));
        Appointment exactAppt = new Appointment("A4", "2023-12-01 10:00", 1, 5);
        
        // isBefore() should return false here, meaning the appointment is valid
        assertTrue(dateRule.isValid(exactAppt), "Appointment at the exact current time should be valid");
    }

    @Test
    void testNullAppointmentDateIsRejected() {
        // Create an appointment with a 'null' date to trigger a different type of Exception
        Appointment nullDateAppt = new Appointment("A5", null, 1, 5);
        
        // The catch block should handle this gracefully and return false
        assertFalse(dateRule.isValid(nullDateAppt), "Appointment with a null date should be rejected");
    }
    
    
    
//    @Test
//    void testInvalidDateFormatIsRejected() {
//        // Create an appointment with a completely invalid date string
//        Appointment badFormatAppt = new Appointment("A3", "Not-a-real-date", 1, 5);
//        
//        // The rule should catch the Exception and return false
//        assertFalse(dateRule.isValid(badFormatAppt), "Appointment with bad date format should be rejected");
//    }
    
}