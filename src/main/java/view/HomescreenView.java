package view;

import interface_adapter.get_all_decks.GetAllDecksController;
import interface_adapter.get_all_decks.GetAllDecksState;
import interface_adapter.get_all_decks.GetAllDecksViewModel;

import interface_adapter.get_all_decks.GetAllDecksViewModelListener;
import interface_adapter.get_deck.GetDeckController;

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

    private GetDeckController getDeckController = null;
    private GetAllDecksController getAllDecksController = null;

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
        title.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE_TITLE));
        title.setForeground(AppTheme.TEXT_PRIMARY);

        //error text for when there are no available decks
        getAllDecksErrorField.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
        getAllDecksErrorField.setForeground(AppTheme.TEXT_PRIMARY);
        getAllDecksErrorField.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(getAllDecksErrorField, "EMPTY");

        // list of decks
        JList<SwingDeck> deckList = new JList<>(deckListModel);
        deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deckList.setBackground(AppTheme.CARD_COLOR);
        deckList.setCellRenderer(new SwingDeckRenderer());

        // scroll pane
        JScrollPane scrollPane = new JScrollPane(deckList);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1));
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

        openButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        getDeckController.execute(deckList.getSelectedValue().getName());
                    }
                }
        );

        // footer panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footerPanel.setBackground(AppTheme.BG_COLOR);
        footerPanel.add(openButton);

        this.setLayout(new BorderLayout(100, 14));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setBackground(AppTheme.BG_COLOR);

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

            nameLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
            nameLabel.setForeground(AppTheme.TEXT_PRIMARY);

            countLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE_SMALL));
            countLabel.setBorder(new EmptyBorder(3, 10, 3, 10));

            add(nameLabel, BorderLayout.CENTER);
            add(countLabel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends SwingDeck> list, SwingDeck deck, int index,
                boolean isSelected, boolean cellHasFocus) {

            nameLabel.setText(deck.getName());
            countLabel.setText(deck.getCount() + " cards");

            if (isSelected) {
                setBackground(AppTheme.SELECTION_BG);
                nameLabel.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE));
                nameLabel.setForeground(AppTheme.PRIMARY_COLOR);
                countLabel.setBackground(AppTheme.PRIMARY_COLOR);
                countLabel.setForeground(AppTheme.PRIMARY_COLOR);
            } else {
                setBackground(AppTheme.CARD_COLOR);
                nameLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
                nameLabel.setForeground(AppTheme.TEXT_PRIMARY);
                countLabel.setBackground(AppTheme.BG_SECONDARY);
                countLabel.setForeground(AppTheme.TEXT_PRIMARY);
            }

            // spacing between rows
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BG_COLOR),
                    new EmptyBorder(10, 14, 10, 14)
            ));
            return this;
        }
    }

    // Creates and formats a button
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE_BUTTON));
        button.setForeground(Color.WHITE);
        button.setBackground(AppTheme.PRIMARY_COLOR);
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

        if (map != null) {
            for (Map.Entry<String, Integer> deck : map.entrySet()) {
                deckListModel.addElement(new SwingDeck(deck.getKey(), deck.getValue()));
            }
        }
    }

    public void setGetAllDecksController(GetAllDecksController getAllDecksController) {
        this.getAllDecksController = getAllDecksController;
    }

    public void setGetDeckController(GetDeckController getDeckController) {
        this.getDeckController = getDeckController;
    }

    public void initialExecuteGetAllDecks() {
        getAllDecksController.execute();
    }
}
