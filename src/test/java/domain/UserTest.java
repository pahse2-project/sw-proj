package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    
    @Test
    void testUserGettersAndSetters() {
        User user = new User("U1", "Alice", "alice@test.com");
        
        // Test original values
        assertEquals("U1", user.getUserId());
        assertEquals("Alice", user.getName());
        assertEquals("alice@test.com", user.getEmail());
        
        // Test setters
        user.setUserId("U2");
        user.setName("Bob");
        user.setEmail("bob@test.com");
        
        // Test new values
        assertEquals("U2", user.getUserId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@test.com", user.getEmail());
    }
}