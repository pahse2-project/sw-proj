package service;

import domain.Administrator;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class responsible for managing administrator authentication.
 * * @author [Your Name]
 * @version 1.0
 */
public class AuthService {
    
    /** In-memory database storing registered administrators. */
    private Map<String, Administrator> adminDatabase;

    /**
     * Initializes the AuthService with an empty administrator database.
     */
    public AuthService() {
        this.adminDatabase = new HashMap<>();
    }

    /**
     * Registers a new administrator in the system.
     * * @param admin the Administrator object to register
     */
    public void registerAdmin(Administrator admin) {
        adminDatabase.put(admin.getUserId(), admin);
    }

    /**
     * Attempts to log in an administrator using their credentials.
     * * @param userId the user ID of the administrator
     * @param password the password of the administrator
     * @return true if credentials are valid, false otherwise
     */
    public boolean loginAdministrator(String userId, String password) {
        Administrator admin = adminDatabase.get(userId);
        if (admin != null && admin.getPassword().equals(password)) {
            System.out.println("Login success: Welcome, " + admin.getName() + "!");
            return true;
        }
        System.out.println("Error: Invalid credentials. Login failed.");
        return false;
    }

    /**
     * Logs out the currently authenticated administrator.
     * * @return true when logout is successful
     */
    public boolean logout() {
        System.out.println("Logout successful. Admin session closed.");
        return true;
    }
}