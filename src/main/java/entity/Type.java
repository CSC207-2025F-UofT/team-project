package entity;

import java.util.List;

public class Type {
    private String name;
    private int id;
    private List<String> strengths;
    private List<String> weaknesses;

    public Type(String name, int id, List<String> strengths, List<String> weaknesses) {
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

    public List<String> getStrengths() {
        return strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }
}
