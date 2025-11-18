package use_case.open_team_entry;

public class OpenTeamEntryInteractor implements OpenTeamEntryInputBoundary {

    private final OpenTeamEntryOutputBoundary presenter;


    public OpenTeamEntryInteractor(OpenTeamEntryOutputBoundary presenter) {
        this.presenter = presenter;
    }
    public void execute() {
        OpenTeamEntryOutputData outputData = new OpenTeamEntryOutputData();
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void switchToHomePage() {
        presenter.switchToHomePage();
    }
}
