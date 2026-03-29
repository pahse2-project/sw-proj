package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Appointment;
import service.rules.CapacityRuleStrategy;
import service.rules.DurationRuleStrategy;
import domain.User;

/**
 * Unit tests for BookingService, verifying Sprint 2, Sprint 4, and Sprint 5 requirements.
 * @author [Your Name]
 * @version 1.0
 */
public class BookingServiceTest {

    /** The booking service instance used for testing. */
    private BookingService bookingService;
    
    /** A test user representing the person making the bookings. */
    private User testUser;

    /**
     * Sets up the test environment by initializing the service, adding rules, and creating a test user.
     */
    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        testUser = new User("U001", "Test User", "mayshamayel78@gmail.com");
    }

    /**
     * Tests booking a valid appointment ensures it gets saved and confirmed.
     */
    @Test
    void testBookValidAppointment_US2_1() {
        Appointment appt = new Appointment("A100", "2023-11-01 10:00", 1, 5);
        boolean isBooked = bookingService.bookAppointment(appt, testUser);
        
        assertTrue(isBooked, "Valid appointment should be booked successfully");
        assertEquals("Confirmed", appt.getStatus(), "Status should be updated to Confirmed");
        assertEquals(1, bookingService.getSavedAppointments().size(), "Appointment should be saved in the list");
    }

    /**
     * Tests that an appointment exceeding the maximum allowed duration is rejected.
     */
    @Test
    void testDurationRuleRejection_US2_2() {
        Appointment longAppt = new Appointment("A101", "2023-11-01 10:00", 3, 5);
        boolean isBooked = bookingService.bookAppointment(longAppt, testUser);
        
        assertFalse(isBooked, "Appointment exceeding max duration should be rejected");
        assertEquals("Pending", longAppt.getStatus(), "Status should remain Pending");
    }

    /**
     * Tests that an appointment with a maximum capacity of zero triggers a capacity rule rejection.
     */
    @Test
    void testCapacityRuleRejection_US2_3() {
        Appointment crowdedAppt = new Appointment("A102", "2023-11-01 10:00", 1, 0);
        boolean isBooked = bookingService.bookAppointment(crowdedAppt, testUser);
        
        assertFalse(isBooked, "Appointment exceeding capacity should be rejected");
        assertEquals("Pending", crowdedAppt.getStatus(), "Status should remain Pending");
    }
    
    /**
     * Tests that an existing appointment can be successfully canceled.
     */
    @Test
    void testCancelAppointment_US4() {
        Appointment appt = new Appointment("A300", "2023-11-10 10:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);
        boolean isCanceled = bookingService.cancelAppointment("A300", testUser);
        
        assertTrue(isCanceled, "Appointment should be canceled successfully");
        assertEquals("Canceled", appt.getStatus(), "Status should be updated to Canceled");
    }

    /**
     * Tests that attempting to cancel a non-existent appointment fails gracefully.
     */
    @Test
    void testCancelNonExistentAppointment_US4() {
        boolean isCanceled = bookingService.cancelAppointment("FAKE_ID", testUser);
        assertFalse(isCanceled, "Canceling a non-existent appointment should return false");
    }
    
    /**
     * Tests that the date of an existing appointment can be successfully modified.
     */
    @Test
    void testModifyAppointment_US4() {
        Appointment appt = new Appointment("A400", "2023-11-15 10:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);        
        String newDate = "2023-11-16 14:00";
        boolean isModified = bookingService.modifyAppointment("A400", newDate, testUser);
        
        assertTrue(isModified, "Appointment should be modified successfully");
        assertEquals(newDate, appt.getDate(), "Appointment date should be updated to the new date");
    }

    /**
     * Tests that an administrator can override standard rules to cancel an appointment.
     */
    @Test
    void testAdminCancelOverride_US4() {
        Appointment appt = new Appointment("A401", "2023-11-20 09:00", 1, 5);
        bookingService.bookAppointment(appt, testUser);        
        domain.Administrator admin = new domain.Administrator("ADMIN1", "Admin Boss", "admin@student.edu", "securePass");        
        boolean isCanceled = bookingService.adminCancelAppointment("A401", admin);
        
        assertTrue(isCanceled, "Admin should be able to cancel the appointment");
        assertEquals("Canceled", appt.getStatus(), "Status should be updated to Canceled");
    }
    
    /**
     * Tests that an administrator can override standard rules to modify an appointment.
     */
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
    
    /**
     * Tests that different appointment types trigger different polymorphic business rules.
     */
    @Test
    void testPolymorphicRules_US5() {
        Appointment standardAppt = new Appointment("A500", "2023-12-01 10:00", 2, 1);
        boolean isStandardBooked = bookingService.bookAppointment(standardAppt, testUser);
        assertTrue(isStandardBooked, "Standard 2-hour appointment should be valid");
        
        domain.UrgentAppointment urgentAppt = new domain.UrgentAppointment("A501", "2023-12-01 13:00", 2);
        boolean isUrgentBooked = bookingService.bookAppointment(urgentAppt, testUser);
        assertFalse(isUrgentBooked, "Urgent 2-hour appointment should be rejected due to polymorphic rule");
        assertEquals("Urgent", urgentAppt.getType(), "Type should be correctly stored as Urgent");
        
        domain.GroupAppointment groupAppt = new domain.GroupAppointment("A502", "2023-12-02 10:00", 3, 10);
        boolean isGroupBooked = bookingService.bookAppointment(groupAppt, testUser);
        assertTrue(isGroupBooked, "Group 3-hour appointment should be valid due to polymorphic rule");
    }
}