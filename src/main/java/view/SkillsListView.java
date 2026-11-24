package view;

import interface_adapter.skills_list.SkillListController;
import interface_adapter.skills_list.SkillListState;
import interface_adapter.skills_list.SkillListViewModel;
import interface_adapter.skills_list.SkillListViewModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * The View that shows the skills extracted from the student's courses.
 */
public class SkillsListView extends JPanel implements SkillListViewModelListener {

    private final SkillListViewModel skillListViewModel;
    private final DefaultListModel<String> skillsListModel = new DefaultListModel<>();
    private final JLabel errorLabel = new JLabel(" ");
    private SkillListController skillListController;

    public SkillsListView(SkillListViewModel skillListViewModel) {
        this.skillListViewModel = skillListViewModel;
        this.skillListViewModel.addListener(this);

        final JLabel title = new JLabel(SkillListViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton backButton = new JButton("Back");
        backButton.addActionListener(evt -> {
            if (skillListController != null) {
                skillListController.switchToLoggedInView();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(errorLabel);
        this.add(new JScrollPane(new JList<>(skillsListModel)));
        this.add(backButton);
    }

    @Override
    public void stateChanged() {
        final SkillListState state = skillListViewModel.getState();
        errorLabel.setText(state.getSkillsError() == null ? " " : state.getSkillsError());
        skillsListModel.clear();
        if (state.getSkillsError() == null) {
            for (String skill : state.getSkills()) {
                skillsListModel.addElement(skill);
            }
        }
    }

    public void setSkillListController(SkillListController skillListController) {
        this.skillListController = skillListController;
    }
}
