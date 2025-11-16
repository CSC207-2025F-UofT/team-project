package interface_adapter.deck;

import usecase.deck.create_deck.CreateDeckInputBoundary;
import usecase.deck.create_deck.CreateDeckInputData;

public class CreateDeckController {

    private final CreateDeckInputBoundary interactor;
    private final int userId;

    public CreateDeckController(CreateDeckInputBoundary interactor,
                                int userId) {
        this.interactor = interactor;
        this.userId = userId;
    }

    // Triggered when the user wants to create a new deck
    public void onCreate(String title) {
        CreateDeckInputData in = new CreateDeckInputData(userId, title);
        interactor.createDeck(in);
    }
}
