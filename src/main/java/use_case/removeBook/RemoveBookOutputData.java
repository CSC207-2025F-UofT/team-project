package use_case.removeBook;

/**
 * Stores the output data produced after a book is removed from the bookshelf.
 */
public class RemoveBookOutputData {
    private final String openLibraryId;
    private final String title;

    /**
     * Creates a RemoveBookOutputData object.
     * @param openLibraryId the OpenLibraryID of the removed book
     * @param title the title of the removed book
     */
    public RemoveBookOutputData(String openLibraryId, String title) {
        this.openLibraryId = openLibraryId;
        this.title = title;
    }

    /**
     * Returns the OpenLibraryID of the removed book.
     * @return the OpenLibraryID
     */
    public String getOpenLibraryId() {
        return openLibraryId;
    }

    /**
     * Returns the title of the removed book.
     * @return the title
     */
    public String getTitle() {
        return title;
    }
}
