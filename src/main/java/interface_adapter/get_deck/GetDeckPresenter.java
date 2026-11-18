package interface_adapter.get_deck;

import interface_adapter.ViewManagerModel;
import interface_adapter.get_all_decks.GetAllDecksState;
import interface_adapter.get_all_decks.GetAllDecksViewModel;
import interface_adapter.get_deck.deck_view.GetDeckState;
import interface_adapter.get_deck.deck_view.GetDeckViewModel;
import interface_adapter.get_deck.study_view.StudyState;
import interface_adapter.get_deck.study_view.StudyViewModel;
import use_case.get_deck.GetDeckOutputBoundary;
import use_case.get_deck.GetDeckOutputData;

/**
 * The Presenter for the GetDeck Use Case.
 */
public class GetDeckPresenter implements GetDeckOutputBoundary {

    private final GetDeckViewModel getDeckViewModel;
    private final GetAllDecksViewModel getAllDecksViewModel;
    private final StudyViewModel studyViewModel;
    private final ViewManagerModel viewManagerModel;

    public GetDeckPresenter(ViewManagerModel viewManagerModel,
                            GetAllDecksViewModel getAllDecksViewModel,
                            GetDeckViewModel getDeckViewModel,
                            StudyViewModel studyViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.getAllDecksViewModel = getAllDecksViewModel;
        this.getDeckViewModel = getDeckViewModel;
        this.studyViewModel = studyViewModel;
    }

    @Override
    public void prepareSuccessView(GetDeckOutputData response) {
        // update GetDeckViewModel
        final GetDeckState getDeckState = new GetDeckState();
        getDeckState.setDeckName(response.getName());
        getDeckState.setFlashcards(response.getFlashcards());
        getDeckState.setViewName(response.getViewName());
        getDeckViewModel.setState(getDeckState);
        this.getDeckViewModel.notifyStateChanged();

        // update StudyViewModel
        StudyState studyState = new StudyState();
        studyState.setDeckName(response.getName());
        studyState.setFlashcards(response.getFlashcards());
        studyViewModel.setState(studyState);
        studyViewModel.notifyStateChanged();

        // clear everything from the GetAllDecksViewModel's state
        this.getAllDecksViewModel.setState(new GetAllDecksState());
        this.getAllDecksViewModel.notifyStateChanged();

        // switch to the proper view
        if (response.getViewName().equals("studyView")) {
            this.viewManagerModel.setState(StudyViewModel.VIEW_NAME);
        } else {
            this.viewManagerModel.setState(GetDeckViewModel.VIEW_NAME);
        }
        this.viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final GetDeckState getDeckState = getDeckViewModel.getState();
        getDeckState.setDeckError(error);
        getDeckViewModel.notifyStateChanged();
    }

    @Override
    public void prepareEmptyView(GetDeckOutputData response, String error) {
        if (response.getViewName().equals("studyView")) {
            prepareFailView(error);
        }
        getDeckViewModel.getState().setEmptyDeckError(error);
        prepareSuccessView(response);
    }

    @Override
    public void switchToDeckView() {
        viewManagerModel.setState(getDeckViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void switchToStudyView() {
        viewManagerModel.setState(StudyViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
