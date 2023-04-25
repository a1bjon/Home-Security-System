package accountUtilities;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;


public class Hasher {

    // Creates non-pseudo random salt
    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        ArrayList<String> saltArray = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        String randomString = RandomStringUtils.randomAlphanumeric(16).toLowerCase();
        String salt;

        for(int i = 0; i < 16; i++){
            numbers.add(random.nextInt(300));
        }

        for(int i = 0; i < numbers.size(); i++){
            saltArray.add(numbers.get(i).toString());
        }

        for(int i = 0; i < randomString.length(); i++){
            char value = randomString.charAt(i);
            saltArray.add(String.valueOf(value));
        }

        // Applying Fisher-Yates algorithm to salt array
        Collections.shuffle(saltArray);
        salt = String.join("", saltArray);

        return salt;
    }

    // Creates secure hashed string using SHA-512 hashing algorithm
    public static String hashString(String plainText){
        return Hashing.sha512().hashString(plainText, StandardCharsets.UTF_8).toString();
    }
}
