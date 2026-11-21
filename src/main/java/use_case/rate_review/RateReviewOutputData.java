package use_case.rate_review;

/**
 * Stores the output data produced after successfully
 * adding a book to the user's bookshelf.
 */
public class RateReviewOutputData  {
    private final String openLibraryID;
    private final Integer rating;
    private final String review;

    public RateReviewOutputData(String openLibraryID, Integer rating, String review) {
        this.openLibraryID = openLibraryID;
        this.rating = rating;
        this.review = review;
    }

    /**
     * Returns the review of the book.
     *
     * @return the added review of the book
     */
    String getReview() {
        return review;
    }

    /**
     * Returns the rating of the book.
     *
     * @return the added rating of the book
     */
    Integer getRating() {
        return rating;
    }

    /**
     * Returns the openLibraryID of the book.
     *
     * @return the added openLibraryID of the book
     */
    String getOpenLibraryID() {return openLibraryID;}
}
