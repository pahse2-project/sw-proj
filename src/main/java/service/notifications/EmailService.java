package service.notifications;


import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;




import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;

public class EmailService {
	
private static final java.util.logging.Logger LOGGER =
    java.util.logging.Logger.getLogger(EmailService.class.getName());
	
    private final String username;
    private final String password;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String to, String subject, String body) {
    	
        // SMTP configuration
        Properties props = new Properties();
        
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // e.g., Gmail SMTP
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Build email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send email
            Transport.send(message);

           LOGGER.info("Email sent successfully to " + to);

        } catch (MessagingException e) {
           LOGGER.severe("Failed to send email: " + e.getMessage());
            throw new RuntimeException(" Failed to send email", e);
        }
    }
    
    
    static void  run() {
    	

    	Dotenv dotenv = Dotenv.load();  
    	
    	String username = dotenv.get("EMAIL_USERNAME");
    	String password = dotenv.get("EMAIL_PASSWORD");
		
    	
    	EmailService emailService=new EmailService(username,password );
    	
    	String subject = "Appointment";
        String body = "Dear user, Your Appointment is comming soon. Best regards";
       
        
    	emailService.sendEmail("s12323849@stu.najah.edu", subject, body);
    	//emailService.sendEmail("mohammadnihad224@gmail.com", subject, body);
    
    	
    }
    
    
    public static void main(String []s) {
    	run();
    }
    
    
    /*Generate and Use an App Password
    1�Turn on 2-Step Verification

    You must enable 2-step verification (2FA) for your Google account before you can create an app password.

    Go to https://myaccount.google.com/security

    Under Signing in to Google, click 2-Step Verification and complete the setup.

    2Create an App Password

    After enabling 2FA, go back to the Security page.

    Under Signing in to Googleclick App passwords.

    Choose:

    App: Mail

    Device: Other (Custom name)type something like JavaMailApp

    Google will generate a 16-character password.*/
}
