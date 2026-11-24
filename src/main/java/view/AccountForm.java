package view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Layout and behaviour shared by the Log In and Register forms. Subclasses add their fields, then call
 * {@link #addActions}. A form clears itself whenever it is hidden.
 */
abstract class AccountForm extends JPanel {
    private static final int COLUMNS = 34;

    private final GridBagConstraints row = new GridBagConstraints();
    private final Map<JTextField, JLabel> fieldErrors = new LinkedHashMap<>();
    private final JTextArea status = new JTextArea(3, COLUMNS);
    private JButton submit;
    private JButton navigate;
    private JTextField pendingFocus;

    AccountForm(String title) {
        super(new GridBagLayout());
        setBorder(new EmptyBorder(16, 20, 16, 20));
        row.gridx = 0;
        row.weightx = 1;
        row.fill = GridBagConstraints.HORIZONTAL;
        row.anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel(title), row);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                focusFirstField();
            }

            @Override
            public void componentHidden(ComponentEvent event) {
                reset();
            }
        });
    }

    /** Called when the user submits the form. */
    protected abstract void submit();

    /** Called when the user switches to the other form. */
    protected abstract void navigate();

    protected JTextField addTextField(String label, char mnemonic) {
        return addField(label, mnemonic, new JTextField(COLUMNS));
    }

    protected JPasswordField addPasswordField(String label, char mnemonic) {
        return addField(label, mnemonic, new JPasswordField(COLUMNS));
    }

    private <T extends JTextField> T addField(String labelText, char mnemonic, T input) {
        final JLabel label = new JLabel(labelText);
        label.setLabelFor(input);
        label.setDisplayedMnemonic(mnemonic);
        row.insets = new Insets(5, 0, 0, 0);
        add(label, row);
        row.insets = new Insets(0, 0, 0, 0);
        add(input, row);
        input.addActionListener(event -> submit());

        final JLabel error = new JLabel(" ");
        error.setName(labelText + " error");
        add(error, row);
        fieldErrors.put(input, error);
        return input;
    }

    protected void addActions(String submitText, String navigateText) {
        status.setName("Form status");
        status.setEditable(false);
        status.setFocusable(false);
        status.setOpaque(false);
        status.setLineWrap(true);
        status.setWrapStyleWord(true);
        status.setFont(new JLabel().getFont());
        add(status, row);

        submit = new JButton(submitText);
        submit.addActionListener(event -> submit());
        navigate = new JButton(navigateText);
        navigate.addActionListener(event -> navigate());
        final JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEADING));
        actions.add(submit);
        actions.add(navigate);
        add(actions, row);
        row.weighty = 1;
        add(new JPanel(), row);
    }

    /** Disables the form and shows progress while a controller call runs. */
    protected void startAction(String progressMessage) {
        clearMessages();
        status.setText(progressMessage);
        setBusy(true);
    }

    protected void finishAction() {
        setBusy(false);
        if (pendingFocus != null) {
            pendingFocus.requestFocusInWindow();
            pendingFocus = null;
        }
    }

    protected void showNotice(String message) {
        status.setText(message);
    }

    protected void showFieldError(JTextField field, String message) {
        final String text = "Error: " + message;
        fieldErrors.get(field).setText(text);
        field.getAccessibleContext().setAccessibleDescription(text);
        focus(field);
    }

    /** Shows an error that isn't about one field, then moves focus to {@code retryField}. */
    protected void showFormError(String message, JTextField retryField) {
        status.setText("Error: " + message);
        focus(retryField);
    }

    // Disabled fields can't take focus, so wait until the running action finishes.
    private void focus(JTextField field) {
        if (field.isEnabled()) {
            field.requestFocusInWindow();
        } else {
            pendingFocus = field;
        }
    }

    protected void clearMessages() {
        status.setText("");
        fieldErrors.forEach((field, error) -> {
            error.setText(" ");
            field.getAccessibleContext().setAccessibleDescription(null);
        });
    }

    /** Reads a password and clears the field, so it is never left on screen after a submission. */
    protected static String takePassword(JPasswordField field) {
        final String password = new String(field.getPassword());
        field.setText("");
        return password;
    }

    public void focusFirstField() {
        if (getRootPane() != null) {
            getRootPane().setDefaultButton(submit);
        }
        fieldErrors.keySet().iterator().next().requestFocusInWindow();
    }

    private void reset() {
        pendingFocus = null;
        fieldErrors.keySet().forEach(field -> field.setText(""));
        clearMessages();
        setBusy(false);
    }

    private void setBusy(boolean busy) {
        submit.setEnabled(!busy);
        navigate.setEnabled(!busy);
        fieldErrors.keySet().forEach(field -> field.setEnabled(!busy));
    }
}
