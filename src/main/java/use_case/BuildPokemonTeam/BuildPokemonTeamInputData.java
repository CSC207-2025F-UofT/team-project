package use_case.BuildPokemonTeam;

import entity.Team;

/**
 * The input data for the Pokemon Lookup Use Case.
 */

public class BuildPokemonTeamInputData {
    private final int index;
    private final String name;
    private final Team team;


    public BuildPokemonTeamInputData(int index, String name, Team team) {
        this.index = index;
        this.name = name;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public Team getTeam() {
        return team;
    }
}