package service.notifications;

import domain.User;

/**
 * Concrete implementation of the Observer interface for sending emails.
 * @author [Your Name]
 * @version 1.0
 */
public class EmailNotifier implements Observer {

    @Override
    public void notify(User user, String message) {
        // In a real system, this would connect to an SMTP server.
        // For our project, we just simulate it with a print statement.
        System.out.println("Sending Email to " + user.getEmail() + ": " + message);
    }
}