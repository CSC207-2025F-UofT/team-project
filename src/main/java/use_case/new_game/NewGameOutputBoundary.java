package use_case.new_game;

/**
 * Implemented by the NewGamePresenter. Starting a new game cannot fail in
 * Story 2, so there is only a success view; a fail view can be added when a
 * failure case exists.
 */
public interface NewGameOutputBoundary {

    void prepareSuccessView(NewGameOutputData outputData);
}
