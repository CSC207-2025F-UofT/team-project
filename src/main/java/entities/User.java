package entities;

public class User {
    private final String name;
    private int score;
    private final String lobbyID;
    public User(String name, String lobbyID) {
        this.name = name;
        this.score = 0;
        this.lobbyID = lobbyID;
    }
    public String getName() {return this.name;
    }
    public int getScore() {
        return this.score;
    }
    public String getLobbyID() {
        return this.lobbyID;}
}
