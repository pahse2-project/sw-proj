package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Appointment;
import domain.User;
import service.notifications.Observer;
import service.rules.DurationRuleStrategy;
import service.rules.CapacityRuleStrategy;

/**
 * Unit tests for Sprint 3: Notifications using the Observer Pattern and Mockito.
 */
public class NotificationTest {

    private BookingService bookingService;
    private Observer mockObserver;
    private User testUser;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());

        mockObserver = mock(Observer.class);
        
        bookingService.addObserver(mockObserver);

        testUser = new User("U1", "Alice", "alice@example.com");
    }

    @Test
    void testNotificationSentOnSuccessfulBooking() {
        Appointment appt = new Appointment("A200", "2023-11-05 10:00", 1, 5);
        
        boolean isBooked = bookingService.bookAppointment(appt, testUser);
        assertTrue(isBooked, "Appointment should be successfully booked");
        
        verify(mockObserver, times(1)).notify(eq(testUser), contains("A200"));
    }

    @Test
    void testNotificationNotSentOnFailedBooking() {
        Appointment longAppt = new Appointment("A201", "2023-11-05 10:00", 3, 5);
        
        boolean isBooked = bookingService.bookAppointment(longAppt, testUser);
        assertFalse(isBooked, "Appointment should fail validation");
        
        verify(mockObserver, never()).notify(any(User.class), anyString());
    }
}