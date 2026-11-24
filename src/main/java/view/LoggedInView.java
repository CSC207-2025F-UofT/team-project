package view;

import interface_adapter.add_course.AddCourseController;
import interface_adapter.job_list.JobListController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInViewModelListener;
import interface_adapter.logout.LogoutController;
import interface_adapter.skills_list.SkillListController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements ActionListener, LoggedInViewModelListener {

    private final LoggedInViewModel loggedInViewModel;
    private final JLabel passwordErrorField = new JLabel();
    private LogoutController logoutController;
    private AddCourseController addCourseController;
    private SkillListController skillListController;
    private JobListController jobListController;

    private final JLabel username;

    private final JButton logOut;

    private final JTextField passwordInputField = new JTextField(15);

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addListener(this);

        final JLabel title = new JLabel("Logged In Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

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

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setPassword(passwordInputField.getText());
                loggedInViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.add(title);
        this.add(usernameInfo);
        this.add(username);

        this.add(passwordInfo);
        this.add(passwordErrorField);
        this.add(navigation);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        // TODO: execute the logout use case through the Controller
        System.out.println("Click " + evt.getActionCommand());
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

    public void setLogoutController(LogoutController logoutController) {
        // TODO: save the logout controller in the instance variable.
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
            passwordInputField.setText("");
        }
        else {
            JOptionPane.showMessageDialog(this, state.getPasswordError());
        }
    }
}
