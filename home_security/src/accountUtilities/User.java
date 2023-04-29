package accountUtilities;

public class User {
    // User Entity Class
    private String email;
    private String password;
    private String salt;

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public String getSalt(){
        return this.salt;
    }
}
