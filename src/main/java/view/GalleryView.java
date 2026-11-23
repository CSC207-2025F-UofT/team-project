package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import entity.Temperament;
import interface_adapter.browse_pets.AgeFilterOption;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.FilterLabels;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.browse_pets.PetCardData;

/**
 * The pet gallery screen: a filter panel on the left and a scrollable grid of pet cards.
 */
public class GalleryView extends JPanel implements PropertyChangeListener {
    /** Name this screen is registered under in the view manager. */
    public static final String VIEW_NAME = "gallery";

    private static final int COLUMNS = 3;
    private static final int GAP = 12;

    private final BrowsePetsController controller;
    private final JPanel cardGrid = new JPanel(new GridLayout(0, COLUMNS, GAP, GAP));
    private final JComboBox<AgeFilterOption> ageBox = new JComboBox<>(AgeFilterOption.values());
    private final Map<Temperament, JCheckBox> temperamentBoxes = new EnumMap<>(Temperament.class);
    private final JCheckBox noMedicalNeedsBox = new JCheckBox("Only cats with no medical needs");
    private boolean updating;

    /**
     * Creates the gallery screen.
     *
     * @param controller starts the browse use case when a filter changes
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

        add(buildFilterPanel(), BorderLayout.WEST);

        cardGrid.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        final JPanel gridHolder = new JPanel(new BorderLayout());
        gridHolder.add(cardGrid, BorderLayout.NORTH);
        final JScrollPane scrollPane = new JScrollPane(gridHolder);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel buildFilterPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        panel.getAccessibleContext().setAccessibleName("Filters");

        final JLabel ageLabel = new JLabel("Age");
        ageLabel.setLabelFor(ageBox);
        ageLabel.setDisplayedMnemonic(KeyEvent.VK_A);
        ageBox.getAccessibleContext().setAccessibleName("Age filter");
        ageBox.addActionListener(event -> applyFilters());
        ageBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, ageBox.getPreferredSize().height));
        panel.add(ageLabel);
        panel.add(ageBox);

        panel.add(Box.createVerticalStrut(GAP));
        final JLabel temperamentLabel = new JLabel("Temperament (must have all)");
        panel.add(temperamentLabel);
        for (Temperament temperament : Temperament.values()) {
            final JCheckBox box = new JCheckBox(FilterLabels.temperament(temperament));
            box.getAccessibleContext().setAccessibleName("Temperament: " + FilterLabels.temperament(temperament));
            box.addActionListener(event -> applyFilters());
            temperamentBoxes.put(temperament, box);
            panel.add(box);
        }

        panel.add(Box.createVerticalStrut(GAP));
        noMedicalNeedsBox.setMnemonic(KeyEvent.VK_M);
        noMedicalNeedsBox.getAccessibleContext().setAccessibleName("Only cats with no medical needs");
        noMedicalNeedsBox.addActionListener(event -> applyFilters());
        panel.add(noMedicalNeedsBox);

        panel.add(Box.createVerticalStrut(GAP));
        final JButton clearButton = new JButton("Clear filters");
        clearButton.setMnemonic(KeyEvent.VK_C);
        clearButton.getAccessibleContext().setAccessibleName("Clear filters");
        clearButton.addActionListener(event -> clearFilters());
        panel.add(clearButton);
        for (Component component : panel.getComponents()) {
            ((JComponent) component).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        return panel;
    }

    private void applyFilters() {
        if (updating) {
            return;
        }
        final Set<Temperament> chosen = EnumSet.noneOf(Temperament.class);
        for (Map.Entry<Temperament, JCheckBox> entry : temperamentBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                chosen.add(entry.getKey());
            }
        }
        controller.browse((AgeFilterOption) ageBox.getSelectedItem(), chosen, noMedicalNeedsBox.isSelected());
    }

    private void clearFilters() {
        updating = true;
        ageBox.setSelectedItem(AgeFilterOption.ANY);
        for (JCheckBox box : temperamentBoxes.values()) {
            box.setSelected(false);
        }
        noMedicalNeedsBox.setSelected(false);
        updating = false;
        applyFilters();
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
            final JLabel empty = new JLabel("No cats match your filters. Try clearing some filters.");
            empty.getAccessibleContext().setAccessibleName("No results");
            cardGrid.add(empty);
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
        card.setPreferredSize(new Dimension(180, 110));
        card.add(new JLabel(data.getName()));
        card.add(new JLabel(data.getSummary()));
        card.add(new JLabel(data.getStatus()));
        card.getAccessibleContext().setAccessibleName("Pet card: " + data.getName());
        card.getAccessibleContext().setAccessibleDescription(data.getSummary() + ". " + data.getStatus());
        return card;
    }

    /**
     * Loads the gallery with whatever the filters currently say, used when the screen first opens.
     */
    public void loadAll() {
        applyFilters();
    }
}
