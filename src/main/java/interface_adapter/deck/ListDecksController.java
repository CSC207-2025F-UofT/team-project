package interface_adapter.deck;

import usecase.deck.list_deck.ListDecksInputBoundary;

public class ListDecksController {
    private final ListDecksInputBoundary interactor;

    public ListDecksController(ListDecksInputBoundary interactor) {
        this.interactor = interactor;
    }

    // Triggered when the user enters the deck menu
    public void onEnterDeckMenu(int userId) {
        interactor.listForUser(userId);
    }
}
