package user_case.Favorite;

/** Output boundary used by the favourite interactor to present its result. */
public interface FavoriteOutputBoundary {
    void prepareSuccessView(FavoriteOutputData outputData);

    void prepareFailView(String message);
}
