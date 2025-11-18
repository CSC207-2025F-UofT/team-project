package use_case.BuildPokemonTeam;

import java.io.IOException;

/**
 * The Pokemon Lookup Use Case.
 */

public interface BuildPokemonTeamInputBoundary {

    /**
     * Execute the add to Team Use Case.
     * @param BuildPokemonTeamInputData the input data for this use case
     */
    void addToTeam(BuildPokemonTeamInputData BuildPokemonTeamInputData) throws IOException;

    void removeFromTeam(BuildPokemonTeamInputData BuildPokemonTeamInputData) throws IOException;

    void saveTeam(BuildPokemonTeamInputData BuildPokemonTeamInputData) throws IOException;
}