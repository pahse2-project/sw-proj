package service.notifications;

import org.junit.jupiter.api.Test;
import domain.User;

public class EmailNotifierTest {
    @Test
    void testEmailNotification() {
        EmailNotifier notifier = new EmailNotifier();
        User testUser = new User("U1", "Test", "test@student.edu");
        notifier.notify(testUser, "This is a test message.");
    }
}