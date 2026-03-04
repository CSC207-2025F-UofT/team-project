package entity;

public class NewGame {
    String name;
    String username;
    String password;
    public NewGame(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public Boolean DataStored() {
        return null;
    }

    public Boolean DataProcessed() {
        return null;
    }

    public String PresentOutput() {
        return "";
    }

    public Boolean ChangeState() {
        return null;
    }
}
