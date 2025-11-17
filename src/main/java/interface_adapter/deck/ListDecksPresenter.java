package interface_adapter.deck;

import usecase.deck.create_deck.CreateDeckOutputData;
import usecase.deck.list_deck.ListDecksOutputBoundary;
import usecase.deck.list_deck.ListDecksOutputData;

import java.util.ArrayList;
import java.util.List;

public class ListDecksPresenter implements ListDecksOutputBoundary {

    private final DeckMenuViewModel vm;

    public ListDecksPresenter(DeckMenuViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void present(ListDecksOutputData output) {
        List<DeckMenuViewModel.DeckTileVM> tiles = new ArrayList<>();
        for (ListDecksOutputData.DeckSummary s : output.getDecks()) {
            tiles.add(new DeckMenuViewModel.DeckTileVM(s.getId(), s.getTitle()));
        }
        vm.setErrorMessage(null);
        vm.setTiles(tiles);
    }
}



