package interface_adapter.create_teams;

import interface_adapter.ViewManagerModel;
import use_case.start_game.StartGameOutputData;

public class StartGamePresenter {
    private final MainMenuViewModel mainMenuViewModel;
    private final ViewManagerModel viewManagerModel;

    public StartGamePresenter(ViewManagerModel viewManagerModel, MainMenuViewModel mainMenuViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.mainMenuViewModel = mainMenuViewModel;
    }

    @Override
    public void prepareSuccessView(StartGameOutputData teams) {
        // On success, update the MainMenuViewModel's state
        final MainMenuState state = mainMenuViewModel.getState();
        // Update any state stuff here
        mainMenuViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final MainMenuState state = mainMenuViewModel.getState();
        state.setLoadQuestionError(error);
        gameRunningViewModel.notifyStateChanged();
    }
}
