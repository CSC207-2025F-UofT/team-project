package Reviews;
import data_access.GetMovieData;
import data_access.ReviewDataAccess;
import entity.MovieReview;
import java.io.IOException;
import java.util.List;

public class ReviewDataAccesManualTest {
    public static void main(String[] args){
        String token = System.getenv("TMDB_ACCESS_TOKEN");
        GetMovieData movieData = new GetMovieData(token);
        ReviewDataAccess reviewDataAccess = new ReviewDataAccess(movieData);
        try{
            List<MovieReview> reviews = reviewDataAccess.getReviews(550);

            System.out.println("Total reviews: " + reviews.size());

            for(MovieReview review: reviews){
                System.out.println("Author: " + review.getAuthor());
                System.out.println("Rating: " + review.getRating());
                System.out.println("Content: " + review.getContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
