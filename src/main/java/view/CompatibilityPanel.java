package view;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.check_compatibility.CompatibilityState;
import interface_adapter.check_compatibility.CompatibilityViewModel;
import interface_adapter.check_compatibility.CompatibilityViewModelListener;

/**
 * The panel on the detail page that says how well the cat fits the adopter's home, one line per rule.
 */
public class CompatibilityPanel extends JPanel implements CompatibilityViewModelListener {
    private final CompatibilityViewModel viewModel;
    private Runnable logInAction = () -> { };
    private Runnable profileAction = () -> { };

    /**
     * Creates the panel.
     *
     * @param viewModel the model whose state this panel draws
     */
    public CompatibilityPanel(CompatibilityViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Does this cat fit your home?"));
        getAccessibleContext().setAccessibleName("Compatibility with your home");
    }

    /**
     * Sets what the Log in button does.
     *
     * @param action opens the login screen
     */
    public void setLogInAction(Runnable action) {
        this.logInAction = action;
    }

    /**
     * Sets what the Fill in My profile button does.
     *
     * @param action opens My profile
     */
    public void setProfileAction(Runnable action) {
        this.profileAction = action;
    }

    @Override
    public void stateChanged() {
        final CompatibilityState state = viewModel.getState();
        removeAll();
        if (!state.getMessage().isEmpty()) {
            add(label(state.getMessage(), false));
        }
        if (state.needsLogin()) {
            add(button("Log in", () -> logInAction.run()));
        }
        if (state.needsPreferences()) {
            add(button("Open My profile", () -> profileAction.run()));
        }
        if (!state.getHeadline().isEmpty()) {
            final JLabel headline = label(state.getHeadline(), false);
            headline.setFont(headline.getFont().deriveFont(Font.BOLD));
            add(headline);
        }
        for (int i = 0; i < state.getLines().size(); i++) {
            add(label(state.getLines().get(i), state.getConflicts().get(i)));
        }
        revalidate();
        repaint();
    }

    private JButton button(String text, Runnable action) {
        final JButton button = new JButton(text);
        button.getAccessibleContext().setAccessibleName(text);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.addActionListener(event -> action.run());
        return button;
    }

    private JLabel label(String text, boolean conflict) {
        final JLabel label = new JLabel();
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.putClientProperty("html.disable", true);
        label.setText(text);
        label.getAccessibleContext().setAccessibleName(text);
        if (conflict) {
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        }
        return label;
    }
}
