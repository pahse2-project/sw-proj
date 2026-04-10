import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MainTest {

    @Test
    void testMainMethod() {
        
        String simulatedUserInput = 
            "1\n" +                  // Choose Option 1 (Book)
            "A99\n" +                // Enter Appt ID
            "2023-12-01 10:00\n" +   // Enter Date
            "2\n" +                  // Enter Duration
            "2\n" +                  // Choose Option 2 (View)
            "3\n";                   // Choose Option 3 (Exit)

        InputStream originalSystemIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

            Main.main(new String[]{});
            
        } finally {
            System.setIn(originalSystemIn);
        }
    }
}