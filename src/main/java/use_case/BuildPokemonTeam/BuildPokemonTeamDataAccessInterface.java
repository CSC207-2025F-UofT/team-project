package use_case.BuildPokemonTeam;
import entity.Team;
import entity.Pokemon;

import java.util.Map;


public interface BuildPokemonTeamDataAccessInterface {
    /**
     * Saves or updates a team.
     * @param team the team to save
     */
    void saveTeam(Team team);

    /**
     * Checks if a team exists.
     * @param name name of the team
     * @return true if team exists
     */
    boolean exists(String name);

}
