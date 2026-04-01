package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdministratorTest {

    @Test
    void testAdminGettersAndSetters() {
        Administrator admin = new Administrator("A1", "Admin Boss", "admin@test.com", "pass123");
        
        // Test original password
        assertEquals("pass123", admin.getPassword());
        
        // Test setter
        admin.setPassword("newpass456");
        assertEquals("newpass456", admin.getPassword());
    }
}