package usecase.deck.list_deck;

import usecase.deck.create_deck.CreateDeckOutputData;

public interface ListDecksOutputBoundary {
    void present(CreateDeckOutputData output);
}
