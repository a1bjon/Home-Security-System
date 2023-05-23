package tests;
import org.junit.Test;
import accountUtilities.*;
import static org.junit.Assert.*;

public class AccountManagerTest {

    @Test
    public void testEmailExists() {
        accountManager am = new accountManager();
        am.loadAccounts();
        assertEquals(true, am.emailExists("test@org.com"));
        assertNotEquals(true, am.emailExists("test@ta@est.com"));
    }

    @Test
    public void testCreateUser() {
        int random = (int)(Math.random() * 500000 + 1);
        String id = "test" + random + "@org.com";
        accountManager am = new accountManager();
        am.loadAccounts();
        am.createUser(id, id);

        assertEquals(true, am.emailExists(id));
    }

    @Test
    public void testDeleteUser() {
        int random = (int)(Math.random() * 500000 + 1);
        String id = "test" + random + "@org.com";
        accountManager am = new accountManager();
        am.loadAccounts();
        am.createUser(id, id);
        am.deleteUser(id);

        assertEquals(false, am.emailExists(id));
    }

    @Test
    public void testLogin() {
        int random = (int)(Math.random() * 500000 + 1);
        String id = "test" + random + "@org.com";
        accountManager am = new accountManager();
        am.loadAccounts();
        am.createUser(id, id);
        assertEquals(true, am.login(id, id));
        assertEquals(false, am.login(id, "wrongpassword"));
    }
}
