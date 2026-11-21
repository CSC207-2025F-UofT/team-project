package use_case.addBook;

import entity.Book;

/**
 * Stores the input data required to add a book
 * to the user's personal bookshelf.
 */
public class AddBookInputData {
    private final String openLibraryId;
    /**
     * Creates an AddBookInputData object.
     * @param openLibraryId the Open Library ID of the selected book
     */
    public AddBookInputData(String openLibraryId) {
        this.openLibraryId = openLibraryId;
    }

    /**
    * Returns the Open Library ID of the selected book.
    *
    * @return the Open Library ID
     */
    public String getOpenLibraryId() {
            return openLibraryId;
        }
}
