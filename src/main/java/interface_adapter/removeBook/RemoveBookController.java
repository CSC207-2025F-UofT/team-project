package interface_adapter.removeBook;

import use_case.removeBook.RemoveBookInputBoundary;
import use_case.removeBook.RemoveBookInputData;

/**
 * The controller for the Remove Book use case.
 */
public class RemoveBookController {
    private final RemoveBookInputBoundary removeBookInteractor;

    public RemoveBookController(RemoveBookInputBoundary removeBookInteractor) {
        this.removeBookInteractor = removeBookInteractor;
    }

    /**
     * Executes the Remove Book use case.
     * @param openLibraryId the OpenLibraryID of the book to remove
     */
    public void execute(String openLibraryId) {
        final RemoveBookInputData removeBookInputData = new RemoveBookInputData(openLibraryId);
        removeBookInteractor.execute(removeBookInputData);
    }
}
