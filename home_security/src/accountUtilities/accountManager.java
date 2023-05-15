package accountUtilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class accountManager implements DataHandler{

    // array of accounts in data
    public ArrayList<User> accounts;
    public static String loggedInUser;


    // enables readable JSON format
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Loads all account objects into memory to be accessed by program
    @Override
    public void loadAccounts() {
        try {
            Type listType = new TypeToken<List<User>>() {}.getType();
            this.accounts = gson.fromJson(new FileReader("data/accounts.json"), listType);
        }
        catch (FileNotFoundException e){
            System.out.println("Error loading accounts into memory");
            e.printStackTrace();
        }
    }

    // Writes current accounts in memory to database
    @Override
    public void saveAccounts(){
        JsonArray jsonArr = gson.toJsonTree(this.accounts).getAsJsonArray();
        try{
            FileWriter writer = new FileWriter("data/accounts.json");
            gson.toJson(jsonArr, writer);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates new User and adds User to database
    @Override
    public void createUser(String email, String password){
        User newUser = new User();
        String userSalt = Hasher.generateSalt();
        newUser.setEmail(email);
        newUser.setPassword(Hasher.hashString(userSalt + password));
        newUser.setSalt(userSalt);

        this.accounts.add(newUser);
        this.saveAccounts();
    }

    // Deletes User from database
    @Override
    public void deleteUser(String user){
        for(int i = 0; i < this.accounts.size(); i++){
            if(this.accounts.get(i).getEmail().equals(user)){
                this.accounts.remove(i);
                break;
            }
        }
        this.saveAccounts();
    }

    // Checks if email provided is valid
    public static boolean isValidAddress(String email) {
        boolean check = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            check = false;
        }
        return check;
    }

    // Checks if email already exists in database
    public boolean emailExists(String email){
        for(int i = 0; i < this.accounts.size(); i++){
            if(email.equals(this.accounts.get(i).getEmail())){
                return true;
            }
        }
        return false;
    }

    public boolean login(String emailAddress, String password) {
        boolean accountCheck = false;
        User userLoginRequest = new User();
        userLoginRequest.setEmail(emailAddress);
        userLoginRequest.setPassword(password);
        for(int i = 0; i < this.accounts.size(); i++){
            if(this.accounts.get(i).getEmail().equals(emailAddress)){
                userLoginRequest.setSalt(this.accounts.get(i).getSalt());
            }
        }

        String hashedPassword = Hasher.hashString(userLoginRequest.getSalt() + userLoginRequest.getPassword());

        for(int i = 0; i < this.accounts.size(); i++){
            if(userLoginRequest.getEmail().equals(this.accounts.get(i).getEmail()) && this.accounts.get(i).getPassword().contains(hashedPassword)){
                accountCheck = true;
                break;
            }
        }
        return accountCheck;
    }
}
