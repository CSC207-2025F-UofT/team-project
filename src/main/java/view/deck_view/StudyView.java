package view.deck_view;

import interface_adapter.get_all_decks.GetAllDecksController;
import interface_adapter.get_deck.GetDeckController;
import interface_adapter.get_deck.study_view.StudyState;
import interface_adapter.get_deck.study_view.StudyViewModel;
import interface_adapter.get_deck.study_view.StudyViewModelListener;
import view.AppTheme;
import view.FormattedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class StudyView extends JPanel implements StudyViewModelListener {

    private final StudyViewModel studyViewModel;

    private final JLabel deckName = new JLabel();
    private final JLabel cardCount = new JLabel();
    private final JLabel flashcardLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel whichSideLabel = new JLabel("QUESTION", SwingConstants.CENTER);
    private final JLabel currentIndexLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel emptyDeckError = new JLabel();

    private final CardLayout centerLayout = new CardLayout();
    private final JPanel center = new JPanel(centerLayout);
    private final JPanel flashcard = new JPanel(new BorderLayout());

    private GetDeckController getDeckController = null;
    private GetAllDecksController getAllDecksController = null;


    public StudyView(StudyViewModel studyViewModel) {
        this.studyViewModel = studyViewModel;
        this.studyViewModel.addListener(this);

        createStudyView();
    }

    private void createStudyView() {
        this.setLayout(new BorderLayout(14, 14));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setBackground(AppTheme.BG_COLOR);

        this.add(createToolBar(), BorderLayout.WEST);
        this.add(createFlashcardPanel(), BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        // buttons
        JButton home = new FormattedButton();
        JButton view = new FormattedButton("View");

        home.addActionListener(evt -> {
            studyViewModel.reset();
            getAllDecksController.execute();
        });

        view.addActionListener(evt -> {
            studyViewModel.reset();
            getDeckController.switchToDeckView();
        });

        // add buttons to toolBar
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);

        buttons.add(home);
        buttons.add(Box.createVerticalStrut(10));
        buttons.add(view);

        JPanel toolBar = new JPanel(new BorderLayout());
        toolBar.setOpaque(false);
        toolBar.add(buttons, BorderLayout.NORTH);

        return toolBar;
    }

    private JPanel createFlashcardPanel() {
        // title text
        deckName.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE_TITLE));
        deckName.setForeground(AppTheme.TEXT_PRIMARY);
        cardCount.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
        cardCount.setForeground(AppTheme.TEXT_SECONDARY);

        // deck name and card count
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(deckName);
        titlePanel.add(cardCount);

        // make flashcard
        flashcard.setBorder(BorderFactory.createEmptyBorder(24, 36, 36, 36));
        flashcard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        flashcard.setBackground(AppTheme.CARD_COLOR);

        flashcard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                studyViewModel.flipCard();
            }
        });

        whichSideLabel.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, 11));
        whichSideLabel.setForeground(AppTheme.TEXT_PRIMARY);

        flashcardLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE_TITLE));
        flashcardLabel.setForeground(AppTheme.TEXT_PRIMARY);

        flashcard.add(flashcardLabel, BorderLayout.CENTER);
        flashcard.add(whichSideLabel, BorderLayout.NORTH);

        // foot panel with arrows and current card number
        currentIndexLabel.setFont(new Font(AppTheme.FONT_NAME, Font.PLAIN, AppTheme.FONT_SIZE));
        currentIndexLabel.setForeground(AppTheme.TEXT_SECONDARY);
        currentIndexLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backArrow = new FormattedButton("\u2190 Last", AppTheme.BG_SECONDARY, AppTheme.TEXT_PRIMARY);
        JButton nextArrow = new FormattedButton("Next \u2192", AppTheme.BG_SECONDARY, AppTheme.TEXT_PRIMARY);
        backArrow.addActionListener(evt -> studyViewModel.lastCard());
        nextArrow.addActionListener(evt -> studyViewModel.nextCard());

        JPanel arrows = new JPanel(new FlowLayout(FlowLayout.CENTER));
        arrows.setOpaque(false);

        arrows.add(backArrow);
        arrows.add(nextArrow);

        JPanel footPanel = new JPanel();
        footPanel.setLayout(new BoxLayout(footPanel, BoxLayout.Y_AXIS));
        footPanel.setOpaque(false);

        footPanel.add(currentIndexLabel);
        footPanel.add(arrows);

        // flashcard panel
        JPanel flashcardPanel = new JPanel(new BorderLayout(20, 10));
        flashcardPanel.setOpaque(false);
        flashcardPanel.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));

        flashcardPanel.add(titlePanel, BorderLayout.NORTH);
        flashcardPanel.add(footPanel, BorderLayout.SOUTH);
        flashcardPanel.add(flashcard, BorderLayout.CENTER);

        return flashcardPanel;
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
        // give error pop up if could not get deck
        if (studyViewModel.getState().getDeckError() != null) {
            JOptionPane.showMessageDialog(this, studyViewModel.getState().getDeckError());
        } else {
            setFields(studyViewModel.getState());
        }
    }

    private void setFields(StudyState state) {
        deckName.setText(state.getDeckName());
        cardCount.setText(state.getCardCountText());
        whichSideLabel.setText(state.getWhichSideLabel());
        currentIndexLabel.setText(state.getProgressText());

        flashcardLabel.setFont(new Font(AppTheme.FONT_NAME,
                state.getIsFlipped() ? Font.PLAIN : Font.BOLD, AppTheme.FONT_SIZE_TITLE));
        flashcardLabel.setText("<html><div style='text-align: center; line-height: 1.4;'>" +
                state.getCurrentCardText() + "</div></html>");
        flashcard.setBackground(state.getIsFlipped() ? AppTheme.SELECTION_BG : AppTheme.CARD_COLOR);
    }

    public void setGetDeckController(GetDeckController getDeckController) {
        this.getDeckController = getDeckController;
    }

    public void setGetAllDecksController(GetAllDecksController getAllDecksController) {
        this.getAllDecksController = getAllDecksController;
    }
}
