import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MainTest {

    @Test
    void testMainMethod() {
        String simulatedUserInput =
            "1\n" +
            "A99\n" +
            "2023-12-01 10:00\n" +
            "2\n" +
            "2\n" +
            "3\n";

        InputStream originalSystemIn = System.in;

        try (ByteArrayInputStream simulatedInput = new ByteArrayInputStream(simulatedUserInput.getBytes())) {
            System.setIn(simulatedInput);
            assertDoesNotThrow(() -> Main.main(new String[]{}));

        } catch (Exception e) {
            System.setIn(originalSystemIn);
        } finally {
            System.setIn(originalSystemIn);
        }
    }
}