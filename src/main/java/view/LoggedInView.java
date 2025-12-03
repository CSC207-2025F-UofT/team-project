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

    private final JTextField passwordInputField = new JTextField(15);

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Welcome!");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);


        final JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        uploadStatement = new JButton("Upload statement");
        uploadStatement.addActionListener(this);

        final JPanel buttons = new JPanel();
        logOut = new JButton("Log Out");
        buttons.add(logOut);


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
        // TODO: execute the logout use case through the Controller
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

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
        // TODO: save the logout controller in the instance variable.
    }
    public void setUploadStatementController(UploadStatementController uploadStatementController) {
        this.uploadStatementController = uploadStatementController; }
}
