package entity;

public class Team {
    private String teamName;
    private Pokemon[] pokemon;

    public Team(String teamName) {
        this.teamName = teamName;
        this.pokemon = new Pokemon[6];
    }

    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public Pokemon[] getPokemon() {
        return pokemon;
    }
    public Pokemon getPokemon(int index){
        return pokemon[index];
    }

    public void setPokemon(Pokemon[] pokemon) {
        this.pokemon = pokemon;
    }
    public void setPokemon(Pokemon pokemon, int index) {
        this.pokemon[index] = pokemon;
    }
}
