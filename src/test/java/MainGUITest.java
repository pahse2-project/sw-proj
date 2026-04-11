import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.awt.Window;

public class MainGUITest {

    @Test
    void testGUILoadsWithoutCrashing() {
        assertDoesNotThrow(() -> {
            // 1. Simply creating the object runs your GUI setup code (buttons, panels, etc.)
            new MainGUI(); 

            // 2. We ask Java to find any active windows that just popped up and close them
            for (Window window : Window.getWindows()) {
                window.dispose();
            }
        });
    }
}