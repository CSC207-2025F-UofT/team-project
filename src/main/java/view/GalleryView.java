package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import entity.Species;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.browse_pets.PetCardData;

/**
 * The pet gallery screen: a species filter on top and a scrollable grid of pet cards.
 */
public class GalleryView extends JPanel implements PropertyChangeListener {
    /** Name this screen is registered under in the view manager. */
    public static final String VIEW_NAME = "gallery";

    private static final String ALL = "All pets";
    private static final String CATS = "Cats";
    private static final String DOGS = "Dogs";

    private final BrowsePetsController controller;
    private final JPanel cardGrid = new JPanel(new GridLayout(0, 3, 12, 12));

    /**
     * Creates the gallery screen.
     *
     * @param controller starts the browse use case when the filter changes
     * @param viewModel the model whose cards this view draws
     */
    public GalleryView(BrowsePetsController controller, GalleryViewModel viewModel) {
        super(new BorderLayout());
        this.controller = controller;
        viewModel.addPropertyChangeListener(this);
        getAccessibleContext().setAccessibleName("Pet gallery");

        final JComboBox<String> speciesBox = new JComboBox<>(new String[] {ALL, CATS, DOGS});
        speciesBox.getAccessibleContext().setAccessibleName("Species filter");
        speciesBox.addActionListener(event -> controller.browse(selectedSpecies(speciesBox)));
        final JLabel speciesLabel = new JLabel("Show:");
        speciesLabel.setLabelFor(speciesBox);
        speciesLabel.setDisplayedMnemonic(KeyEvent.VK_S);

        final JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterBar.add(speciesLabel);
        filterBar.add(speciesBox);
        add(filterBar, BorderLayout.NORTH);

        cardGrid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        final JPanel gridHolder = new JPanel(new BorderLayout());
        gridHolder.add(cardGrid, BorderLayout.NORTH);
        add(new JScrollPane(gridHolder), BorderLayout.CENTER);
    }

    private Species selectedSpecies(JComboBox<String> box) {
        final Object choice = box.getSelectedItem();
        if (CATS.equals(choice)) {
            return Species.CAT;
        }
        if (DOGS.equals(choice)) {
            return Species.DOG;
        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (GalleryViewModel.CARDS.equals(event.getPropertyName())) {
            @SuppressWarnings("unchecked")
            final List<PetCardData> cards = (List<PetCardData>) event.getNewValue();
            showCards(cards);
        }
    }

    private void showCards(List<PetCardData> cards) {
        cardGrid.removeAll();
        if (cards.isEmpty()) {
            cardGrid.setLayout(new GridLayout(1, 1));
            cardGrid.add(new JLabel("No pets match your filters."));
        } else {
            cardGrid.setLayout(new GridLayout(0, 3, 12, 12));
            for (PetCardData card : cards) {
                cardGrid.add(buildCard(card));
            }
        }
        cardGrid.revalidate();
        cardGrid.repaint();
    }

    private JPanel buildCard(PetCardData data) {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(java.awt.Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        card.setPreferredSize(new Dimension(220, 110));
        card.add(new JLabel(data.getName()));
        card.add(new JLabel(data.getSummary()));
        card.add(new JLabel(data.getStatus()));
        card.getAccessibleContext().setAccessibleName("Pet card: " + data.getName());
        card.getAccessibleContext().setAccessibleDescription(data.getSummary() + ". " + data.getStatus());
        return card;
    }

    /**
     * Loads the gallery with no filters, used when the screen first opens.
     */
    public void loadAll() {
        controller.browse(null);
    }
}
