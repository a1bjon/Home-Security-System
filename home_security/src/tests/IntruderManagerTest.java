package tests;
import org.junit. Test;
import intruderUtilities.*;
import static org.junit.Assert.*;

public class IntruderManagerTest {
    @Test
    public void testLoadIntruders() {
        intruderManager im = new intruderManager();
        im.loadIntruders();
        assertEquals(true, im.intruders.size() > 0);
    }
}
