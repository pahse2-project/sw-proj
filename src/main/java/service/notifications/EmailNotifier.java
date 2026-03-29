package service.notifications;

import domain.User;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Concrete Observer that sends a REAL email notification using the provided EmailService.
 * @author [Your Name]
 * @version 2.0
 */
public class EmailNotifier implements Observer {

    /**
     * Triggers the real email sending process.
     * @param user the user who will receive the email
     * @param message the text/body of the email
     */
    @Override
    public void notify(User user, String message) {
        System.out.println("Attempting to send a REAL email to " + user.getEmail() + "...");
        
        try {
            // 1. Load the secret credentials from the .env file
            Dotenv dotenv = Dotenv.load();  
            String username = dotenv.get("EMAIL_USERNAME");
            String password = dotenv.get("EMAIL_PASSWORD");
            
            // 2. Create the professor's email service using those credentials
            EmailService emailService = new EmailService(username, password);
            
            // 3. Send the email!
            String subject = "Appointment System Notification";
            emailService.sendEmail(user.getEmail(), subject, message);
            
            System.out.println("Success: Real email sent to " + user.getEmail() + "!");
            
        } catch (Exception e) {
            System.out.println("Error: Could not send real email. " + e.getMessage());
        }
    }
}