package view;

import interface_adapter.profile.CreateProfileController;
import interface_adapter.profile.ProfileFormController;
import interface_adapter.profile.ProfileViewModel;
import interface_adapter.profile.UpdateProfileController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import use_case.profile.ProfileData;

/** Shared Swing form for creating and updating the local student's qualification profile. */
public final class ProfileView extends JPanel {
  private final JTextField program = new JTextField(28);
  private final JTextField year = new JTextField();
  private final JTextField skills = new JTextField();
  private final JTextField roles = new JTextField();
  private final JTextField locations = new JTextField();
  private final JComboBox<String> arrangement =
      new JComboBox<>(new String[] {"ANY", "REMOTE", "HYBRID", "ONSITE"});
  private final JButton save = new JButton();
  private final JButton cancel = new JButton("Cancel edits");
  private final JButton reload = new JButton("Reload profile");
  private final JButton recommendations = new JButton("View recommendations");
  private final JLabel status = new JLabel();

  /** Connects form actions to their use cases and observes presenter updates. */
  public ProfileView(
      CreateProfileController create,
      UpdateProfileController update,
      ProfileViewModel model,
      Runnable showRecommendations) {
    super(new BorderLayout(8, 8));
    setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    add(
        new JLabel("Separate multiple skills, roles or locations with commas."),
        BorderLayout.NORTH);
    JPanel form = new JPanel(new GridBagLayout());
    addField(form, 0, "Program of study", program);
    addField(form, 1, "Graduation year (optional)", year);
    addField(form, 2, "Skills", skills);
    addField(form, 3, "Preferred roles (optional)", roles);
    addField(form, 4, "Preferred locations (optional)", locations);
    addField(form, 5, "Work arrangement", arrangement);
    JPanel top = new JPanel(new BorderLayout());
    top.add(form, BorderLayout.NORTH);
    add(new JScrollPane(top), BorderLayout.CENTER);
    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
    buttons.add(save);
    buttons.add(cancel);
    buttons.add(reload);
    buttons.add(recommendations);
    JPanel footer = new JPanel(new BorderLayout(0, 6));
    footer.add(status, BorderLayout.NORTH);
    footer.add(buttons, BorderLayout.SOUTH);
    add(footer, BorderLayout.SOUTH);

    save.addActionListener(
        event -> {
          ProfileFormController controller =
              model.getState().getMode() == ProfileViewModel.Mode.CREATE ? create : update;
          controller.submit(
              program.getText(),
              year.getText(),
              skills.getText(),
              roles.getText(),
              locations.getText(),
              (String) arrangement.getSelectedItem());
        });
    cancel.addActionListener(event -> model.cancel());
    reload.addActionListener(event -> update.loadProfile());
    recommendations.addActionListener(event -> showRecommendations.run());
    model.addPropertyChangeListener(event -> render(model.getState()));
    render(model.getState());
  }

  private static void addField(JPanel form, int row, String text, JComponent field) {
    GridBagConstraints cell = new GridBagConstraints();
    cell.gridy = row;
    cell.gridx = 0;
    cell.anchor = GridBagConstraints.LINE_START;
    cell.insets = new Insets(4, 2, 4, 10);
    JLabel label = new JLabel(text);
    label.setLabelFor(field);
    form.add(label, cell);
    cell.gridx = 1;
    cell.weightx = 1;
    cell.fill = GridBagConstraints.HORIZONTAL;
    form.add(field, cell);
  }

  private void render(ProfileViewModel.State state) {
    ProfileData draft = state.getDraft();
    program.setText(draft.getProgram());
    year.setText(draft.getYear());
    skills.setText(draft.getSkills());
    roles.setText(draft.getRoles());
    locations.setText(draft.getLocations());
    arrangement.setSelectedItem(draft.getArrangement());
    for (JComponent field :
        new JComponent[] {program, year, skills, roles, locations, arrangement}) {
      field.setEnabled(state.isEditable());
    }
    boolean existing = state.getMode() == ProfileViewModel.Mode.EDIT;
    save.setText(existing ? "Save changes" : "Save profile");
    save.setEnabled(state.isEditable());
    cancel.setEnabled(state.isEditable());
    reload.setEnabled(state.isEditable() || state.getMode() == ProfileViewModel.Mode.UNAVAILABLE);
    recommendations.setEnabled(existing);
    status.setText(state.getMessage());
  }
}
