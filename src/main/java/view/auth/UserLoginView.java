package view.auth;

import interface_adapter.user_login.UserLoginController;
import interface_adapter.user_login.UserLoginState;
import interface_adapter.user_login.UserLoginViewModel;
import view.common.UIFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
/**
 * The View for the Login Use Case.
 */

public class UserLoginView extends JPanel implements ActionListener, PropertyChangeListener {

    // not change
    private final String viewName = "login";

    private final UserLoginViewModel UserloginViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton logIn;
    private final JButton goBack;
    private UserLoginController userLoginController = null;

    private String userType = "user";
    private JLabel titleLabel;

    public UserLoginView(UserLoginViewModel userloginViewModel) {
        this.UserloginViewModel  = userloginViewModel;
        this.UserloginViewModel.addPropertyChangeListener(this);
        userType = userloginViewModel.getState().getUserType();

        this.titleLabel = new JLabel(formatTitle(userType));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(60, 60, 75));

        // ====== UI ======
        setBackground(new Color(0xF5F7FB));
        setLayout(new GridBagLayout()); // 居中

        // middle
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 320));
        // ====== end ======

        // put LabelTextPanel into card
        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

        UIFactory.styleTextField(usernameInputField);
        UIFactory.styleTextField(passwordInputField);

        final JPanel buttons = new JPanel();
        buttons.setOpaque(false);

        logIn = UIFactory.createPrimaryButton("Log in");
        goBack = UIFactory.createSecondaryButton("Go Back");
        buttons.add(logIn);
        buttons.add(goBack);

        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(20));

        card.add(usernameInfo);
        card.add(usernameErrorField);
        card.add(Box.createVerticalStrut(10));

        card.add(passwordInfo);
        card.add(passwordErrorField);
        card.add(Box.createVerticalStrut(20));

        card.add(buttons);
        card.add(Box.createVerticalStrut(10));

        add(card);

        logIn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(logIn)) {
                            final UserLoginState currentState = UserloginViewModel.getState();

                            userLoginController.execute(
                                    userType,
                                    currentState.getUsername(),
                                    currentState.getUserPassword()
                            );
                        }
                    }
                }
        );

        goBack.addActionListener(e -> {
            if (userLoginController != null) {
                userLoginController.goBack();
            }
        });

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final UserLoginState currentState = userloginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                userloginViewModel.setState(currentState);
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

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final UserLoginState currentState = userloginViewModel.getState();
                currentState.setUserPassword(new String(passwordInputField.getPassword()));
                userloginViewModel.setState(currentState);
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
    }

    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final UserLoginState state = (UserLoginState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getLoginError());
        
        String userType = state.getUserType();
        this.userType = userType;

        switch (userType) {
	        case "user":
	            titleLabel.setText("User Login");
	            break;

            case "admin":
                titleLabel.setText("Admin Login");
                break;

            case "staff":
                titleLabel.setText("Staff Login");
                break;

            default:
                break;
        }
    }

    private void setFields(UserLoginState state) {
        usernameInputField.setText(state.getUsername());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(UserLoginController loginController) {
        this.userLoginController = loginController;
    }

    public void setUserType(String userType) {
        this.userType = userType;
        updateTitle();
    }

    public void updateTitle() {
        this.titleLabel.setText(formatTitle(userType));

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.setTitle(getTitleText());
        }
        titleLabel.setText(getTitleText());

        revalidate();
        repaint();
    }

    private String formatTitle(String type) {
        return type.substring(0, 1).toUpperCase() + type.substring(1) + " Login";
    }

    public String getTitleText() {
        return formatTitle(userType);
    }
}
