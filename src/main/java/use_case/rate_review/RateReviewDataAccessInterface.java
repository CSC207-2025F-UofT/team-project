package use_case.rate_review;
import entity.Book;

/**
 * DAO interface for the Rate and Review Use Case.
 */
public interface RateReviewDataAccessInterface {

    /**
     * Checks if the given openLibraryID exists.
     * @param openLibraryID the openLibraryID to look for
     * @return true if a book with the given openLibraryID exists; false otherwise
     */
    boolean existsByOpenLibraryID(String openLibraryID);

    /**
     * Saves the openLibraryID.
     * @param openLibraryID the openLibraryID to save
     */
    void save(Book openLibraryID);

    /**
     * Returns the user with the given username.
     * @param openLibraryID the openLibraryID to look up
     * @return the book with the given openLibraryID
     */
    Book getBook(String openLibraryID);

    void saveBook(Book book);
}
