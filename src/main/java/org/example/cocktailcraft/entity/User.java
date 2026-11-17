package org.example.cocktailcraft.entity;
import java.util.List;
import java.util.ArrayList;

public class User {
    private final String id;
    private final String name;
    private final List<Cocktail> favourite;

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

    public List<Cocktail> getFavourite() {
        return List.copyOf(favourite);
    }

    public void addFavourite(Cocktail cocktail){favourite.add(cocktail);}

    public void removeFavourite(Cocktail cocktail){favourite.remove(cocktail);}
}
