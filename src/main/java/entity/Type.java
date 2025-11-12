package entity;

import java.util.HashSet;

public class Type {
    private String name;
    private int id;
    private HashSet<String> strengths;
    private HashSet<String> weaknesses;

    public Type(String name, int id, HashSet<String> strengths, HashSet<String> weaknesses) {
        this.name = name;
        this.id = id;
        this.strengths = strengths;
        this.weaknesses = weaknesses;
    }

    public String getName() {
        return name;
    }

    public int  getId() {
        return id;
    }

    public HashSet<String> getStrengths() {
        return strengths;
    }

    public HashSet<String> getWeaknesses() {
        return weaknesses;
    }
}
