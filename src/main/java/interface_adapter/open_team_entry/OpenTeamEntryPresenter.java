package interface_adapter.open_team_entry;
import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import use_case.open_team_entry.OpenTeamEntryOutputBoundary;
import use_case.open_team_entry.OpenTeamEntryOutputData;

public class OpenTeamEntryPresenter implements OpenTeamEntryOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final OpenTeamEntryViewModel teamEntryViewModel;
    private final HomeViewModel homePageViewModel;


    public OpenTeamEntryPresenter(ViewManagerModel viewManagerModel,
                                  OpenTeamEntryViewModel teamEntryViewModel,
                                  HomeViewModel homePageViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.teamEntryViewModel = teamEntryViewModel;
        this.homePageViewModel = homePageViewModel;
    }

    @Override
    public void prepareSuccessView(OpenTeamEntryOutputData outputData) {
        teamEntryViewModel.setState(new OpenTeamEntryState());
        teamEntryViewModel.firePropertyChange();
        viewManagerModel.setState(teamEntryViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void switchToHomePage() {
        viewManagerModel.setState(homePageViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}

