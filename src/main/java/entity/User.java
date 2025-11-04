package entity;

import java.util.List;

public class User {
    private final long steamid;
    private final String username;
    private final List<User> friends;
    private final List<Game> library;

    public User(long id, String name, List<User> friends, List<Game> games) {
        this.steamid = id;
        this.username = name;
        this.friends = friends;
        this.library = games;
    }

    public long getId() {
        return this.steamid;
    }

    public String getUsername() {
        return this.username;
    }

    public List<User> getFriends() {
        return this.friends;
    }

    public List<Game> getLibrary() {
        return this.library;
    }
}
