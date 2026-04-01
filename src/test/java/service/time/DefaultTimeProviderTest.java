package service.time;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class DefaultTimeProviderTest {
    @Test
    void testNowReturnsCurrentTime() {
        DefaultTimeProvider provider = new DefaultTimeProvider();
        LocalDateTime time = provider.now();
        
        // Verify it successfully grabbed a real time and isn't null
        assertNotNull(time, "The time provider should return a valid time");
    }
}