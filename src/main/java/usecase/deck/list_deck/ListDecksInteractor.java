package usecase.deck.list_deck;

import data_access.DeckDataAccess;
import entity.FlashcardDeck;
import usecase.deck.create_deck.CreateDeckOutputData;

import java.util.ArrayList;
import java.util.List;

public class ListDecksInteractor implements  ListDecksInputBoundary {
    private final DeckDataAccess deckDAO;
    private final ListDecksOutputBoundary presenter;

    public ListDecksInteractor(DeckDataAccess deckDAO,
                               ListDecksOutputBoundary presenter) {
        this.deckDAO = deckDAO;
        this.presenter = presenter;
    }

    @Override
    public void listForUser(int userId) {

        // Make sure the "Don't know" deck exists
        int dkId = deckDAO.findOrCreateDontKnowDeckId(userId);

        List<FlashcardDeck> decks = deckDAO.findByUser(userId);

        // Sort so that "Don't know" deck comes first and others follow in alphabetical order by title
        decks.sort((a, b) -> {
            int aKey = (a.getId() == dkId) ? 0 : 1;
            int bKey = (b.getId() == dkId) ? 0 : 1;
            if (aKey != bKey) return Integer.compare(aKey, bKey);
            // If both are same key, sort alphabetically by title
            return a.getTitle().compareToIgnoreCase(b.getTitle());
        });

        // Convert to output data format
        List<CreateDeckOutputData.DeckSummary> list = new ArrayList<>();
        for (FlashcardDeck d : decks) {
            list.add(new CreateDeckOutputData.DeckSummary(d.getId(), d.getTitle()));
        }
        presenter.present(new CreateDeckOutputData(list));
    }
}
