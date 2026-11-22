package entity;

public class Player {
    private final String username;

    public Player(String username) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.username = username;
    }

    public String getName() {
        return username;
    }
}
