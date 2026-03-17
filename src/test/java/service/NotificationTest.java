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
 * @author [Your Name]
 * @version 1.0
 */
public class NotificationTest {

    /** The booking service instance used for testing. */
    private BookingService bookingService;
    
    /** A mocked observer to verify notification behaviors. */
    private Observer mockObserver;
    
    /** A test user representing the person receiving notifications. */
    private User testUser;

    /**
     * Sets up the test environment by initializing the service, adding rules, and configuring the mock observer.
     */
    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());

        mockObserver = mock(Observer.class);
        bookingService.addObserver(mockObserver);

        testUser = new User("U1", "Alice", "alice@example.com");
    }

    /**
     * Tests that a notification is successfully sent to the observer when a booking succeeds.
     */
    @Test
    void testNotificationSentOnSuccessfulBooking() {
        Appointment appt = new Appointment("A200", "2023-11-05 10:00", 1, 5);
        
        boolean isBooked = bookingService.bookAppointment(appt, testUser);
        assertTrue(isBooked, "Appointment should be successfully booked");
        
        verify(mockObserver, times(1)).notify(eq(testUser), contains("A200"));
    }

    /**
     * Tests that no notification is sent if the booking is rejected by business rules.
     */
    @Test
    void testNotificationNotSentOnFailedBooking() {
        Appointment longAppt = new Appointment("A201", "2023-11-05 10:00", 3, 5);
        
        boolean isBooked = bookingService.bookAppointment(longAppt, testUser);
        assertFalse(isBooked, "Appointment should fail validation");
        
        verify(mockObserver, never()).notify(any(User.class), anyString());
    }
}