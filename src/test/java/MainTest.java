import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MainTest {

    @Test
    void testMainMethodFullCoverage() {
        // We simulate a user typing these exact commands into the console:
        String simulatedUserInput = 
            // --- GUEST ACTIONS ---
            "1\n\n" +                        // 1: View slots, press enter to cancel
            "1\nA1\n" +                      // 1: View slots, type A1 to book it
            "2\n" +                          // 2: View my bookings (should show A1)
            "3\nA1\n" +                      // 3: Cancel my booking (A1)
            "3\nFAKE_ID\n" +                 // 3: Try to cancel a fake ID to hit the error branch
            "99\n" +                         // Type an invalid menu option
            "4\nwrong_password\n" +          // 4: Admin login (Fail)
            
            // --- ADMIN ACTIONS ---
            "4\nadmin123\n" +                // 4: Admin login (Success!)
            "1\n" +                          // 1: Admin view ALL appointments
            "2\nA3\n2023-12-01 10:00\n1\n" + // 2: Admin add new slot (A3, 1 hour)
            "2\nA4\n2023-12-01 10:00\nbad\n" +// 2: Admin add slot (Type 'bad' to trigger NumberFormatException)
            "3\nA3\n" +                      // 3: Admin delete slot (A3)
            "3\nFAKE_ID\n" +                 // 3: Admin delete fake slot (Fail branch)
            "99\n" +                         // Type an invalid admin menu option
            "4\n" +                          // 4: Admin logout
            
            // --- EXIT ---
            "0\n";                           // 0: Exit the application gracefully

        // Save the original System.in so we can restore it later
        InputStream originalSystemIn = System.in;

        try {
            // Swap System.in with our simulated string
            ByteArrayInputStream simulatedInput = new ByteArrayInputStream(simulatedUserInput.getBytes());
            System.setIn(simulatedInput);
            
            // Run the main method. If it processes all inputs and exits via "0" without crashing, it passes!
            assertDoesNotThrow(() -> Main.main(new String[]{}));

        } finally {
            // ALWAYS restore original System.in, even if the test fails, 
            // so it doesn't break other tests in the suite.
            System.setIn(originalSystemIn);
        }
    }
}