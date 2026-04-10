package service.notifications;
import jakarta.mail.*;
import jakarta.mail.internet.*;
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

            // ln 75 fix: use String.format instead of concatenation
            LOGGER.info(String.format("Email sent successfully to %s", to));

        } catch (MessagingException e) {
            // ln 59 fix: log AND rethrow with contextual info
            LOGGER.severe(String.format("Failed to send email to %s: %s", to, e.getMessage()));
            throw new RuntimeException("Failed to send email to: " + to, e);
        }
    }

    static void run() {
        Dotenv dotenv = Dotenv.load();
        String username = dotenv.get("EMAIL_USERNAME");
        String password = dotenv.get("EMAIL_PASSWORD");

        EmailService emailService = new EmailService(username, password);
        String subject = "Appointment";
        String body = "Dear user, Your Appointment is coming soon. Best regards";
        emailService.sendEmail("s12323849@stu.najah.edu", subject, body);
    }

    public static void main(String[] s) {
        run();
    }
}