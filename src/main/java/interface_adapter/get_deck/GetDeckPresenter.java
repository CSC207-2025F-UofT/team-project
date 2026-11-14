package interface_adapter.get_deck;

import interface_adapter.ViewManagerModel;
import interface_adapter.get_all_decks.GetAllDecksViewModel;
import use_case.get_deck.GetDeckOutputBoundary;
import use_case.get_deck.GetDeckOutputData;

/**
 * The Presenter for the GetDeck Use Case.
 */
public class GetDeckPresenter implements GetDeckOutputBoundary {

    private final GetDeckViewModel getDeckViewModel;
    private final GetAllDecksViewModel getAllDecksViewModel;
    private final ViewManagerModel viewManagerModel;

    public GetDeckPresenter(ViewManagerModel viewManagerModel,
                            GetAllDecksViewModel getAllDecksViewModel,
                            GetDeckViewModel getDeckViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.getAllDecksViewModel = getAllDecksViewModel;
        this.getDeckViewModel = getDeckViewModel;
    }

    @Override
    public void prepareSuccessView(GetDeckOutputData response) {
        // update the GetDeckViewModel's state
        final GetDeckState getDeckState = getDeckViewModel.getState();
        getDeckState.setDeck(response.getName, response.getFlashcards());
        this.getDeckViewModel.notifyStateChanged();


    }
}
