package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Administrator;

/**
 * Unit tests for AuthService fulfilling US1.1 and US1.2 requirements.
 * @author [Your Name]
 * @version 1.0
 */
public class AuthServiceTest {
    
    /** The authentication service instance used for testing. */
    private AuthService authService;
    
    /** A mock administrator used for testing login. */
    private Administrator admin;

    /**
     * Sets up the test environment by initializing the service and registering an admin.
     */
    @BeforeEach
    void setUp() {
        authService = new AuthService();
        admin = new Administrator("admin1", "John Doe", "john@example.com", "password123");
        authService.registerAdmin(admin);
    }

    /**
     * Tests that an administrator can successfully log in with correct credentials.
     */
    @Test
    void testLoginSuccess() {
        assertTrue(authService.loginAdministrator("admin1", "password123"), "Login should succeed with correct credentials");
    }

    /**
     * Tests that login fails when incorrect credentials or non-existent IDs are provided.
     */
    @Test
    void testLoginFailure() {
        assertFalse(authService.loginAdministrator("admin1", "wrongPass"), "Login should fail with incorrect password");
        assertFalse(authService.loginAdministrator("unknown", "password123"), "Login should fail with non-existent ID");
    }
    
    /**
     * Tests that an administrator can successfully log out.
     */
    @Test
    void testLogout() {
        assertTrue(authService.logout(), "Logout should close the session successfully");
    }
}