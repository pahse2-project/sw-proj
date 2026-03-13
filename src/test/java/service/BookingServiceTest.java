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
        Appointment appt = new Appointment("A300", "2023-11-10 10:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);
        
        boolean isCanceled = bookingService.cancelAppointment("A300", testUser);
        
        assertTrue(isCanceled, "Appointment should be canceled successfully");
        assertEquals("Canceled", appt.getStatus(), "Status should be updated to Canceled");
    }

    @Test
    void testCancelNonExistentAppointment_US4() {
        boolean isCanceled = bookingService.cancelAppointment("FAKE_ID", testUser);
        
        assertFalse(isCanceled, "Canceling a non-existent appointment should return false");
    }
    
    @Test
    void testModifyAppointment_US4() {
        Appointment appt = new Appointment("A400", "2023-11-15 10:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);        
        String newDate = "2023-11-16 14:00";
        boolean isModified = bookingService.modifyAppointment("A400", newDate, testUser);
        
        assertTrue(isModified, "Appointment should be modified successfully");
        assertEquals(newDate, appt.getDate(), "Appointment date should be updated to the new date");
    }

    @Test
    void testAdminCancelOverride_US4() {
        Appointment appt = new Appointment("A401", "2023-11-20 09:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);        
        domain.Administrator admin = new domain.Administrator("ADMIN1", "Admin Boss", "admin@student.edu", "securePass");        
        boolean isCanceled = bookingService.adminCancelAppointment("A401", admin);
        
        assertTrue(isCanceled, "Admin should be able to cancel the appointment");
        assertEquals("Canceled", appt.getStatus(), "Status should be updated to Canceled");
    }
    
    @Test
    void testAdminModifyOverride_US4_2() {
        Appointment appt = new Appointment("A402", "2023-11-22 09:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);
        domain.Administrator admin = new domain.Administrator("ADMIN2", "Admin Boss", "admin@student.edu", "securePass");
        String newDate = "2023-11-23 11:00";
        boolean isModified = bookingService.adminModifyAppointment("A402", newDate, admin);
        
        assertTrue(isModified, "Admin should be able to modify the appointment");
        assertEquals(newDate, appt.getDate(), "Appointment date should be updated to the new date by admin");
    }
    
    @Test
    void testPolymorphicRules_US5() {
        // US5.1 & US5.2: Different types, different rules.
        
        // 1. A standard appointment of 2 hours is VALID.
        Appointment standardAppt = new Appointment("A500", "2023-12-01 10:00", 2, 1);
        boolean isStandardBooked = bookingService.bookAppointment(standardAppt, testUser);
        assertTrue(isStandardBooked, "Standard 2-hour appointment should be valid");
        
        // 2. An Urgent appointment of 2 hours is INVALID (max is 1 hour).
        domain.UrgentAppointment urgentAppt = new domain.UrgentAppointment("A501", "2023-12-01 13:00", 2);
        boolean isUrgentBooked = bookingService.bookAppointment(urgentAppt, testUser);
        assertFalse(isUrgentBooked, "Urgent 2-hour appointment should be rejected due to polymorphic rule");
        assertEquals("Urgent", urgentAppt.getType(), "Type should be correctly stored as Urgent");
        
        // 3. A Group appointment of 3 hours is VALID (max is 3 hours).
        domain.GroupAppointment groupAppt = new domain.GroupAppointment("A502", "2023-12-02 10:00", 3, 10);
        boolean isGroupBooked = bookingService.bookAppointment(groupAppt, testUser);
        assertTrue(isGroupBooked, "Group 3-hour appointment should be valid due to polymorphic rule");
    }
}


