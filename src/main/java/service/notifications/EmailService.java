package service.notifications;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.logging.Level;

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

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);

            // FIX S2629: Use message format placeholders {0} for lazy evaluation
            LOGGER.log(Level.INFO, "Email sent successfully to {0}", to);

        } catch (MessagingException e) {
            /* FIX S2139 & S112: 
               Do not log AND throw. Throwing a specific exception with the 
               original 'e' as the cause allows the caller to handle/log it.
            */
            throw new IllegalStateException("Failed to send email to: " + to, e);
        }
    }

    static void run() {
        Dotenv dotenv = Dotenv.load();
        String user = dotenv.get("EMAIL_USERNAME");
        String pass = dotenv.get("EMAIL_PASSWORD");

        EmailService emailService = new EmailService(user, pass);
        String subject = "Appointment";
        String body = "Dear user, Your Appointment is coming soon. Best regards";
        
        try {
            emailService.sendEmail("s12323849@stu.najah.edu", subject, body);
        } catch (IllegalStateException e) {
            // Log once at the top level (the entry point)
            LOGGER.log(Level.SEVERE, "Application error during email dispatch", e);
        }
    }

    public static void main(String[] s) {
        run();
    }
}