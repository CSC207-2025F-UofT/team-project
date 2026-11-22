package view;

import interface_adapter.edit_card.EditCardController;
import interface_adapter.edit_card.EditCardViewModel;

import javax.swing.*;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditCardView extends JPanel {
    private final JTextField questionField = new JTextField();
    private final JTextField answerField = new JTextField();
    private final JLabel resultLabel = new JLabel(" ");

    private final EditCardViewModel viewModel;
    private final PropertyChangeListener listener = this::displayResult;

    public EditCardView(EditCardController controller,
                        EditCardViewModel viewModel,
                        int deckId, int cardId,
                        String initialQuestion, String initialAnswer) {
        this.viewModel = viewModel;

        setLayout(new GridLayout(0, 1, 8, 8));

        questionField.setText(initialQuestion);
        answerField.setText(initialAnswer);

        JButton saveButton = new JButton("Save changes");

        add(new JLabel("Question"));
        add(questionField);
        add(new JLabel("Answer"));
        add(answerField);
        add(saveButton);
        add(resultLabel);

        saveButton.addActionListener(event ->
                controller.execute(
                        deckId,
                        cardId,
                        questionField.getText(),
                        answerField.getText()
                )
        );

        viewModel.addPropertyChangeListener(listener);
    }

    private void displayResult(PropertyChangeEvent event) {
        if (EditCardViewModel.CARD_UPDATED.equals(
                event.getPropertyName())) {
            questionField.setText(viewModel.getQuestion());
            answerField.setText(viewModel.getAnswer());
            resultLabel.setText(viewModel.getMessage());
        } else if (EditCardViewModel.ERROR.equals(
                event.getPropertyName())) {
            resultLabel.setText(viewModel.getError());
        }
    }

    public void dispose() {
        viewModel.removePropertyChangeListener(listener);
    }
}