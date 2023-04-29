package tests;
import org.junit. Test;
import accountUtilities.*;
import static org.junit.Assert.*;

public class HasherTest {

    @Test
    public void testHashedString() {
        String hashedString = Hasher.hashString("Test");
        assertNotEquals("Test", hashedString);
    }

    @Test
    public void testGenerateSalt() {
        String salt = Hasher.generateSalt();
        String salt_2 = Hasher.generateSalt();
        assertTrue(!salt.equals(salt_2));
    }
}
