package service.time;

import java.time.LocalDateTime;

/**
 * Interface for providing the current time.
 * Allows for time mocking during unit testing.
 */
public interface TimeProvider {
    /**
     * Gets the current date and time.
     * @return the current LocalDateTime
     */
    LocalDateTime now();
}