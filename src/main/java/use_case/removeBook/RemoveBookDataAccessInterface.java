package use_case.removeBook;

import entity.Book;
import entity.Bookshelf;

/**
 * DAO interface for the Remove Book use case.
 * Books are identified by their OpenLibraryID.
 */
public interface RemoveBookDataAccessInterface {
    /**
     * Returns the book on the bookshelf with the given OpenLibraryID.
     * @param openLibraryId the OpenLibraryID of the book
     * @return the book, or null if it is not on the bookshelf
     */
    Book getBook(String openLibraryId);

    /**
     * Returns the user's bookshelf.
     * @return the bookshelf
     */
    Bookshelf getBookshelf();

    /**
     * Saves the bookshelf.
     * @param bookshelf the bookshelf to save
     */
    void saveBookshelf(Bookshelf bookshelf);
}
