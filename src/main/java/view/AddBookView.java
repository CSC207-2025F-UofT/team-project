package view;

import interface_adapter.addBook.AddBookViewModel;

import javax.swing.*;

public class AddBookView extends JPanel {

    private final AddBookViewModel viewModel;

    private final JLabel messageLabel;

    public AddBookView(AddBookViewModel viewModel) {
        this.viewModel = viewModel;

        this.messageLabel = new JLabel();

        this.add(messageLabel);

        updateView();
    }

    public void updateView() {
        messageLabel.setText(viewModel.getMessage());
    }
}
