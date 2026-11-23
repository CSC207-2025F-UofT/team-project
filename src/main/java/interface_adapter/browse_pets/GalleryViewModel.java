package interface_adapter.browse_pets;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * What the gallery screen shows. The view listens for changes to the cards.
 */
public class GalleryViewModel {
    /** Name of the property fired when the list of cards changes. */
    public static final String CARDS = "cards";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
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
     * Replaces the cards and notifies listeners.
     *
     * @param cards the new cards
     */
    public void setCards(List<PetCardData> cards) {
        final List<PetCardData> old = this.cards;
        this.cards = List.copyOf(cards);
        support.firePropertyChange(CARDS, old, this.cards);
    }

    /**
     * Registers a listener for changes.
     *
     * @param listener the listener to notify
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
