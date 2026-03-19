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
        // 1. Mock the TimeProvider using Mockito
        mockTimeProvider = mock(TimeProvider.class);
        
        // 2. Pass the mock into our rule
        dateRule = new DateRuleStrategy(mockTimeProvider);
    }

    @Test
    void testFutureAppointmentIsValid() {
        // Tell Mockito: "Whenever now() is called, pretend it is January 1, 2023 at 12:00 PM"
        when(mockTimeProvider.now()).thenReturn(LocalDateTime.of(2023, 1, 1, 12, 0));
        
        // This appointment is in December 2023 (in the FUTURE compared to our mocked time)
        Appointment futureAppt = new Appointment("A1", "2023-12-01 10:00", 1, 5);
        assertTrue(dateRule.isValid(futureAppt), "Future appointment should be valid");
    }

    @Test
    void testPastAppointmentIsInvalid() {
        // Tell Mockito: "Whenever now() is called, pretend it is January 1, 2024 at 12:00 PM"
        when(mockTimeProvider.now()).thenReturn(LocalDateTime.of(2024, 1, 1, 12, 0));
        
        // This appointment is in December 2023 (now in the PAST compared to our mocked time)
        Appointment pastAppt = new Appointment("A2", "2023-12-01 10:00", 1, 5);
        assertFalse(dateRule.isValid(pastAppt), "Past appointment should be rejected");
    }
}