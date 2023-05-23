package tests;
import org.junit.Test;
import accountUtilities.*;
import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void testEmailValidator() {
        accountManager am = new accountManager();
        assertEquals(true, am.isValidAddress("test@test.com"));
        assertNotEquals(true, am.isValidAddress("test@ta@est.com"));
    }
}
