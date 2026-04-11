package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Appointment;
import service.rules.CapacityRuleStrategy;
import service.rules.DurationRuleStrategy;
import domain.User;

public class BookingServiceTest {

    private BookingService bookingService;
    private User testUser;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        bookingService.addRule(new DurationRuleStrategy());
        bookingService.addRule(new CapacityRuleStrategy());
        testUser = new User("U001", "Test User", "mayshamayel78@gmail.com");
    }

    @Test
    void testBookValidAppointment_US2_1() {
        Appointment appt = new Appointment("A100", "2023-11-01 10:00", 1, 5);
        bookingService.addAvailableSlot(appt); // Admin adds slot
        
        boolean isBooked = bookingService.bookAppointment("A100", testUser); // User books it
        
        assertTrue(isBooked, "Valid appointment should be booked successfully");
        assertEquals("Confirmed", appt.getStatus(), "Status should be updated to Confirmed");
        assertEquals(1, bookingService.getMyBookings(testUser).size(), "Appointment should be in user's bookings");
    }

    @Test
    void testDurationRuleRejection_US2_2() {
        Appointment longAppt = new Appointment("A101", "2023-11-01 10:00", 3, 5);
        bookingService.addAvailableSlot(longAppt);
        
        boolean isBooked = bookingService.bookAppointment("A101", testUser);
        
        assertFalse(isBooked, "Appointment exceeding max duration should be rejected");
        assertEquals("Available", longAppt.getStatus(), "Status should remain Available");
    }

    @Test
    void testCapacityRuleRejection_US2_3() {
        Appointment crowdedAppt = new Appointment("A102", "2023-11-01 10:00", 1, 0);
        bookingService.addAvailableSlot(crowdedAppt);
        
        boolean isBooked = bookingService.bookAppointment("A102", testUser);
        
        assertFalse(isBooked, "Appointment exceeding capacity should be rejected");
    }
    
    @Test
    void testCancelAppointment_US4() {
        Appointment appt = new Appointment("A300", "2023-11-10 10:00", 1, 5);
        bookingService.addAvailableSlot(appt);
        bookingService.bookAppointment("A300", testUser);
        
        boolean isCanceled = bookingService.cancelUserAppointment("A300", testUser);
        
        assertTrue(isCanceled, "Appointment should be canceled successfully");
        assertEquals("Available", appt.getStatus(), "Status should revert to Available for others");
    }

    @Test
    void testCancelNonExistentAppointment_US4() {
        boolean isCanceled = bookingService.cancelUserAppointment("FAKE_ID", testUser);
        assertFalse(isCanceled, "Canceling a non-existent appointment should return false");
    }
    
    @Test
    void testModifyAppointment_US4() {
        Appointment appt = new Appointment("A400", "2023-11-15 10:00", 1, 5);
        bookingService.addAvailableSlot(appt);
        bookingService.bookAppointment("A400", testUser);        
        
        String newDate = "2023-11-16 14:00";
        boolean isModified = bookingService.modifyAppointment("A400", newDate, testUser);
        
        assertTrue(isModified, "Appointment should be modified successfully");
        assertEquals(newDate, appt.getDate(), "Appointment date should be updated");
    }

    @Test
    void testAdminCancelOverride_US4() {
        Appointment appt = new Appointment("A401", "2023-11-20 09:00", 1, 5);
        bookingService.addAvailableSlot(appt);
        bookingService.bookAppointment("A401", testUser);        
        
        boolean isCanceled = bookingService.adminDeleteSlot("A401");
        
        assertTrue(isCanceled, "Admin should be able to delete the appointment completely");
        assertEquals(0, bookingService.getAllAppointments().size(), "Appointment should be removed from system");
    }
    
    @Test
    void testAdminModifyOverride_US4_2() {
        Appointment appt = new Appointment("A402", "2023-11-22 09:00", 1, 5);
        bookingService.addAvailableSlot(appt);
        bookingService.bookAppointment("A402", testUser);
        
        domain.Administrator admin = new domain.Administrator("ADMIN2", "Admin Boss", "admin@student.edu", "securePass");
        String newDate = "2023-11-23 11:00";
        boolean isModified = bookingService.adminModifyAppointment("A402", newDate, admin);
        
        assertTrue(isModified, "Admin should be able to modify the appointment");
        assertEquals(newDate, appt.getDate(), "Appointment date should be updated");
    }
    
    @Test
    void testPolymorphicRules_US5() {
        Appointment standardAppt = new Appointment("A500", "2023-12-01 10:00", 2, 1);
        bookingService.addAvailableSlot(standardAppt);
        assertTrue(bookingService.bookAppointment("A500", testUser), "Standard valid");
        
        domain.UrgentAppointment urgentAppt = new domain.UrgentAppointment("A501", "2023-12-01 13:00", 2);
        bookingService.addAvailableSlot(urgentAppt);
        assertFalse(bookingService.bookAppointment("A501", testUser), "Urgent rejected");
        
        domain.GroupAppointment groupAppt = new domain.GroupAppointment("A502", "2023-12-02 10:00", 3, 10);
        bookingService.addAvailableSlot(groupAppt);
        assertTrue(bookingService.bookAppointment("A502", testUser), "Group valid");
    }
    
    @Test
    void testModifyNonExistentAppointment() {
        boolean result = bookingService.modifyAppointment("FAKE_ID", "2023-12-01 10:00", testUser);
        assertFalse(result, "Modifying a non-existent appointment should return false");
    }
}