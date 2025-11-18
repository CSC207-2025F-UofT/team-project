package use_case.open_team_entry;

/**
 * Input Boundary for actions which are related to opening the team entry page.
 */
public interface OpenTeamEntryInputBoundary {

    /**
     * Executes the open team entry page use case.
     */
    void execute();

    /**
     * Executes the switch to home page use case.
     */
    void switchToHomePage();
}
