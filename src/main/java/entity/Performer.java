package entity;

import java.util.List;

public class Performer {
    private final String id;
    private final String name;
    private final List<String> genres;

    public Performer(String id, String name, List<String> genres) {
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    // Getters
    private String getId() {
        return id;
    }
    private String getName() {
        return name;
    }
    private List<String> getGenres() {
        return genres;
    }
}


