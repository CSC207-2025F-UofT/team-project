package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.browse_pets.AgeFilterOption;
import interface_adapter.browse_pets.BrowseChoices;
import interface_adapter.browse_pets.BrowsePetsController;
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
    private static final int PHOTO_WIDTH = 200;
    private static final int PHOTO_HEIGHT = 150;
    private static final int PHOTO_CAPTION_HEIGHT = 20;
    private static final int CARD_WIDTH = 236;

    private final BrowsePetsController controller;
    private final GalleryViewModel viewModel;
    private final ViewPetDetailsController detailsController;
    private final JPanel cardGrid = new JPanel(new GridLayout(0, COLUMNS, GAP, GAP));
    private final JComboBox<AgeFilterOption> ageBox = new JComboBox<>(AgeFilterOption.values());
    private final JComboBox<SexFilterOption> sexBox = new JComboBox<>(SexFilterOption.values());
    private final JTextField breedField = new JTextField(12);
    private final JTextField colorField = new JTextField(12);
    private final JComboBox<String> hairLengthBox =
            new JComboBox<>(new String[]{"Any length", "Short", "Medium", "Long", "Hairless"});
    private boolean updating;
    private int columns = COLUMNS;
    private boolean showingCards;

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
        scrollPane.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                updateColumns(scrollPane.getViewport().getWidth());
            }
        });
    }

    /**
     * Fits as many cards per row as the width allows, so the cards keep their size when the window changes.
     */
    private void updateColumns(int width) {
        final int fitted = Math.max(1, (width - 2 * GAP) / (CARD_WIDTH + GAP));
        if (fitted != columns) {
            columns = fitted;
            if (showingCards) {
                cardGrid.setLayout(new GridLayout(0, columns, GAP, GAP));
                cardGrid.revalidate();
                cardGrid.repaint();
            }
        }
    }

    private JPanel buildFilterPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        panel.getAccessibleContext().setAccessibleName("Filters");

        addFilter(panel, "Breed", KeyEvent.VK_B, breedField);
        addFilter(panel, "Age", KeyEvent.VK_A, ageBox);
        addFilter(panel, "Sex", KeyEvent.VK_S, sexBox);
        addFilter(panel, "Color", KeyEvent.VK_O, colorField);
        addFilter(panel, "Hair length", KeyEvent.VK_H, hairLengthBox);
        ageBox.addActionListener(event -> applyFilters());
        sexBox.addActionListener(event -> applyFilters());
        hairLengthBox.addActionListener(event -> applyFilters());
        final DocumentListener textFilterListener = new DocumentListener() {
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
        };
        breedField.getDocument().addDocumentListener(textFilterListener);
        colorField.getDocument().addDocumentListener(textFilterListener);

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

    private static void addFilter(JPanel panel, String name, int mnemonic, JComponent control) {
        final JLabel label = new JLabel(name);
        label.setLabelFor(control);
        label.setDisplayedMnemonic(mnemonic);
        control.getAccessibleContext().setAccessibleName(name + " filter");
        control.setMaximumSize(new Dimension(Integer.MAX_VALUE, control.getPreferredSize().height));
        panel.add(label);
        panel.add(control);
        panel.add(Box.createVerticalStrut(GAP));
    }

    private void applyFilters() {
        if (updating) {
            return;
        }
        final BrowseChoices choices = new BrowseChoices();
        choices.setAge((AgeFilterOption) ageBox.getSelectedItem());
        choices.setSex((SexFilterOption) sexBox.getSelectedItem());
        choices.setBreed(breedField.getText());
        choices.setColor(colorField.getText());
        if (hairLengthBox.getSelectedIndex() > 0) {
            choices.setHairLength((String) hairLengthBox.getSelectedItem());
        }
        controller.browse(choices);
    }

    private void clearFilters() {
        updating = true;
        ageBox.setSelectedItem(AgeFilterOption.ANY);
        sexBox.setSelectedItem(SexFilterOption.ANY);
        breedField.setText("");
        colorField.setText("");
        hairLengthBox.setSelectedIndex(0);
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
            showingCards = false;
            cardGrid.setLayout(new GridLayout(1, 1));
            final JLabel empty = new JLabel("No cats match your filters. Try clearing some filters.");
            empty.getAccessibleContext().setAccessibleName("No results");
            cardGrid.add(empty);
        } else {
            showingCards = true;
            cardGrid.setLayout(new GridLayout(0, columns, GAP, GAP));
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
        card.setPreferredSize(new Dimension(CARD_WIDTH, 300));
        final JLabel photo = new JLabel();
        photo.setHorizontalAlignment(JLabel.CENTER);
        photo.setHorizontalTextPosition(JLabel.CENTER);
        photo.setVerticalTextPosition(JLabel.BOTTOM);
        final Dimension photoSize = new Dimension(PHOTO_WIDTH, PHOTO_HEIGHT + PHOTO_CAPTION_HEIGHT);
        photo.setPreferredSize(photoSize);
        photo.setMinimumSize(photoSize);
        photo.setMaximumSize(photoSize);
        photo.getAccessibleContext().setAccessibleName("Cat photo");
        ListingPhoto.showFilled(photo, data.getPhotoPath(), PHOTO_WIDTH, PHOTO_HEIGHT);
        final JLabel name = new JLabel(data.getName());
        name.setFont(name.getFont().deriveFont(Font.BOLD, 15f));
        card.add(photo);
        card.add(Box.createVerticalStrut(6));
        card.add(name);
        card.add(new JLabel(data.getSummary()));
        card.add(new JLabel(data.getStatus()));
        final JButton detailsButton = new JButton("View details");
        detailsButton.getAccessibleContext().setAccessibleName("View details for " + data.getName());
        detailsButton.addActionListener(event -> detailsController.show(data.getPetId()));
        card.add(Box.createVerticalStrut(6));
        card.add(detailsButton);
        for (Component component : card.getComponents()) {
            ((JComponent) component).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
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
