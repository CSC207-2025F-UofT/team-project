package use_case.removeBook;

/**
 * Stores the input data required to remove a book from the user's bookshelf.
 */
public class RemoveBookInputData {
    private final String openLibraryId;

    /**
     * Creates a RemoveBookInputData object.
     * @param openLibraryId the OpenLibraryID of the book to remove
     */
    public RemoveBookInputData(String openLibraryId) {
        this.openLibraryId = openLibraryId;
    }

    /**
     * Returns the OpenLibraryID of the book to remove.
     * @return the OpenLibraryID
     */
    public String getOpenLibraryId() {
        return openLibraryId;
    }
}
