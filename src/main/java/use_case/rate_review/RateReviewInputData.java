package use_case.rate_review;
/**
 * The Input Data for the Rate and Review use case.
 */
public class RateReviewInputData {
    private final String openLibraryID;
    private final Integer rating;
    private final String review;

    public RateReviewInputData(String openLibraryID, Integer rating, String review) {
        this.openLibraryID = openLibraryID;
        this.rating = rating;
        this.review = review;
    }

    /**
     * Returns the review text of the selected book.
     *
     * @return the review text
     */
    String getReview() {
        return review;
    }

    /**
     * Returns the rating of the selected book.
     *
     * @return the rating
     */
    Integer getRating() {
        return rating;
    }

    /**
     * Returns the Open Library ID of the selected book.
     *
     * @return the Open Library ID
     */
    String getOpenLibraryID() {return openLibraryID;}
}
