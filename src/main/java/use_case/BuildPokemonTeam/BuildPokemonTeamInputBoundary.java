package use_case.BuildPokemonTeam;

import java.io.IOException;

/**
 * The Pokemon Lookup Use Case.
 */

public interface BuildPokemonTeamInputBoundary {

    /**
     * Execute the Pokemon Lookup Use Case.
     * @param BuildPokemonTeamInputData the input data for this use case
     */

    void execute(BuildPokemonTeamInputData BuildPokemonTeamInputData) throws IOException;
}