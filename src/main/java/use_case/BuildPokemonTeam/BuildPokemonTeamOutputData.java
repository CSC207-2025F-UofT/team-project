package use_case.BuildPokemonTeam;

import entity.Pokemon;
import entity.Team;

public class BuildPokemonTeamOutputData {
    private final Team Team;

    public BuildPokemonTeamOutputData(Team Team) {
        this.Team = Team;
    }

    public Team getTeam() {
        return Team;
    }

}