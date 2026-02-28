package service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Administrator;

/**
 * Unit tests for AuthService fulfilling US1.1 and US1.2 requirements.
 */
public class AuthServiceTest {
    private AuthService authService;
    private Administrator admin;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        admin = new Administrator("admin1", "John Doe", "john@example.com", "password123");
        authService.registerAdmin(admin);
    }

    @Test
    void testLoginSuccess() {
        // Typo fixed here: "admin1" instead of "admins1"
        assertTrue(authService.loginAdministrator("admin1", "password123"), "Login should succeed with correct credentials");
    }

    @Test
    void testLoginFailure() {
        assertFalse(authService.loginAdministrator("admin1", "wrongPass"), "Login should fail with incorrect password");
        assertFalse(authService.loginAdministrator("unknown", "password123"), "Login should fail with non-existent ID");
    }
    
    @Test
    void testLogout() {
        // This actually tests the logout method we added to AuthService.java
        assertTrue(authService.logout(), "Logout should close the session successfully");
    }
}