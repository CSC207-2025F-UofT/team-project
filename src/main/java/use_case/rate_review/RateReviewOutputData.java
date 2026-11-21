package use_case.rate_review;

public class RateReviewOutputData  {
    private final String openLibraryID;
    private final Integer rating;
    private final String review;

    public RateReviewOutputData(String openLibraryID, Integer rating, String review) {
        this.openLibraryID = openLibraryID;
        this.rating = rating;
        this.review = review;
    }

    String getReview() {
        return review;
    }

    Integer getRating() {
        return rating;
    }
    String getOpenLibraryID() {return openLibraryID;}
}
