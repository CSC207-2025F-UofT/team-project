package use_case.watchlist;

public interface WatchlistOutputBoundary {

    void prepareSuccessView(WatchListOutputdata outputdata);

    void prepareFailView(String errorMessage);

}
