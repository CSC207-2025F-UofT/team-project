package interface_adapter.create_document;

import interface_adapter.ViewManagerModel;
import use_case.create_document.CreationOutputBoundary;
import use_case.create_document.CreationOutputData;

/**
 * The Presenter for the creating a document use case.
 */
public class CreationPresenter implements CreationOutputBoundary {

    private final CreationViewModel creationViewModel;
    private final NewDocumentViewModel newDocumentViewModel;
    private final ViewManagerModel viewManagerModel;

    public CreationPresenter(ViewManagerModel viewManagerModel,
                           CreationViewModel creationViewModel,
                           NewDocumentViewModel newDocumentViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.creationViewModel = creationViewModel;
        this.newDocumentViewModel = newDocumentViewModel;
    }

    @Override
    public void prepareSuccessView(CreationOutputData creationOutputData) {
        // On success, switch to the new document view.
        final NewDocumentState newDocumentState = newDocumentViewModel.getState();
        newDocumentState.setName(creationOutputData.getDocument().getName());
        newDocumentViewModel.notifyStateChanged();

        viewManagerModel.setState(NewDocumentViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final NewDocumentState newDocumentState = creationViewModel.getState();
        newDocumentState.setNameError(error);
        creationViewModel.notifyStateChanged();
    }

    @Override
    public void switchToNewDocumentView() {
        viewManagerModel.setState(CreationViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}