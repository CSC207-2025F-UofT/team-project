package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import interface_adapter.submit_application.SubmitApplicationController;
import interface_adapter.submit_application.SubmitApplicationState;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.submit_application.SubmitApplicationViewModelListener;

/**
 * The View for applying to adopt one specific pet.
 */
public final class SubmitApplicationView extends JPanel implements SubmitApplicationViewModelListener {

    private static final int PADDING = 12;
    private static final int LABEL_WIDTH = 160;
    private static final int LABEL_HEIGHT = 20;
    private static final String BLANK = " ";

    private final SubmitApplicationViewModel submitApplicationViewModel;
    private SubmitApplicationController submitApplicationController;

    private final JLabel title = new JLabel("Apply to adopt");
    private final JComboBox<String> housingTypeBox = new JComboBox<>(
            new String[] {"", "House", "Apartment", "Condo", "Other"});
    private final JTextField petExperienceField = new JTextField(25);
    private final JTextField householdMembersField = new JTextField(25);
    private final JTextField contactInfoField = new JTextField(25);
    private final JTextArea noteArea = new JTextArea(3, 25);
    private final JLabel errorLabel = new JLabel(BLANK);
    private final JLabel successLabel = new JLabel(BLANK);
    private final JButton submitButton = new JButton("Submit application");

    /**
     * Creates the view.
     * @param submitApplicationViewModel the view model this view displays
     */
    public SubmitApplicationView(SubmitApplicationViewModel submitApplicationViewModel) {
        this.submitApplicationViewModel = submitApplicationViewModel;
        this.submitApplicationViewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        errorLabel.setForeground(Color.RED);
        noteArea.setLineWrap(true);
        noteArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        add(title);
        add(row("Housing type", housingTypeBox));
        add(row("Experience with pets", petExperienceField));
        add(row("Household members", householdMembersField));
        add(row("Contact information", contactInfoField));
        add(row("Note (optional)", noteArea));
        add(submitButton);
        add(errorLabel);
        add(successLabel);

        submitButton.addActionListener(event -> {
            final SubmitApplicationState state = submitApplicationViewModel.getState();
            submitApplicationController.execute(state.getAdopterId(), state.getPetId(),
                    (String) housingTypeBox.getSelectedItem(), petExperienceField.getText(),
                    householdMembersField.getText(), contactInfoField.getText(), noteArea.getText());
        });
        stateChanged();
    }

    private static JPanel row(String label, Component field) {
        final JPanel panel = new JPanel();
        final JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        panel.add(jLabel);
        panel.add(field);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private static String blankIfEmpty(String text) {
        String result = text;
        if (result.isEmpty()) {
            result = BLANK;
        }
        return result;
    }

    @Override
    public void stateChanged() {
        final SubmitApplicationState state = submitApplicationViewModel.getState();
        title.setText("Apply to adopt " + state.getPetName());
        errorLabel.setText(blankIfEmpty(state.getError()));
        successLabel.setText(blankIfEmpty(state.getSuccessMessage()));
        if (!state.getSuccessMessage().isEmpty()) {
            housingTypeBox.setSelectedIndex(0);
            petExperienceField.setText("");
            householdMembersField.setText("");
            contactInfoField.setText("");
            noteArea.setText("");
        }
    }

    public String getViewName() {
        return SubmitApplicationViewModel.VIEW_NAME;
    }

    public void setSubmitApplicationController(SubmitApplicationController submitApplicationController) {
        this.submitApplicationController = submitApplicationController;
    }
}
