package interface_adapter.browse_pets;

import java.util.List;

/**
 * What the gallery screen shows: the cards for the cats that match the filters.
 */
public class GalleryState {
    private List<PetCardData> cards = List.of();

    /**
     * Returns the cards to show.
     *
     * @return the cards, empty if no pet matched
     */
    public List<PetCardData> getCards() {
        return cards;
    }

    /**
     * Replaces the cards.
     *
     * @param cards the new cards
     */
    public void setCards(List<PetCardData> cards) {
        this.cards = List.copyOf(cards);
    }
}
