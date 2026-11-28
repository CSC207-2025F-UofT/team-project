package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import entity.Temperament;
import interface_adapter.browse_pets.AgeFilterOption;
import interface_adapter.browse_pets.BrowseChoices;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.FilterLabels;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.browse_pets.GalleryViewModelListener;
import interface_adapter.browse_pets.PetCardData;
import interface_adapter.browse_pets.SexFilterOption;
import interface_adapter.view_pet_details.ViewPetDetailsController;

/**
 * The pet gallery screen: a filter panel on the left and a scrollable grid of pet cards.
 */
public class GalleryView extends JPanel implements GalleryViewModelListener {
    /** Name this screen is registered under in the view manager. */
    public static final String VIEW_NAME = GalleryViewModel.VIEW_NAME;

    private static final int COLUMNS = 3;
    private static final int GAP = 12;

    private final BrowsePetsController controller;
    private final GalleryViewModel viewModel;
    private final ViewPetDetailsController detailsController;
    private final JPanel cardGrid = new JPanel(new GridLayout(0, COLUMNS, GAP, GAP));
    private final JComboBox<AgeFilterOption> ageBox = new JComboBox<>(AgeFilterOption.values());
    private final Map<Temperament, JCheckBox> temperamentBoxes = new EnumMap<>(Temperament.class);
    private final JComboBox<SexFilterOption> sexBox = new JComboBox<>(SexFilterOption.values());
    private final JTextField breedField = new JTextField(12);
    private final JCheckBox noMedicalNeedsBox = new JCheckBox("Only cats with no medical needs");
    private final JCheckBox vaccinatedBox = new JCheckBox("Only vaccinated cats");
    private final JCheckBox spayedNeuteredBox = new JCheckBox("Only spayed or neutered cats");
    private boolean updating;

    /**
     * Creates the gallery screen.
     *
     * @param controller starts the browse use case when a filter changes
     * @param viewModel the model whose cards this view draws
     * @param detailsController opens the detail page when a card is chosen
     */
    public GalleryView(BrowsePetsController controller, GalleryViewModel viewModel,
                       ViewPetDetailsController detailsController) {
        super(new BorderLayout());
        this.controller = controller;
        this.detailsController = detailsController;
        this.viewModel = viewModel;
        viewModel.addListener(this);
        getAccessibleContext().setAccessibleName("Pet gallery");

        final JLabel title = new JLabel("Adoptable Cats");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        title.getAccessibleContext().setAccessibleName("Gallery heading");
        add(title, BorderLayout.NORTH);

        final JScrollPane filterScroll = new JScrollPane(buildFilterPanel());
        filterScroll.setBorder(BorderFactory.createEmptyBorder());
        filterScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(filterScroll, BorderLayout.WEST);

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
        final JLabel sexLabel = new JLabel("Sex");
        sexLabel.setLabelFor(sexBox);
        sexLabel.setDisplayedMnemonic(KeyEvent.VK_S);
        sexBox.getAccessibleContext().setAccessibleName("Sex filter");
        sexBox.addActionListener(event -> applyFilters());
        sexBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, sexBox.getPreferredSize().height));
        panel.add(sexLabel);
        panel.add(sexBox);

        panel.add(Box.createVerticalStrut(GAP));
        final JLabel breedLabel = new JLabel("Breed (part of the name)");
        breedLabel.setLabelFor(breedField);
        breedLabel.setDisplayedMnemonic(KeyEvent.VK_B);
        breedField.getAccessibleContext().setAccessibleName("Breed filter");
        breedField.setMaximumSize(new Dimension(Integer.MAX_VALUE, breedField.getPreferredSize().height));
        breedField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                applyFilters();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                applyFilters();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                applyFilters();
            }
        });
        panel.add(breedLabel);
        panel.add(breedField);

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
        vaccinatedBox.setMnemonic(KeyEvent.VK_V);
        vaccinatedBox.getAccessibleContext().setAccessibleName("Only vaccinated cats");
        vaccinatedBox.addActionListener(event -> applyFilters());
        panel.add(vaccinatedBox);
        spayedNeuteredBox.setMnemonic(KeyEvent.VK_N);
        spayedNeuteredBox.getAccessibleContext().setAccessibleName("Only spayed or neutered cats");
        spayedNeuteredBox.addActionListener(event -> applyFilters());
        panel.add(spayedNeuteredBox);

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
        final BrowseChoices choices = new BrowseChoices();
        choices.setAge((AgeFilterOption) ageBox.getSelectedItem());
        choices.setSex((SexFilterOption) sexBox.getSelectedItem());
        choices.setBreed(breedField.getText());
        choices.setTemperaments(chosen);
        choices.setNoMedicalNeedsOnly(noMedicalNeedsBox.isSelected());
        choices.setVaccinatedOnly(vaccinatedBox.isSelected());
        choices.setSpayedNeuteredOnly(spayedNeuteredBox.isSelected());
        controller.browse(choices);
    }

    private void clearFilters() {
        updating = true;
        ageBox.setSelectedItem(AgeFilterOption.ANY);
        sexBox.setSelectedItem(SexFilterOption.ANY);
        breedField.setText("");
        for (JCheckBox box : temperamentBoxes.values()) {
            box.setSelected(false);
        }
        noMedicalNeedsBox.setSelected(false);
        vaccinatedBox.setSelected(false);
        spayedNeuteredBox.setSelected(false);
        updating = false;
        applyFilters();
    }

    @Override
    public void stateChanged() {
        showCards(viewModel.getState().getCards());
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
        card.setPreferredSize(new Dimension(180, 205));
        final JLabel photo = new JLabel();
        photo.setHorizontalTextPosition(JLabel.CENTER);
        photo.setVerticalTextPosition(JLabel.BOTTOM);
        photo.getAccessibleContext().setAccessibleName("Cat photo");
        ListingPhoto.show(photo, data.getPhotoPath(), 140, 85);
        card.add(photo);
        card.add(new JLabel(data.getName()));
        card.add(new JLabel(data.getSummary()));
        card.add(new JLabel(data.getStatus()));
        final JButton detailsButton = new JButton("View details");
        detailsButton.getAccessibleContext().setAccessibleName("View details for " + data.getName());
        detailsButton.addActionListener(event -> detailsController.show(data.getPetId()));
        card.add(Box.createVerticalStrut(6));
        card.add(detailsButton);
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                detailsController.show(data.getPetId());
            }
        });
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
