package use_case.rate_review;
import entity.Book;

/**
 * DAO interface for the Rate and Review Use Case.
 */
public interface RateReviewDataAccessInterface {

    /**
     * Returns the book with the given openLibraryID.
     * @param openLibraryID the openLibraryID to look up
     * @return the book with the given openLibraryID
     */
    Book getBook(String openLibraryID);

    /**
     * Saves the given book, including its current rating and review.
     * @param book the book to save
     */
    void saveBook(Book book);
}
