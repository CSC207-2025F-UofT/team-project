package view;

import entity.Transaction;
import interface_adapter.record_transaction.RecordTransactionController;
import interface_adapter.record_transaction.RecordTransactionState;
import interface_adapter.record_transaction.RecordTransactionViewModel;
import interface_adapter.record_transaction.RecordTransactionViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Component;
import java.awt.Dimension;

/**
 * The form for recording an income or expense.
 */
public class RecordTransactionView extends JPanel
        implements RecordTransactionViewModelListener {

    private final RecordTransactionViewModel recordTransactionViewModel;
    private final JTextField amountInputField = new JTextField(15);
    private final JComboBox<Transaction.Type> typeInputField =
            new JComboBox<>(Transaction.Type.values());
    private final JLabel messageLabel = new JLabel(" ");
    private final JButton saveButton = new JButton("Save");
    private final JButton backButton = new JButton("Back");

    private RecordTransactionController recordTransactionController;

    public RecordTransactionView(
            RecordTransactionViewModel recordTransactionViewModel) {
        this.recordTransactionViewModel = recordTransactionViewModel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(500, 220));

        final JLabel title = new JLabel("Record Transaction");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel amountPanel = new LabelTextPanel(
                new JLabel("Amount"), amountInputField);

        final JPanel typePanel = new JPanel();
        typePanel.add(new JLabel("Type"));
        typePanel.add(typeInputField);

        final JPanel messagePanel = new JPanel();
        messagePanel.add(messageLabel);

        final JPanel buttons = new JPanel();
        buttons.add(saveButton);
        buttons.add(backButton);
        saveButton.setEnabled(false);

        add(title);
        add(amountPanel);
        add(typePanel);
        add(messagePanel);
        add(buttons);

        amountInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void updateAmount() {
                recordTransactionViewModel.getState()
                        .setAmount(amountInputField.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent event) {
                updateAmount();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                updateAmount();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                updateAmount();
            }
        });

        typeInputField.addActionListener(event ->
                recordTransactionViewModel.getState().setType(
                        (Transaction.Type) typeInputField.getSelectedItem()));

        saveButton.addActionListener(event -> {
            final RecordTransactionState state = recordTransactionViewModel.getState();
            recordTransactionController.execute(state.getAmount(), state.getType());
        });

        this.recordTransactionViewModel.addListener(this);
        stateChanged();
    }

    @Override
    public void stateChanged() {
        final RecordTransactionState state = recordTransactionViewModel.getState();

        if (!amountInputField.getText().equals(state.getAmount())) {
            amountInputField.setText(state.getAmount());
        }
        typeInputField.setSelectedItem(state.getType());

        if (state.getError() != null) {
            view.settings.ThemeManager.setMessageColor(
                    messageLabel, "App.error");
            messageLabel.setText(state.getError());
        }
        else {
            view.settings.ThemeManager.setMessageColor(
                    messageLabel, "App.success");
            messageLabel.setText(state.getSuccessMessage() == null
                    ? " " : state.getSuccessMessage());
        }
    }

    public void setRecordTransactionController(
            RecordTransactionController recordTransactionController) {
        this.recordTransactionController = recordTransactionController;
        saveButton.setEnabled(recordTransactionController != null);
    }

    public void setBackAction(Runnable action) {
        backButton.addActionListener(event -> action.run());
    }
}
