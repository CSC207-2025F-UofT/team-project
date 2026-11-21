package interface_adapter.removeBook;

import use_case.removeBook.RemoveBookOutputBoundary;
import use_case.removeBook.RemoveBookOutputData;

/**
 * The Presenter for the Remove Book use case.
 */
public class RemoveBookPresenter implements RemoveBookOutputBoundary {
    private final RemoveBookViewModel removeBookViewModel;

    public RemoveBookPresenter(RemoveBookViewModel removeBookViewModel) {
        this.removeBookViewModel = removeBookViewModel;
    }

    @Override
    public void prepareSuccessView(RemoveBookOutputData outputData) {
        final RemoveBookState state = removeBookViewModel.getState();
        state.setRemovedOpenLibraryId(outputData.getOpenLibraryId());
        state.setMessage(outputData.getTitle() + " was removed from your bookshelf.");
        state.setError(null);
        removeBookViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final RemoveBookState state = removeBookViewModel.getState();
        state.setRemovedOpenLibraryId(null);
        state.setMessage(null);
        state.setError(errorMessage);
        removeBookViewModel.notifyStateChanged();
    }
}
