package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.browse_pets.PetCardData;

/**
 * The pet gallery screen: a scrollable grid of pet cards. The filter panel is added in a later PR.
 */
public class GalleryView extends JPanel implements PropertyChangeListener {
    /** Name this screen is registered under in the view manager. */
    public static final String VIEW_NAME = GalleryViewModel.VIEW_NAME;

    private static final int COLUMNS = 3;
    private static final int GAP = 12;

    private final BrowsePetsController controller;
    private final JPanel cardGrid = new JPanel(new GridLayout(0, COLUMNS, GAP, GAP));

    /**
     * Creates the gallery screen.
     *
     * @param controller starts the browse use case
     * @param viewModel the model whose cards this view draws
     */
    public GalleryView(BrowsePetsController controller, GalleryViewModel viewModel) {
        super(new BorderLayout());
        this.controller = controller;
        viewModel.addPropertyChangeListener(this);
        getAccessibleContext().setAccessibleName("Pet gallery");

        final JLabel title = new JLabel("Adoptable Cats");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        title.getAccessibleContext().setAccessibleName("Gallery heading");
        add(title, BorderLayout.NORTH);

        cardGrid.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        final JPanel gridHolder = new JPanel(new BorderLayout());
        gridHolder.add(cardGrid, BorderLayout.NORTH);
        add(new JScrollPane(gridHolder), BorderLayout.CENTER);
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
            cardGrid.setLayout(new GridLayout(0, COLUMNS, GAP, GAP));
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
                BorderFactory.createLineBorder(Color.GRAY),
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
        controller.browse(List.of());
    }
}
