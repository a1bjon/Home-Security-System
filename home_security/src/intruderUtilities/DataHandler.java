package intruderUtilities;
import java.io.FileNotFoundException;

public interface DataHandler {

    // Implement this interface to use other database

    void loadIntruders() throws FileNotFoundException;
}