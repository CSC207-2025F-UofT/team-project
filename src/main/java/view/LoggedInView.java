package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.upload_statement.UploadStatementController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final JLabel passwordErrorField = new JLabel();
    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController;
    private UploadStatementController uploadStatementController;

    private final JLabel username;

    private final JButton uploadStatement;

    private final JButton logOut;

    private final JButton changePasswordButton;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Welcome!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);


        final JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        uploadStatement = new JButton("Upload statement");


        final JPanel buttons = new JPanel();
        changePasswordButton = new JButton("Change Password");
        buttons.add(changePasswordButton);
        logOut = new JButton("Log Out");
        buttons.add(logOut);

        changePasswordButton.addActionListener(this);
        logOut.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(usernameInfo);
        this.add(username);
        this.add(uploadStatement);
        this.add(buttons);

        uploadStatement.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                chooseCSVFile();
            }
        });
    }



    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source.equals(logOut)) {
            logoutController.execute();
        }
        else if (source.equals(changePasswordButton)) {
            handleChangePassword();
        }
    }

    private void handleChangePassword() {
        JPasswordField newPasswordField = new JPasswordField(15);
        int result = JOptionPane.showConfirmDialog(
                this,
                newPasswordField,
                "Enter new password",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String newPassword = new String(newPasswordField.getPassword());
            String currentUsername = loggedInViewModel.getState().getUsername();
            changePasswordController.execute(newPassword, currentUsername);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoggedInState state = (LoggedInState) evt.getNewValue();
        username.setText(state.getUsername());

        if (state.getPasswordError() != null) {
            passwordErrorField.setText(state.getPasswordError());
            JOptionPane.showMessageDialog(this, state.getPasswordError());
        } else {
            passwordErrorField.setText("");
        }
    }

    private void chooseCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Bank Statement CSV");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            uploadStatementController.execute(file.getAbsolutePath(), loggedInViewModel.getState().getUsername());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
    public void setUploadStatementController(UploadStatementController uploadStatementController) {
        this.uploadStatementController = uploadStatementController; }
    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }
}
