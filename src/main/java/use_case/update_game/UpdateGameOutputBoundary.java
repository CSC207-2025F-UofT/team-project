package use_case.update_game;

/** Receives the result of a tick. Implemented by the presenter. */
public interface UpdateGameOutputBoundary {
    void present(UpdateGameOutputData outputData);
}
