package entity;
import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    // Private fields
    private String name;
    private int type1;
    private int type2;
    private ArrayList<Integer> stats;
    private ArrayList<Integer> abilities;
    private int hidden;
    private ArrayList<Integer> moves;
    private ArrayList<Integer> egggroup;
    private ArrayList<Integer> pokedexes;
    // Constructor
    public Pokemon(String name, int type1, int type2, ArrayList<Integer> stats,
                   ArrayList<Integer> abilities, int hidden, ArrayList<Integer> moves, ArrayList<Integer> egggroup, ArrayList<Integer> pokedexes) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.stats = stats;
        this.abilities = abilities;
        this.hidden = hidden;
        this.moves = moves;
        this.egggroup = egggroup;
        this.pokedexes = pokedexes;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", type1=" + type1 +
                ", type2=" + type2 +
                ", stats=" + stats +
                ", abilities=" + abilities +
                ", hidden=" + hidden +
                ", moves=" + moves +
                ", egggroups=" + egggroup +
                '}';
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType1() {
        return type1;
    }

    public void setType1(int type1) {
        this.type1 = type1;
    }

    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }

    public ArrayList<Integer> getStats() {
        return stats;
    }

    public void setStats(ArrayList<Integer> stats) {
        this.stats = stats;
    }

    public ArrayList<Integer> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<Integer> abilities) {
        this.abilities = abilities;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public List<Integer> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Integer> moves) {
        this.moves = moves;
    }

    public ArrayList<Integer> getEgggroup() {
        return egggroup;
    }

    public void setEgggroup(ArrayList<Integer> egggroup) {
        this.egggroup = egggroup;
    }

    public void setPokedexes(ArrayList<Integer> pokedexes) {
        this.pokedexes = pokedexes;
    }

    public List<Integer> getPokedexes() {
        return pokedexes;
    }

    // Helper methods to add single elements
    public void addStat(int stat) {
        this.stats.add(stat);
    }

    public void addAbility(int ability) {
        this.abilities.add(ability);
    }

    public void addMove(int move) {
        this.moves.add(move);
    }
}