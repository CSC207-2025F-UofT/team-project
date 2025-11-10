package interface_adapter.deck;

import usecase.deck.create_deck.CreateDeckOutputData;
import usecase.deck.list_deck.ListDecksOutputBoundary;

import java.util.ArrayList;
import java.util.List;

public class ListDecksPresenter implements ListDecksOutputBoundary {

    private final DeckMenuViewModel vm;

    public ListDecksPresenter(DeckMenuViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void present(CreateDeckOutputData output) {
        List<DeckMenuViewModel.DeckTileVM> tiles = new ArrayList<>();
        for (CreateDeckOutputData.DeckSummary s : output.getDeckSummaries()) {
            tiles.add(new DeckMenuViewModel.DeckTileVM(s.deckId, s.title));
        }
        vm.setErrorMessage(null);
        vm.setTiles(tiles);
    }
}
