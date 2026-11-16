package view;

import interface_adapter.get_deck.GetDeckController;
import interface_adapter.get_deck.GetDeckState;
import interface_adapter.get_deck.GetDeckViewModel;
import interface_adapter.get_deck.GetDeckViewModelListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.List;

/**
 * The View for when the user is viewing a Deck.
 */
public class DeckView extends JPanel implements ActionListener, GetDeckViewModelListener {

    private final GetDeckViewModel getDeckViewModel;

    private final DefaultListModel<DeckView.SwingDeck> deckModel = new DefaultListModel<>();

    private final JLabel deckName = new JLabel();

    private final CardLayout layout = new CardLayout();

    private int currentIndex = 0;
    private boolean isFlipped = false;

    private final JLabel flashcardLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel flashcard = new JPanel(new BorderLayout());
    private final JLabel whichSideLabel = new JLabel("QUESTION", SwingConstants.CENTER);
    private final JLabel currentIndexLabel = new JLabel("", SwingConstants.CENTER);

    private GetDeckController getDeckController = null;

    public DeckView(GetDeckViewModel getDeckViewModel) {
        this.getDeckViewModel = getDeckViewModel;
        this.getDeckViewModel.addListener(this);

        // deck name text
        deckName.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE_TITLE));
        deckName.setForeground(AppTheme.TEXT_PRIMARY);

        JPanel deckView = createDeckView();
        JPanel studyView = createStudyView();

        this.setLayout(layout);
        this.add(deckView, "deckView");
        this.add(studyView, "studyView");

        layout.show(this, "deckView");

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
        if (getDeckViewModel.getState().getDeckError() != null) {
            JOptionPane.showMessageDialog(this, getDeckViewModel.getState().getDeckError());
        } else {
            layout.show(this, "deckView");
            deckModel.clear();
            setFields(getDeckViewModel.getState());
            updateCardDisplay();
        }
    }

    private void setFields(GetDeckState state) {
        deckName.setText(state.getDeckName());
        List<Map<String, String>> list = state.getFlashcards();

        if (list != null) {
            for (Map<String, String> flashcard : list) {
                for (Map.Entry<String, String> f : flashcard.entrySet()) {
                    deckModel.addElement(new DeckView.SwingDeck(f.getKey(), f.getValue()));
                }
            }
        }
    }

    public void setGetDeckController(GetDeckController getDeckController) {
        this.getDeckController = getDeckController;
    }

    // gui for deck view which lists the flashcards
    private JPanel createDeckView() {
        // list of flashcards
        JList<SwingDeck> flashcards = new JList<>(deckModel) {
            // used so JList will wrap text and resize with the window
            @Override
            public boolean getScrollableTracksViewportWidth () { return true; }
        };

        flashcards.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flashcards.setBackground(AppTheme.BG_COLOR);
        flashcards.setCellRenderer(new DeckView.SwingDeckRenderer());

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

        // buttons
        JButton home = new FormattedButton();

        JButton study = new FormattedButton("Study");

        home.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        getDeckController.switchToHomescreenView();
                    }
                }
        );

        DeckView viewPanel = this;
        study.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        layout.show(viewPanel, "studyView");
                    }
                }
        );

        JPanel deckView = new JPanel();
        deckView.setLayout(new BorderLayout(14, 14));
        deckView.setBorder(new EmptyBorder(20, 20, 20, 20));
        deckView.setBackground(AppTheme.BG_COLOR);

        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);
        buttons.add(home);
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(study);
        toolBar.add(buttons, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 14));
        centerPanel.setOpaque(false);
        centerPanel.add(deckName, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        deckView.add(centerPanel, BorderLayout.CENTER);
        deckView.add(toolBar, BorderLayout.WEST);
        return deckView;
    }

    private JPanel createStudyView() {
        JPanel studyView = new JPanel();
        studyView.setLayout(new BorderLayout(14, 14));
        studyView.setBorder(new EmptyBorder(20, 20, 20, 20));
        studyView.setBackground(AppTheme.BG_COLOR);

        JPanel flashcardPanel = new JPanel(new BorderLayout(20, 10));
        flashcardPanel.setOpaque(false);
        flashcardPanel.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));

