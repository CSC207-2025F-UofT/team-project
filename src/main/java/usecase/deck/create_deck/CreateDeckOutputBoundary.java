package usecase.deck.create_deck;

public interface CreateDeckOutputBoundary {
    void prepareSuccess(CreateDeckOutputData outputData);

    void prepareFail(String errorMessage);
}
