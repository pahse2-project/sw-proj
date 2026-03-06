package service.notifications;

import domain.User;


/**
 * Observer interface for the notification system.
 * Part of the Observer Design Pattern for Sprint 3.
 * @author [Your Name]
 * @version 1.0
 */

public interface Observer {
    /**
     * Sends a notification to the user.
     * @param user the user to notify
     * @param message the content of the notification
     */
    void notify(User user, String message);

}