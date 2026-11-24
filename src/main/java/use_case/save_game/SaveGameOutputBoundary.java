package use_case.save_game;

public interface SaveGameOutputBoundary {
    void prepareSaveSuccessView();

    void prepareNameView();

    void prepareFailView(String message);
}
