package domain;

/**
 * Represents an administrator in the system, inheriting from User.
 * * @author [Your Name]
 * @version 1.0
 */
public class Administrator extends User {
    
    /** The administrator's password for system access. */
    private String password;

    /**
     * Constructs a new Administrator.
     * * @param userId the unique identifier for the admin
     * @param name the name of the admin
     * @param email the email address of the admin
     * @param password the login password
     */
    public Administrator(String userId, String name, String email, String password) {
        super(userId, name, email);
        this.password = password;
    }

    /**
     * Gets the administrator's password.
     * * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the administrator's password.
     * * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}