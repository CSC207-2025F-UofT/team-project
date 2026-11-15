package interface_adapter.get_all_decks;

import interface_adapter.ViewManagerModel;
import use_case.get_all_decks.GetAllDecksOutputBoundary;
import use_case.get_all_decks.GetAllDecksOutputData;

/**
 * The Presenter for the GetAllDecks Use Case.
 */
public class GetAllDecksPresenter implements GetAllDecksOutputBoundary {

    private final GetAllDecksViewModel getAllDecksViewModel;
    private final ViewManagerModel viewManagerModel;

    public GetAllDecksPresenter(ViewManagerModel viewManagerModel,
                          GetAllDecksViewModel getAllDecksViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.getAllDecksViewModel = getAllDecksViewModel;
    }

    @Override
    public void prepareSuccessView(GetAllDecksOutputData response) {
        // On success, update the GetAllDecksViewModel's state
        final GetAllDecksState getAllDecksState = getAllDecksViewModel.getState();
        getAllDecksState.setDecks(response.getDecks());
        this.getAllDecksViewModel.notifyStateChanged();

        // switch to the GetAllDecks in view
        this.viewManagerModel.setState(GetAllDecksViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareEmptyView(String error) {
        final GetAllDecksState getAllDecksState = getAllDecksViewModel.getState();
        getAllDecksState.setGetAllDecksError(error);
        getAllDecksViewModel.notifyStateChanged();
    }

    @Override
    public void switchToDeckView() {
//        viewManagerModel.setState(GetDeckViewModel.VIEW_NAME);
//        viewManagerModel.notifyViewChanged();
    }
}
