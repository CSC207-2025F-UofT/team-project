package use_case.open_team_entry;

public interface OpenTeamEntryOutputBoundary {
    /**
     * Prepares the success view for the Open Team Entry Page Use Case
     * @param outputData the output data
     */
    void prepareSuccessView(OpenTeamEntryOutputData outputData);

    /**
     * Switches to the Home Page View.
     */
    void switchToHomePage();
}
