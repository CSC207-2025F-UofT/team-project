package view;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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

    @Override
    public void stateChanged() {
        final CompatibilityState state = viewModel.getState();
        removeAll();
        if (!state.getMessage().isEmpty()) {
            add(label(state.getMessage(), false));
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

    private JLabel label(String text, boolean conflict) {
        final JLabel label = new JLabel();
        label.putClientProperty("html.disable", true);
        label.setText(text);
        label.getAccessibleContext().setAccessibleName(text);
        if (conflict) {
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        }
        return label;
    }
}