//        JButton prevArrowBtn = new arrowButton("‹", true);
//        JButton nextArrowBtn = new arrowButton("›", true);

        flashcard.setBorder(BorderFactory.createEmptyBorder(24, 36, 36, 36));
        flashcard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        flashcard.setBackground(AppTheme.CARD_COLOR);

        whichSideLabel.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, 11));
        whichSideLabel.setForeground(AppTheme.TEXT_PRIMARY);

        flashcardLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, 22));
        flashcardLabel.setForeground(AppTheme.TEXT_PRIMARY);

        currentIndexLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
        currentIndexLabel.setForeground(AppTheme.TEXT_SECONDARY);

        flashcard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                flipCard();
            }
        });

        flashcard.add(flashcardLabel, BorderLayout.CENTER);
        flashcard.add(whichSideLabel, BorderLayout.NORTH);

        flashcardPanel.add(currentIndexLabel, BorderLayout.SOUTH);
        flashcardPanel.add(flashcard, BorderLayout.CENTER);

        // buttons
        JButton home = new FormattedButton();
        JButton back = new FormattedButton("Back");

        home.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        goHome();
                        getDeckController.switchToHomescreenView();
                    }
                }
        );

        DeckView viewPanel = this;
        back.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        layout.show(viewPanel, "deckView");
                    }
                }
        );

        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);
        buttons.add(home);
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(back);
        toolBar.add(buttons, BorderLayout.NORTH);

        studyView.add(toolBar, BorderLayout.WEST);
        studyView.add(flashcardPanel, BorderLayout.CENTER);

        return studyView;
    }

    private void updateCardDisplay() {
        SwingDeck current = deckModel.get(currentIndex);
        String text = !isFlipped ? current.getQuestion() : current.getAnswer();

        whichSideLabel.setText(!isFlipped ? "QUESTION" : "ANSWER");
        flashcardLabel.setFont(new Font(AppTheme.FONT_NAME, isFlipped ? Font.PLAIN : Font.BOLD, 22));
        flashcardLabel.setText("<html><div style='text-align: center; line-height: 1.4;'>" + text + "</div></html>");

        flashcard.setBackground(!isFlipped ? AppTheme.CARD_COLOR : AppTheme.SELECTION_BG);
        currentIndexLabel.setText(String.format("Card %d of %d", currentIndex + 1, deckModel.size()));
    }

    private void goHome() {
        currentIndex = 0;
        isFlipped = false;
        updateCardDisplay();
    }

    private void flipCard() {
        isFlipped = !isFlipped;
        updateCardDisplay();
    }

    // Class used for swing JList
    static class SwingDeck {
        private final String question;
        private final String answer;

        public SwingDeck(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() { return question; }
        public String getAnswer() { return answer; }
    }

    // Sets the format of each Deck in the list
    static class SwingDeckRenderer extends JPanel implements ListCellRenderer<DeckView.SwingDeck> {
        private final JPanel cardPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        private final JTextArea questionLabel = new JTextArea();
        private final JTextArea answerLabel = new JTextArea();

        public SwingDeckRenderer() {
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(6, 12, 6, 12));

            questionLabel.setLineWrap(true);
            questionLabel.setWrapStyleWord(true);
            questionLabel.setOpaque(false);
            questionLabel.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE));
            questionLabel.setForeground(AppTheme.TEXT_PRIMARY);

            answerLabel.setLineWrap(true);
            answerLabel.setWrapStyleWord(true);
            answerLabel.setOpaque(false);
            answerLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
            answerLabel.setForeground(AppTheme.TEXT_PRIMARY);

            // creates line between question and answer
            questionLabel.setBorder(new CompoundBorder(
                    new MatteBorder(0, 0, 0, 1, AppTheme.BORDER_COLOR),
                    new EmptyBorder(0, 0, 0, 12)
            ));

            cardPanel.add(questionLabel);
            cardPanel.add(answerLabel);
            add(cardPanel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends SwingDeck> list, SwingDeck deck, int index,
                boolean isSelected, boolean cellHasFocus) {

            questionLabel.setText(deck.getQuestion());
            answerLabel.setText(deck.getAnswer());

            cardPanel.setBorder(new EmptyBorder(14, 16, 14, 16));

            if (isSelected) {
                cardPanel.setBackground(AppTheme.SELECTION_BG);
                questionLabel.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE));
                questionLabel.setForeground(AppTheme.PRIMARY_COLOR);
                answerLabel.setForeground(AppTheme.PRIMARY_COLOR);

            } else {
                cardPanel.setBackground(AppTheme.CARD_COLOR);
                questionLabel.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE));
                questionLabel.setForeground(AppTheme.TEXT_PRIMARY);
                answerLabel.setBackground(AppTheme.BG_SECONDARY);
                answerLabel.setForeground(AppTheme.TEXT_PRIMARY);
            }

            // spacing between rows
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BG_COLOR),
                    new EmptyBorder(10, 14, 10, 14)
            ));

            // wrap the question and answer text
            int width = list.getWidth();
            if (width > 0) {
                setSize(width, Short.MAX_VALUE);
                doLayout();
            }

            return this;
        }
    }
}
