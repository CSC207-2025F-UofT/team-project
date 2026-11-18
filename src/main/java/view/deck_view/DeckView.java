package view.deck_view;

import interface_adapter.get_all_decks.GetAllDecksController;
import interface_adapter.get_deck.GetDeckController;
import interface_adapter.get_deck.deck_view.GetDeckState;
import interface_adapter.get_deck.deck_view.GetDeckViewModel;
import interface_adapter.get_deck.deck_view.GetDeckViewModelListener;
import view.AppTheme;
import view.FormattedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.List;

/**
 * The View for when the user is viewing a Deck.
 */
public class DeckView extends JPanel implements GetDeckViewModelListener {

    private final GetDeckViewModel getDeckViewModel;
    private final DefaultListModel<SwingDeck> deckModel = new DefaultListModel<>();

    private final JLabel deckName = new JLabel();
    private final JLabel cardCount = new JLabel();
    private final JLabel emptyDeckError = new JLabel();
    private final JButton study = new FormattedButton("Study");

    private final CardLayout flashcardLayout = new CardLayout();
    private final JPanel flashcardPanel = new JPanel(flashcardLayout);

    private GetDeckController getDeckController = null;
    private GetAllDecksController getAllDecksController = null;

    public DeckView(GetDeckViewModel getDeckViewModel) {
        this.getDeckViewModel = getDeckViewModel;
        this.getDeckViewModel.addListener(this);

        createDeckView();
    }

    // gui for deck view which lists the flashcards
    private void createDeckView() {
        this.setLayout(new BorderLayout(14, 14));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setBackground(AppTheme.BG_COLOR);

        this.add(createCenterPanel(), BorderLayout.CENTER);
        this.add(createToolBar(), BorderLayout.WEST);
    }

    public JPanel createToolBar() {
        // buttons
        JButton home = new FormattedButton();
        home.addActionListener(evt -> getAllDecksController.execute());
        study.addActionListener(evt -> getDeckController.switchToStudyView());

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);
        buttons.add(home);
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(study);

        // toolBar
        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);
        toolBar.add(buttons, BorderLayout.NORTH);
        return toolBar;
    }

    public JPanel createCenterPanel() {
        // error text
        emptyDeckError.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
        emptyDeckError.setForeground(AppTheme.TEXT_PRIMARY);
        emptyDeckError.setHorizontalAlignment(SwingConstants.CENTER);

        // title panel
        deckName.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE_TITLE));
        deckName.setForeground(AppTheme.TEXT_PRIMARY);
        cardCount.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
        cardCount.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
        cardCount.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(deckName);
        titlePanel.add(cardCount);

        flashcardPanel.setOpaque(false);
        flashcardPanel.add(createScrollPane(), "DECK");
        flashcardPanel.add(emptyDeckError, "EMPTY");

        JPanel centerPanel = new JPanel(new BorderLayout(14, 14));
        centerPanel.setOpaque(false);
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(flashcardPanel, BorderLayout.CENTER);

        return centerPanel;
    }

    public JScrollPane createScrollPane() {
        // list of flashcards
        JList<SwingDeck> flashcards = new JList<>(deckModel) {
            // used so JList will wrap text and resize with the window
            @Override
            public boolean getScrollableTracksViewportWidth () { return true; }
        };
        flashcards.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flashcards.setBackground(AppTheme.BG_COLOR);
        flashcards.setCellRenderer(new SwingDeckRenderer());

        // Forces JList to recalculate row height when window is resized
        flashcards.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                flashcards.setFixedCellHeight(10);  // dummy value
                flashcards.setFixedCellHeight(-1);  // makes it recalculate height
            }
        });

        // scroll pane
        JScrollPane scrollPane = new JScrollPane(flashcards);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12); // makes scrolling smoother
        scrollPane.setBorder(null);

        return scrollPane;
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
        GetDeckState state = getDeckViewModel.getState();

        // give error pop up if the deck could not be retrieved
        if (state.getDeckError() != null) {
            JOptionPane.showMessageDialog(this, getDeckViewModel.getState().getDeckError());
            return;
        }

        deckName.setText(state.getDeckName());
        cardCount.setText(state.getFlashcardCountText());
        deckModel.clear();

        if (state.getDeckEmptyError() != null) {
            emptyDeckError.setText(state.getDeckEmptyError());
            study.setEnabled(false);
            flashcardLayout.show(flashcardPanel, "EMPTY");
        } else {
            updateDeckModel(state.getFlashcards());
            study.setEnabled(true);
            flashcardLayout.show(flashcardPanel, "DECK");
        }
    }

    private void updateDeckModel(List<Map<String, String>> list) {
        for (Map<String, String> card : list) {
            for (Map.Entry<String, String> info : card.entrySet()) {
                deckModel.addElement(new SwingDeck(info.getKey(), info.getValue()));
            }
        }
    }

    public void setGetDeckController(GetDeckController getDeckController) {
        this.getDeckController = getDeckController;
    }

    public void setGetAllDecksController(GetAllDecksController getAllDecksController) {
        this.getAllDecksController = getAllDecksController;
    }
}
