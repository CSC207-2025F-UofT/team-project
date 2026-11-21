package data_access;
import entity.MovieReview;
import user_case.Reviews.ReviewsDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ReviewDataAccess implements ReviewsDataAccessInterface {
    private final GetMovieData movieData;
    public ReviewDataAccess(GetMovieData movieData){
        this.movieData = movieData;
    }
    @Override
    public List<MovieReview> getReviews(int movieId) throws IOException{
        // Get JSON from TMDB
        String json = movieData.get("movie/" + movieId + "/reviews");
        // convert String to JSONObject
        JSONObject response = new JSONObject(json);
        // get list of reviews
        JSONArray results = response.getJSONArray("results");
        List<MovieReview> reviews = new ArrayList<>();

        //read each review
        for (int i = 0; i < results.length(); i++){
            JSONObject review = results.getJSONObject(i);
            String author = review.getString("author");
            String content = review.getString("content");

            JSONObject authorDetails = review.optJSONObject("author_details");

            Double rating = null;

            if (authorDetails != null && !authorDetails.isNull("rating")) {
                rating = authorDetails.getDouble("rating");
            }

            //create MovieReview object
            MovieReview movieReview =
                    new MovieReview(author, content, rating);

            reviews.add(movieReview);
        }
        //return the reviews
        return reviews;
        }
    }

