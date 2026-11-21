package use_case.rate_review;

import entity.Book;
import entity.Review;

public class RateReviewInteractor implements RateReviewInputBoundary{
    private final RateReviewDataAccessInterface dataAccessObject;
    private final RateReviewOutputBoundary rateReviewPresenter;
    public RateReviewInteractor(RateReviewDataAccessInterface dataAccessObject,
                                RateReviewOutputBoundary rateReviewOutputBoundary){
        this.dataAccessObject = dataAccessObject;
        this.rateReviewPresenter = rateReviewOutputBoundary;
    }

    @Override
    public void execute(RateReviewInputData rateReviewInputData) {
        final String openLibraryID = rateReviewInputData.getOpenLibraryID();
        final Book book = dataAccessObject.getBook(rateReviewInputData.getOpenLibraryID());
        final Integer reviewRating = rateReviewInputData.getRating();
        final String reviewText = rateReviewInputData.getReview() == null ? "" :
                rateReviewInputData.getReview().trim();
        if (book == null|| !book.isShelved()){
            rateReviewPresenter.prepareFailView(openLibraryID
                    + " : Add book to bookshelf before reviewing it.");
        } else if (reviewRating == null && reviewText.isEmpty()) {
            rateReviewPresenter.prepareFailView(openLibraryID
                    + " : Please enter rating and/or review.");
        } else if (reviewRating != null &&
                (reviewRating < Review.MIN_RATING || reviewRating > Review.MAX_RATING)){
            rateReviewPresenter.prepareFailView(openLibraryID
                    + " : Invalid rating, please enter number between "
                    + Review.MIN_RATING + " - " + Review.MAX_RATING );
        }else {
            final Review review = new Review(openLibraryID, reviewRating, reviewText);
            book.setReview(review);
            dataAccessObject.saveBook(book);
            RateReviewOutputData rateReviewOutputData =
                    new RateReviewOutputData(book.getOpenLibraryId(), review.getRating(),
                            review.getReview());
            rateReviewPresenter.prepareSuccessView(new RateReviewOutputData(book.getOpenLibraryId(),
                    review.getRating(), review.getReview()));

        }

    }

}
