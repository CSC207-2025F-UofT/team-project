package use_case.BuildPokemonTeam;

/**
 * The output boundary for the BuildPokemonTeam Use Case.
 */
public interface BuildPokemonTeamOutputBoundary {
    /**-are you
     * Prepares the success view for the BuildPokemonTeam Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(BuildPokemonTeamOutputData outputData);

    /**
     * Prepares the failure view for the BuildPokemonTeam Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}