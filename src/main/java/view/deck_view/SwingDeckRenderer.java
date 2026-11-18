package view.deck_view;

import view.AppTheme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * SwingDeckRenderer for JList in deck view
 * Sets the format of each Deck in the list
 */
public class SwingDeckRenderer extends JPanel implements ListCellRenderer<SwingDeck> {
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