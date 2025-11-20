package entity;
import java.util.ArrayList;

public class User {
    private final String name;
    private final String password;
    private final float[] coords = new float[2];

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.coords[0] = 0f;
        this.coords[1] = 0f;
    }
    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public float[] getCoords(){
        return this.coords;
    }
}
