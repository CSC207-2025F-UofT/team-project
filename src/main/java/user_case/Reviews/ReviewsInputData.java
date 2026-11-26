package user_case.Reviews;

public class ReviewsInputData {
    private final int movieId;

    public ReviewsInputData(int movieId){
        this.movieId = movieId;
    }
    public int getMovieId(){
        return movieId;
    }
}
