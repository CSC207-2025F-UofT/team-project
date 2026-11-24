package view;

import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.set_budget.SetBudgetController;
import interface_adapter.set_budget.SetBudgetState;
import interface_adapter.set_budget.SetBudgetViewModel;
import interface_adapter.set_budget.SetBudgetViewModelListener;

/**
 * The View for setting the daily budget and wallet balance.
 */
public class SetBudgetView extends JPanel implements SetBudgetViewModelListener {

    private final SetBudgetViewModel setBudgetViewModel;
    private SetBudgetController setBudgetController;
    private Runnable showLoggedIn;

    private final JLabel dateLabel = new JLabel();
    private final JTextField dailyLimitField = new JTextField(10);
    private final JTextField balanceField = new JTextField(10);
    private final JLabel messageLabel = new JLabel(" ");

    public SetBudgetView(SetBudgetViewModel setBudgetViewModel) {
        this.setBudgetViewModel = setBudgetViewModel;
        this.setBudgetViewModel.addListener(this);

        final JLabel title = new JLabel("Set Budget and Balance");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel limitInfo = new LabelTextPanel(new JLabel("Daily budget"), dailyLimitField);
        final LabelTextPanel balanceInfo = new LabelTextPanel(new JLabel("Total balance"), balanceField);

        final JButton save = new JButton("Save");
        final JButton back = new JButton("Back");
        final JPanel buttons = new JPanel();
        buttons.add(save);
        buttons.add(back);

        onTextChange(dailyLimitField, () ->
                setBudgetViewModel.getState().setDailyLimit(dailyLimitField.getText()));
        onTextChange(balanceField, () ->
                setBudgetViewModel.getState().setBalance(balanceField.getText()));

        save.addActionListener(evt -> {
            final SetBudgetState state = setBudgetViewModel.getState();
            setBudgetController.execute(state.getUsername(), state.getDailyLimit(), state.getBalance());
        });
        back.addActionListener(evt -> {
            if (showLoggedIn != null) {
                showLoggedIn.run();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(dateLabel);
        this.add(limitInfo);
        this.add(balanceInfo);
        this.add(messageLabel);
        this.add(buttons);

        refreshDate();
    }

    private static void onTextChange(JTextField field, Runnable action) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                action.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                action.run();
            }
        });
    }

    private void refreshDate() {
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
    }

    @Override
    public void stateChanged() {
        final SetBudgetState state = setBudgetViewModel.getState();
        refreshDate();

        if (!dailyLimitField.getText().equals(state.getDailyLimit())) {
            dailyLimitField.setText(state.getDailyLimit());
        }
        if (!balanceField.getText().equals(state.getBalance())) {
            balanceField.setText(state.getBalance());
        }

        if (!state.getErrorMessage().isEmpty()) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getErrorMessage());
        }
        else if (!state.getSuccessMessage().isEmpty()) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText(state.getSuccessMessage());
        }
        else {
            messageLabel.setText(" ");
        }
    }

    public void setSetBudgetController(SetBudgetController setBudgetController) {
        this.setBudgetController = setBudgetController;
    }

    public void setShowLoggedIn(Runnable showLoggedIn) {
        this.showLoggedIn = showLoggedIn;
    }
}