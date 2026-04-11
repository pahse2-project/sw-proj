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
        bookingService.addAvailableSlot(appt);
        
        boolean isBooked = bookingService.bookAppointment("A200", testUser);
        assertTrue(isBooked, "Appointment should be successfully booked");
        
        verify(mockObserver, times(1)).notify(eq(testUser), contains("A200"));
    }

    @Test
    void testNotificationNotSentOnFailedBooking() {
        Appointment longAppt = new Appointment("A201", "2023-11-05 10:00", 3, 5);
        bookingService.addAvailableSlot(longAppt);
        
        boolean isBooked = bookingService.bookAppointment("A201", testUser);
        assertFalse(isBooked, "Appointment should fail validation");
        
        verify(mockObserver, never()).notify(any(User.class), anyString());
    }
    
    @Test
    void testNotificationSentOnCancellation() {
        Appointment appt = new Appointment("A300", "2023-11-05 10:00", 1, 5);
        bookingService.addAvailableSlot(appt);
        bookingService.bookAppointment("A300", testUser);
        reset(mockObserver); 
        
        bookingService.cancelUserAppointment("A300", testUser);
        
        // Match the string "Cancel" generated in BookingService
        verify(mockObserver, times(1)).notify(eq(testUser), contains("Cancel"));
    }

    @Test
    void testNotificationSentOnModification() {
        Appointment appt = new Appointment("A400", "2023-11-05 10:00", 1, 5);
        bookingService.addAvailableSlot(appt);
        bookingService.bookAppointment("A400", testUser); 
        reset(mockObserver);
        
        bookingService.modifyAppointment("A400", "2023-11-06 12:00", testUser);  
        
        // Match the string "Update" generated in BookingService
        verify(mockObserver, times(1)).notify(eq(testUser), contains("Update"));
    }
}