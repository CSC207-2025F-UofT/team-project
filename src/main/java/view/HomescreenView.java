package view;

import interface_adapter.get_all_decks.GetAllDecksController;
import interface_adapter.get_all_decks.GetAllDecksState;
import interface_adapter.get_all_decks.GetAllDecksViewModel;

import interface_adapter.get_all_decks.GetAllDecksViewModelListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * The View for when the user is viewing all the flashcard decks.
 */
public class HomescreenView extends JPanel implements ActionListener, GetAllDecksViewModelListener {

    private final GetAllDecksViewModel getAllDecksViewModel;

    // Used for the scroll pane
    private final DefaultListModel<SwingDeck> deckListModel = new DefaultListModel<>();
    private final JLabel getAllDecksErrorField = new JLabel();
    private final CardLayout centerLayout = new CardLayout();
    private final JPanel centerPanel = new JPanel(centerLayout);

    private final JButton openButton;

    private GetAllDecksController getAllDecksController = null;

    // Colors
    private static final Color BG_COLOR = new Color(244, 247, 245); // Green Tinted Off-White
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(16, 185, 129); // Green
    private static final Color SELECTION_BG = new Color(236, 253, 245); // Light Green
    private static final Color TEXT_PRIMARY = new Color(20, 42, 34); // Dark Grey
    private static final Color BG_SECONDARY = new Color(225, 235, 230); // Light Gray
    private static final Color BORDER_COLOR = new Color(225, 235, 230); // Light Gray

    private static final String FONT_NAME = "Segoe UI";

    // Class used for swing JList
    static class SwingDeck {
        private final String name;
        private final int count;

        public SwingDeck(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() { return name; }
        public int getCount() { return count; }
    }

    public HomescreenView(GetAllDecksViewModel getAllDecksViewModel) {
        this.getAllDecksViewModel = getAllDecksViewModel;
        this.getAllDecksViewModel.addListener(this);

        //title text
        final JLabel title = new JLabel("Select Flashcard Deck");
        title.setFont(new Font(FONT_NAME, Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);

        //error text for when there are no available decks
        getAllDecksErrorField.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
        getAllDecksErrorField.setForeground(TEXT_PRIMARY);
        getAllDecksErrorField.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(getAllDecksErrorField, "EMPTY");

        // list of decks
        JList<SwingDeck> deckList = new JList<>(deckListModel);
        deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deckList.setBackground(CARD_COLOR);
        deckList.setCellRenderer(new SwingDeckRenderer());

        // scroll pane
        JScrollPane scrollPane = new JScrollPane(deckList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12); // makes scrolling smoother
        scrollPane.setVisible(false);
        centerPanel.add(scrollPane, "LIST");

        // open button
        openButton = createButton("Open");
        openButton.setEnabled(false); // Disable button until an item is selected

        // Enable openButton when a Deck is selected
        deckList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                openButton.setEnabled(deckList.getSelectedValue() != null);
            }
        });

        // footer panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footerPanel.setBackground(BG_COLOR);
        footerPanel.add(openButton);

        this.setLayout(new BorderLayout(100, 14));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setBackground(BG_COLOR);

        centerLayout.show(centerPanel, "LIST");

        this.add(title, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);
    }

    // Sets the format of each Deck in the list
    static class SwingDeckRenderer extends JPanel implements ListCellRenderer<SwingDeck> {
        private final JLabel nameLabel = new JLabel();
        private final JLabel countLabel = new JLabel();

        public SwingDeckRenderer() {
            setLayout(new BorderLayout(200, 0));
            setBorder(new EmptyBorder(10, 14, 10, 14));

            nameLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
            nameLabel.setForeground(TEXT_PRIMARY);

            countLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
            countLabel.setBorder(new EmptyBorder(3, 10, 3, 10));

            add(nameLabel, BorderLayout.CENTER);
            add(countLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends SwingDeck> list, SwingDeck deck, int index,
                boolean isSelected, boolean cellHasFocus) {

            nameLabel.setText(deck.getName());
            countLabel.setText(String.valueOf(deck.getCount()) + " cards");

            if (isSelected) {
                setBackground(SELECTION_BG);
                nameLabel.setFont(new Font(FONT_NAME, Font.BOLD, 14));
                nameLabel.setForeground(PRIMARY_COLOR);
                countLabel.setBackground(PRIMARY_COLOR);
                countLabel.setForeground(PRIMARY_COLOR);
            } else {
                setBackground(CARD_COLOR);
                nameLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
                nameLabel.setForeground(TEXT_PRIMARY);
                countLabel.setBackground(BG_SECONDARY);
                countLabel.setForeground(TEXT_PRIMARY);
            }

            // spacing between rows
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BG_COLOR),
                    new EmptyBorder(10, 14, 10, 14)
            ));
            return this;
        }
    }

    // Creates and formats a button
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void stateChanged() {
        if (getAllDecksViewModel.getState().getGetAllDecksError() != null) {
            getAllDecksErrorField.setText(getAllDecksViewModel.getState().getGetAllDecksError());
            centerLayout.show(centerPanel, "EMPTY");
        } else {
            setFields(getAllDecksViewModel.getState());
            centerLayout.show(centerPanel, "LIST");
        }
    }

    private void setFields(GetAllDecksState state) {
        Map<String, Integer> map = state.getDecks();
        for (Map.Entry<String,Integer> entry : map.entrySet()) {
            deckListModel.addElement(new SwingDeck(entry.getKey(), entry.getValue()));
        }
    }

    public void setGetAllDecksController(GetAllDecksController getAllDecksController) {
        this.getAllDecksController = getAllDecksController;
    }

    public void initialExecuteGetAllDecks() {
        getAllDecksController.execute();
    }
}
