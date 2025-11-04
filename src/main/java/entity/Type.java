package entity;

import java.util.List;

public class Type {
    private String name;
    private List<String> strengths;
    private List<String> weaknesses;

    public Type(String name, List<String> strengths, List<String> weaknesses) {
        this.name = name;
        this.strengths = strengths;
        this.weaknesses = weaknesses;
    }

    public String getName() {
        return name;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }
}
