package org.example.cocktailcraft.entity;
import java.util.List;
import java.util.ArrayList;

public class User {
    private final String id;
    private final String name;
    private final List<String> favourite;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.favourite = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getFavourite() {
        return List.copyOf(favourite);
    }

    public void favourite(String cocktailId) {
        if (favourite.contains(cocktailId)) {
            favourite.remove(cocktailId);
        } else {
            favourite.add(cocktailId);
        }
    }

}
