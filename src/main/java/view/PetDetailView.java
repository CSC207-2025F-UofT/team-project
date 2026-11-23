package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import interface_adapter.SwitchViewController;
import interface_adapter.view_pet_details.PetDetailState;
import interface_adapter.view_pet_details.PetDetailViewModel;
import interface_adapter.view_pet_details.PetDetailViewModelListener;

/**
 * The page for one cat: name, summary, photo placeholder, the full profile, and a Back button.
 */
public class PetDetailView extends JPanel implements PetDetailViewModelListener {
    private static final int GAP = 12;

    private final PetDetailViewModel viewModel;
    private final JLabel nameLabel = new JLabel();
    private final JLabel summaryLabel = new JLabel();
    private final JLabel photoLabel = new JLabel("No photo yet", SwingConstants.CENTER);
    private final JPanel linesPanel = new JPanel();
    private final JLabel errorLabel = new JLabel();
    private final JButton applyButton = new JButton("Apply to adopt");
    private BiConsumer<String, String> applyHandler;

    /**
     * Creates the detail page.
     *
     * @param viewModel the model whose state this page draws
     * @param switchViewController used by the Back button
     * @param backViewName the name of the screen the Back button returns to
     */
    public PetDetailView(PetDetailViewModel viewModel, SwitchViewController switchViewController,
                         String backViewName) {
        super(new BorderLayout());
        this.viewModel = viewModel;
        viewModel.addListener(this);
        getAccessibleContext().setAccessibleName("Pet details");

        final JButton backButton = new JButton("Back to gallery");
        backButton.setMnemonic(KeyEvent.VK_B);
        backButton.getAccessibleContext().setAccessibleName("Back to gallery");
        backButton.addActionListener(event -> switchViewController.switchTo(backViewName));
        final JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, 0, GAP));
        top.add(backButton, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 24f));
        nameLabel.getAccessibleContext().setAccessibleName("Cat name");
        summaryLabel.getAccessibleContext().setAccessibleName("Cat summary");
        photoLabel.setPreferredSize(new Dimension(240, 160));
        photoLabel.setMaximumSize(new Dimension(240, 160));
        photoLabel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
        linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.Y_AXIS));
        errorLabel.getAccessibleContext().setAccessibleName("Error");

        content.add(nameLabel);
        content.add(summaryLabel);
        content.add(Box.createVerticalStrut(GAP));
        content.add(photoLabel);
        content.add(Box.createVerticalStrut(GAP));
        content.add(linesPanel);
        content.add(errorLabel);
        content.add(Box.createVerticalStrut(GAP));
        applyButton.setMnemonic(KeyEvent.VK_A);
        applyButton.getAccessibleContext().setAccessibleName("Apply to adopt this cat");
        applyButton.addActionListener(event -> apply());
        applyButton.setVisible(false);
        content.add(applyButton);
        for (java.awt.Component component : content.getComponents()) {
            ((javax.swing.JComponent) component).setAlignmentX(LEFT_ALIGNMENT);
        }
        add(content, BorderLayout.CENTER);
    }

    /**
     * Sets what happens when the visitor presses Apply. Until this is set the Apply button stays hidden.
     *
     * @param applyHandler receives the cat's id and name
     */
    public void setApplyHandler(BiConsumer<String, String> applyHandler) {
        this.applyHandler = applyHandler;
        showApplyButtonIfPossible();
    }

    private void apply() {
        final PetDetailState state = viewModel.getState();
        if (applyHandler != null && state.getErrorMessage().isEmpty()) {
            applyHandler.accept(state.getPetId(), state.getName());
        }
    }

    private void showApplyButtonIfPossible() {
        final PetDetailState state = viewModel.getState();
        applyButton.setVisible(applyHandler != null && !state.getPetId().isEmpty()
                && state.getErrorMessage().isEmpty());
    }

    @Override
    public void stateChanged() {
        final PetDetailState state = viewModel.getState();
        nameLabel.setText(state.getName());
        summaryLabel.setText(state.getSummary());
        final String photoDescription = state.getName().isEmpty()
                ? "No photo" : "Photo of " + state.getName() + " is not available yet";
        photoLabel.getAccessibleContext().setAccessibleName("Cat photo");
        photoLabel.getAccessibleContext().setAccessibleDescription(photoDescription);
        photoLabel.setVisible(state.getErrorMessage().isEmpty());
        errorLabel.setText(state.getErrorMessage());

        linesPanel.removeAll();
        for (String line : state.getDetailLines()) {
            final JLabel label = new JLabel(line);
            label.getAccessibleContext().setAccessibleName(line);
            linesPanel.add(label);
        }
        linesPanel.revalidate();
        linesPanel.repaint();
        showApplyButtonIfPossible();
        applyButton.getAccessibleContext().setAccessibleName("Apply to adopt " + state.getName());
    }
}
