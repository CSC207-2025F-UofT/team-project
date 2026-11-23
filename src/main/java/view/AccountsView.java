package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The view that lets a logged-out user choose between signing up and logging in.
 */
public class AccountsView extends JPanel implements ActionListener {
    private final ViewManagerModel viewManagerModel;
    private final JButton signUp;
    private final JButton logIn;
    private final JButton returnToHome;

    public AccountsView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;

        final JLabel title = new JLabel("Accounts");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = new JPanel();
        signUp = new JButton("Sign Up");
        logIn = new JButton("Log In");
        returnToHome = new JButton("Return");
        buttons.add(signUp);
        buttons.add(logIn);
        buttons.add(returnToHome);

        signUp.addActionListener(this);
        logIn.addActionListener(this);
        returnToHome.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(buttons);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(signUp)) {
            viewManagerModel.setState(SignupViewModel.VIEW_NAME);
        }
        else if (evt.getSource().equals(logIn)) {
            viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        }
        else if (evt.getSource().equals(returnToHome)) {
            viewManagerModel.setState(HomeViewModel.VIEW_NAME);
        }
        viewManagerModel.notifyViewChanged();
    }
}
