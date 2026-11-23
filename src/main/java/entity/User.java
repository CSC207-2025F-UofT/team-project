package entity;

/** A User of LinkedOut */
public class User {
    public static final int EXP_PER_LEVEL = 100;

    private final String username;

    private String profilePicturePath;

    private int exp;

    private final String password; //note: future could add change password feature

    //private final String bio; //note: future could add user bio

    /** Creates a new user with no profile picture and 0 EXP */
    public User(String username, String password){
        this(username, null, 0, password);
    }

    /** Recreates a saved user with valid username, password, EXP, and a profile picture */
    public User(String username, String profilePicturePath, int exp, String password){
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
        if (exp < 0) {
            throw new IllegalArgumentException("Exp cannot be negative");
        }
        this.username = username;
        this.profilePicturePath = profilePicturePath;
        this.exp = exp;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getProfilePicturePath(){
        return profilePicturePath;
    }

    public int getExp(){
        return exp;
    }

    public String getPassword(){
        return password;
    }

    public void addExp(int expPerQuest) {
        if (expPerQuest <= 0) {
            throw new IllegalArgumentException("addExp must be positive");
        }
        exp += expPerQuest;
    }
    public int getLevel() {
        int level;
        level = exp / EXP_PER_LEVEL + 1;
        return level;
    }
    public int getExpToNextLevel() {
        int remainingExp;
        remainingExp = exp % EXP_PER_LEVEL;
        return EXP_PER_LEVEL - remainingExp;
    }
    public void setProfilePicturePath(String path) {
        this.profilePicturePath = path;
    }
}
