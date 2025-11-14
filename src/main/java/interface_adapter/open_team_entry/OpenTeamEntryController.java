package interface_adapter.open_team_entry;

import use_case.open_team_entry.OpenTeamEntryInputBoundary;

public class OpenTeamEntryController {

    private final OpenTeamEntryInputBoundary openTeamEntryUseCaseInteractor;

    public OpenTeamEntryController(OpenTeamEntryInputBoundary openTeamEntryUseCaseInteractor) {
        this.openTeamEntryUseCaseInteractor = openTeamEntryUseCaseInteractor;
    }

    /**
     * Executes the Open Team Entry Page Use Case.
     */
    public void execute() {
        openTeamEntryUseCaseInteractor.execute();
    }

    public void switchToHomePage() {
        openTeamEntryUseCaseInteractor.switchToHomePage();
    }
}
