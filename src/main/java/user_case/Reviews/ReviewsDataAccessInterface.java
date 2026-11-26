package user_case.Reviews;

import entity.MovieReview;
import java.util.List;
import java.io.IOException;

/**
 * interface for retrieving movie reviews
 */

public interface ReviewsDataAccessInterface {
    List<MovieReview> getReviews(int movieId)
        throws IOException;
}
