package intruderUtilities;

public class Intruder {
    // Intruder Entity Class
    private String id;
    private String name;
    private String timestamp;

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getTimestamp(){
        return this.timestamp;
    }
}
