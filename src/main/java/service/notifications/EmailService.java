package service.notifications;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Level;
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

    public void sendEmail(String to, String subject, String body) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
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

            // L43 fix: conditional invocation using lambda (only builds string if INFO is enabled)
            LOGGER.log(Level.INFO, "Email sent successfully to {0}", to);

        } catch (MessagingException e) {
            // L45 fix: only rethrow (no double logging), with contextual info
            // L48 fix: throw MessagingException instead of generic RuntimeException
            throw new MessagingException("Failed to send email to: " + to, e);
        }
    }

    static void run() {
        Dotenv dotenv = Dotenv.load();
        String username = dotenv.get("EMAIL_USERNAME");
        String password = dotenv.get("EMAIL_PASSWORD");

        EmailService emailService = new EmailService(username, password);
        String subject = "Appointment";
        String body = "Dear user, Your Appointment is coming soon. Best regards";
        try {
            emailService.sendEmail("s12323849@stu.najah.edu", subject, body);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Email failed in run(): {0}", e.getMessage());
        }
    }

    public static void main(String[] s) {
        run();
    }
}