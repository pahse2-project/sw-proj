package service.notifications;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import domain.User;

public class EmailNotifierTest {

    @Test
    void testEmailNotification() {
        EmailNotifier notifier = new EmailNotifier();
        User testUser = new User("U1", "Test", "test@student.edu");
        notifier.notify(testUser, "This is a test message.");
    }

    @Test
    void testEmailServiceMainAndRunMethods() {
        try {
            service.notifications.EmailService.main(new String[]{});
        } catch (Exception e) {
        	System.out.println("Main method crashed because: " + e.getMessage());        }
    }

    @Test
    void testEmailServiceCatchBlock() {
        service.notifications.EmailService badService = 
            new service.notifications.EmailService("fake_username", "wrong_password");
        
        try {
            badService.sendEmail("test@test.com", "Subject", "Body");
        } catch (RuntimeException e) {
        	System.out.println("SendEmail crashed because: " + e.getMessage());        }
    }
    
    @Test
    void testEmailNotificationExceptionPath() {
        EmailNotifier notifier = new EmailNotifier();
        
        User nullEmailUser = new User("U1", "Test", null); 
        
        notifier.notify(nullEmailUser, "This should safely hit the catch block.");
    }
        
    }
