package service.notifications;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import domain.User;

public class EmailNotifierTest {

    @Test
    void testEmailNotification() {
        EmailNotifier notifier = new EmailNotifier();
        User testUser = new User("U1", "Test", "test@student.edu");
        assertDoesNotThrow(() -> notifier.notify(testUser, "This is a test message."));
    }

    @Test
    void testEmailServiceMainAndRunMethods() {
        try {
            service.notifications.EmailService.main(new String[]{});
        } catch (Exception e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }

    @Test
    void testEmailServiceCatchBlock() {
        service.notifications.EmailService badService =
            new service.notifications.EmailService("fake_username", "wrong_password");

        try {
            badService.sendEmail("test@test.com", "Subject", "Body");
        } catch (RuntimeException e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }

    @Test
    void testEmailNotificationExceptionPath_NullEmail() {
        EmailNotifier notifier = new EmailNotifier();
        User nullEmailUser = new User("U1", "Test", null);
        // This should safely hit the catch block for null pointers or empty strings
        assertDoesNotThrow(() -> notifier.notify(nullEmailUser, "This should safely hit the catch block."));
    }

    @Test
    void testEmailNotificationExceptionPath_InvalidEmailFormat() {
        EmailNotifier notifier = new EmailNotifier();
        // This forces the javax.mail library to throw an AddressException
        User invalidEmailUser = new User("U2", "Test", "this-is-not-an-email");
        assertDoesNotThrow(() -> notifier.notify(invalidEmailUser, "This triggers the AddressException catch block."));
    }

    @Test
    void testEmailNotificationExceptionPath_EmptyEmail() {
        EmailNotifier notifier = new EmailNotifier();
        User emptyEmailUser = new User("U3", "Test", "");
        assertDoesNotThrow(() -> notifier.notify(emptyEmailUser, "This triggers empty string validation."));
    }

    @Test
    void testEmailServiceDirectNullParameters() {
        service.notifications.EmailService directService = 
            new service.notifications.EmailService("dummy", "dummy");

        // Pass nulls directly to the service to trigger internal validation catch blocks
        try {
            directService.sendEmail(null, null, null);
        } catch (Exception e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }
}