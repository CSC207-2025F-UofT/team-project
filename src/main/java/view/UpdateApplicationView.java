package view;


import interface_adapter.update_application_status.UpdateApplicationController;
import interface_adapter.update_application_status.UpdateApplicationState;
import interface_adapter.update_application_status.UpdateApplicationViewModel;
import interface_adapter.update_application_status.UpdateApplicationViewModelListener;

import javax.swing.*;
import java.awt.*;

public class UpdateApplicationView extends JPanel implements UpdateApplicationViewModelListener {
    private  UpdateApplicationController updateApplicationController;
    private final UpdateApplicationViewModel updateApplicationViewModel;

    private final JTextField adminNoteInputField = new JTextField(15);
    private final JComboBox<String> newStatusBox = new JComboBox<>(new String[] {"INTERVIEW", "APPROVED", "REJECTED"});
    private final JButton UpdateApplication;
    private final JButton cancel;
    private final JLabel subtitle = new JLabel("");
    private final JLabel updateErrorField = new JLabel();


    public UpdateApplicationView(UpdateApplicationViewModel updateApplicationViewModel) {
        this.updateApplicationViewModel = updateApplicationViewModel;
        this.updateApplicationViewModel.addListener(this);

        final UpdateApplicationState state = updateApplicationViewModel.getState();
        final String adopterName = state.getAdopterName();


        final JLabel title = new JLabel("Update Application Screen");

        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));

        final JPanel adminNoteInfo = row(
                "Admin Note", adminNoteInputField);
        final JPanel newStatusInfo = row("New Status", newStatusBox);

        final JPanel buttons = new JPanel();
        UpdateApplication = new JButton("update application");
        buttons.add(UpdateApplication);
        cancel = new JButton("cancel");
        buttons.add(cancel);

        UpdateApplication.addActionListener(event -> {
            if (updateApplicationController != null) {
                updateApplicationController.execute(updateApplicationViewModel.getState().getApplicationId(),
                        (String) newStatusBox.getSelectedItem(), adminNoteInputField.getText());
            }
        });
        cancel.addActionListener(event -> {
            if (updateApplicationController != null) {
                updateApplicationController.switchToReviewApplicationView();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(subtitle);
        this.add(adminNoteInfo);
        this.add(newStatusInfo);
        this.add(buttons);
        this.add(updateErrorField);
    }

    private static JPanel row(String label, Component field) {
        final JPanel panel = new JPanel();
        final JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(160, 20));
        panel.add(jLabel);
        panel.add(field);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    @Override
    public void stateChanged() {
        final UpdateApplicationState state = updateApplicationViewModel.getState();
        final String adopterName = state.getAdopterName();
        final String petName = state.getPetName();

        subtitle.setText("Updating " + adopterName + "'s application for " + petName);

        adminNoteInputField.setText(state.getAdminNote());
        if (state.getUpdateError().isEmpty()) {
            updateErrorField.setText(" ");
        }
        else {
            updateErrorField.setText(state.getUpdateError());
        }
    }

    public String getViewName() {
        return UpdateApplicationViewModel.VIEW_NAME;
    }

    public void setUpdateApplicationController(UpdateApplicationController updateApplicationController) {
        this.updateApplicationController = updateApplicationController;
    }

}
