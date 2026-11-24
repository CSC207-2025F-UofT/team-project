package view;

import interface_adapter.add_course.AddCourseController;
import interface_adapter.job_list.JobListController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInViewModelListener;
import interface_adapter.logout.LogoutController;
import interface_adapter.skills_list.SkillListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements ActionListener, LoggedInViewModelListener {

    private final LoggedInViewModel loggedInViewModel;
    private LogoutController logoutController;
    private AddCourseController addCourseController;
    private SkillListController skillListController;
    private JobListController jobListController;

    private final JButton filterJobs = new JButton("Filter Jobs");

    private final JLabel username;

    private final JButton logOut;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addListener(this);

        final JLabel title = new JLabel("Logged In Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        final JPanel buttons = new JPanel();
        logOut = new JButton("Log Out");
        buttons.add(logOut);

        logOut.addActionListener(this);

        final JPanel navigation = new JPanel();
        final JButton addCourses = new JButton("Add Courses");
        final JButton viewSkills = new JButton("View Skills");
        final JButton viewJobs = new JButton("View Jobs");
        navigation.add(addCourses);
        navigation.add(viewSkills);
        navigation.add(viewJobs);
        navigation.add(filterJobs);

        addCourses.addActionListener(evt -> {
            if (addCourseController != null) {
                addCourseController.switchToAddCourseView();
            }
        });
        viewSkills.addActionListener(evt -> {
            if (skillListController != null) {
                skillListController.execute(loggedInViewModel.getState().getUsername());
            }
        });
        viewJobs.addActionListener(evt -> {
            if (jobListController != null) {
                jobListController.execute(loggedInViewModel.getState().getUsername());
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(usernameInfo);
        this.add(username);

        this.add(navigation);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == logOut) {
            logoutController.execute();
        }
    }

    public void setAddCourseController(AddCourseController addCourseController) {
        this.addCourseController = addCourseController;
    }

    public void setSkillListController(SkillListController skillListController) {
        this.skillListController = skillListController;
    }

    public void setJobListController(JobListController jobListController) {
        this.jobListController = jobListController;
    }

    public void addFilterJobsListener(ActionListener listener) {
        filterJobs.addActionListener(listener);
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    /**
     * The logged-in state was updated.
     */
    @Override
    public void stateChanged() {
        LoggedInState state = loggedInViewModel.getState();
        username.setText(state.getUsername());
    }

    /**
     * A password change attempt occurred.
     */
    @Override
    public void passwordChanged() {
        LoggedInState state = loggedInViewModel.getState();
        if (state.getPasswordError() == null) {
            JOptionPane.showMessageDialog(this, "password updated for " + state.getUsername());
        }
        else {
            JOptionPane.showMessageDialog(this, state.getPasswordError());
        }
    }
}
