package use_case.rate_review;

import entity.Book;
import entity.Review;

/*
 * The rate and review interactor, the user may give a rating, review, or both.
 *
 */
public class RateReviewInteractor implements RateReviewInputBoundary{
    private final RateReviewDataAccessInterface dataAccessObject;
    private final RateReviewOutputBoundary rateReviewPresenter;
    /*
     * Constructs a RateReviewInteractor
     *
     * @param dataAccessObject provides access to book information
     * @param rateReviewOutputBoundary prepares the output for the user
     */
    public RateReviewInteractor(RateReviewDataAccessInterface dataAccessObject,
                                RateReviewOutputBoundary rateReviewOutputBoundary){
        this.dataAccessObject = dataAccessObject;
        this.rateReviewPresenter = rateReviewOutputBoundary;
    }

    /*
     * Saves the user's review and/or rating for a book on their bookshelf.
     *
     * Fails if the book is missing or not shelved, if both rating and review are not provided,
     * if rating is outside MIN_RATING to MAX_RATING. A new submission replaces any existing
     * review of the book.
     *
     * @param rateReviewInputData the book's open library ID, rating, and review text
     */
    @Override
    public void execute(RateReviewInputData rateReviewInputData) {
        final String openLibraryID = rateReviewInputData.getOpenLibraryID();
        final Book book = dataAccessObject.getBook(rateReviewInputData.getOpenLibraryID());
        final Integer reviewRating = rateReviewInputData.getRating();
        final String reviewText = rateReviewInputData.getReview() == null ? "" :
                rateReviewInputData.getReview().trim();

        // checks if book does not exist and is not shelved
        if (book == null|| !book.isShelved()){
            rateReviewPresenter.prepareFailView(openLibraryID
                    + " : Add book to bookshelf before reviewing it.");


        //checks if rating is valid
        } else if (reviewRating != null &&
                (reviewRating < Review.MIN_RATING || reviewRating > Review.MAX_RATING)){
            rateReviewPresenter.prepareFailView(openLibraryID
                    + " : Invalid rating, please enter number between "
                    + Review.MIN_RATING + " - " + Review.MAX_RATING );

        } else if (reviewRating == null && reviewText.isEmpty()) {
            // nothing entered: clear the book's review
            book.setReview(null);
            dataAccessObject.saveBook(book);
            rateReviewPresenter.prepareSuccessView(new RateReviewOutputData(book.getOpenLibraryId(),
                    null, ""));

        }else { // saves review to book
            final Review review = new Review(openLibraryID, reviewRating, reviewText);
            book.setReview(review);
            dataAccessObject.saveBook(book);
            rateReviewPresenter.prepareSuccessView(new RateReviewOutputData(book.getOpenLibraryId(),
                    review.getRating(), review.getReview()));

        }

    }

}
