package accountUtilities;
import java.io.FileNotFoundException;

public interface DataHandler {

    // Implement this interface to use other database

    void loadAccounts() throws FileNotFoundException;
    void saveAccounts();
    void createUser(String email, String password);
    void deleteUser(String user);
}