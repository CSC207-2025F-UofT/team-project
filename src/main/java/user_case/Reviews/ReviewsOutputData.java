package user_case.Reviews;

import entity.MovieReview;
import java.util.List;

/**
 * output data for Reviews use case
 */

public class ReviewsOutputData {
    private final List<MovieReview> reviews;

    public ReviewsOutputData(List<MovieReview> reviews){
        this.reviews = reviews;
    }
    public List<MovieReview> getReviews(){
        return reviews;
    }
}
