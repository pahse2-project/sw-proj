package domain; 

/**
 * Represents a generic user in the system.
 * * @author [Your Name]
 * @version 1.0
 */
public class User {
    
    /** The unique identifier for the user. */
    private String userId;
    
    /** The full name of the user. */
    private String name;
    
    /** The email address of the user. */
    private String email;

    /**
     * Constructs a new User.
     * * @param userId the unique identifier for the user
     * @param name the full name of the user
     * @param email the email address of the user
     */
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    /**
     * Gets the user's ID.
     * * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user's ID.
     * * @param userId the new user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's name.
     * * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     * * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's email.
     * * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     * * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}