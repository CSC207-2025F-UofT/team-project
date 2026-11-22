package user_case.MovieDetail;

public interface MovieDetailOutputBoundary {

    void prepareSuccessView(MovieDetailOutputData outputData);

    void prepareFailView(String errorMessage);
}