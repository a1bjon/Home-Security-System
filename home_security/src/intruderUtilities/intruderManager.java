package intruderUtilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class intruderManager implements DataHandler{

    // array of intruders in data
    public ArrayList<Intruder> intruders;

    // enables readable JSON format
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Loads all intruders objects into memory to be accessed by program
    @Override
    public void loadIntruders() {
        try {
            Type listType = new TypeToken<List<Intruder>>() {}.getType();
            this.intruders = gson.fromJson(new FileReader("data/intruder_references/references.json"), listType);
        }
        catch (FileNotFoundException e){
            System.out.println("Error loading intruders into memory");
            e.printStackTrace();
        }
    }
}
