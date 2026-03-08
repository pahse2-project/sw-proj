package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Appointment;
import service.rules.CapacityRuleStrategy;
import service.rules.DurationRuleStrategy;
import domain.User;

/**
 * Unit tests for BookingService, verifying Sprint 2 requirements.
 * @author [Your Name]
 * @version 1.0
 */
public class BookingServiceTest {

    private BookingService bookingService;
    private User testUser;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        // Add our Strategy rules to the service
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        testUser = new User("U001", "Test User", "test@student.edu");
    }

    @Test
    void testBookValidAppointment_US2_1() {
        // US2.1 Acceptance: Appointment is saved, Status = Confirmed
        // Params: ID, Date, Duration(1 hr), MaxCapacity(5)
        Appointment appt = new Appointment("A100", "2023-11-01 10:00", 1, 5);
        
        boolean isBooked = bookingService.bookAppointment(appt, testUser);
        
        assertTrue(isBooked, "Valid appointment should be booked successfully");
        assertEquals("Confirmed", appt.getStatus(), "Status should be updated to Confirmed");
        assertEquals(1, bookingService.getSavedAppointments().size(), "Appointment should be saved in the list");
    }

    @Test
    void testDurationRuleRejection_US2_2() {
        // US2.2 Acceptance: Maximum duration enforced (2 hours), Invalid rejected
        // Params: ID, Date, Duration(3 hrs - INVALID), MaxCapacity(5)
        Appointment longAppt = new Appointment("A101", "2023-11-01 10:00", 3, 5);
        
        boolean isBooked = bookingService.bookAppointment(longAppt, testUser);
        
        assertFalse(isBooked, "Appointment exceeding max duration should be rejected");
        assertEquals("Pending", longAppt.getStatus(), "Status should remain Pending");
    }

    @Test
    void testCapacityRuleRejection_US2_3() {
        // US2.3 Acceptance: Maximum number of participants enforced
        // Params: ID, Date, Duration(1 hr), MaxCapacity(0 - instantly forces capacity rejection since default participants is 1)
        Appointment crowdedAppt = new Appointment("A102", "2023-11-01 10:00", 1, 0);
        
        boolean isBooked = bookingService.bookAppointment(crowdedAppt, testUser);
        
        assertFalse(isBooked, "Appointment exceeding capacity should be rejected");
        assertEquals("Pending", crowdedAppt.getStatus(), "Status should remain Pending");
    }
    
    @Test
    void testCancelAppointment_US4() {
        // 1. Create and book an appointment
        Appointment appt = new Appointment("A300", "2023-11-10 10:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);
        
        // 2. Attempt to cancel it
        boolean isCanceled = bookingService.cancelAppointment("A300", testUser);
        
        // 3. Verify it was canceled successfully
        assertTrue(isCanceled, "Appointment should be canceled successfully");
        assertEquals("Canceled", appt.getStatus(), "Status should be updated to Canceled");
    }

    @Test
    void testCancelNonExistentAppointment_US4() {
        // Attempt to cancel an ID that doesn't exist
        boolean isCanceled = bookingService.cancelAppointment("FAKE_ID", testUser);
        
        // Verify it fails gracefully
        assertFalse(isCanceled, "Canceling a non-existent appointment should return false");
    }
}


