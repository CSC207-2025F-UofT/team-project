package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.Signup.SignupController;
import interface_adapter.Signup.SignupState;
import interface_adapter.Signup.SignupViewModel;
import interface_adapter.Signup.SignupViewModelListener;

/**
 * The View for when the user is signing up for the program.
 */
public class SignupView extends JPanel implements ActionListener, SignupViewModelListener {

    private final SignupViewModel signupViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JCheckBox researcherCheckBox = new JCheckBox("I am a researcher");
    private final JLabel signupErrorField = new JLabel();

    private final JButton signUp;
    private final JButton logIn;
    private SignupController signupController = null;

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        this.signupViewModel.addListener(this);

        final JLabel title = new JLabel("Sign Up Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);

        researcherCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupErrorField.setForeground(Color.RED);

        final JPanel buttons = new JPanel();
        signUp = new JButton("sign up");
        buttons.add(signUp);
        logIn = new JButton("log in");
        buttons.add(logIn);

        signUp.addActionListener(this);
        logIn.addActionListener(this);

        usernameInputField.getDocument().addDocumentListener(new SimpleDocumentListener(() -> {
            final SignupState currentState = signupViewModel.getState();
            currentState.setUsername(usernameInputField.getText());
            signupViewModel.setState(currentState);
        }));

        researcherCheckBox.addActionListener(evt -> {
            final SignupState currentState = signupViewModel.getState();
            currentState.setResearcher(researcherCheckBox.isSelected());
            signupViewModel.setState(currentState);
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(usernameInfo);
        this.add(researcherCheckBox);
        this.add(signupErrorField);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(signUp)) {
            final SignupState currentState = signupViewModel.getState();
            signupController.execute(currentState.getUsername(), currentState.isResearcher());
        }
        else if (evt.getSource().equals(logIn)) {
            signupController.switchToLoginView();
        }
    }

    @Override
    public void stateChanged() {
        final SignupState state = signupViewModel.getState();
        if (!usernameInputField.getText().equals(state.getUsername())) {
            usernameInputField.setText(state.getUsername());
        }
        researcherCheckBox.setSelected(state.isResearcher());
        signupErrorField.setText(state.getSignupError());
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }
}
