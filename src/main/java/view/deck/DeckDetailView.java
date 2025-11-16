package view.deck;

import interface_adapter.deck.DeckDetailViewModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DeckDetailView extends JPanel {
    private final DeckDetailViewModel viewModel;

    private final JLabel titleLabel = new JLabel();
    private final JButton addButton = new JButton("Add Flashcard");
    private final JButton playButton = new JButton("Study Deck");
    private final JButton backButton = new JButton("Back to Menu");

    public DeckDetailView(DeckDetailViewModel viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(titleLabel);
        add(top, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(playButton);
        buttons.add(backButton);
        add(buttons, BorderLayout.SOUTH);

        // placeholder for flashcards list
        add(new JScrollPane(new JPanel()), BorderLayout.CENTER);

        // TODO: add button listeners (to UC1, UC2, UC9) later
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void addAddFlashcardListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addPlayListener(ActionListener listener) {
        playButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}

