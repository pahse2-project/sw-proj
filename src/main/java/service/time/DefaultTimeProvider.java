package service.time;

import java.time.LocalDateTime;

/**
 * Standard implementation of TimeProvider that returns the actual system time.
 */
public class DefaultTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}