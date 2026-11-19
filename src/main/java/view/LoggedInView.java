package view;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Screen shown after login.
 * PLACEHOLDER: the dashboard owner replaces the CENTER area with the real dashboard.
 * The top bar (username + Log out) stays.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {
    private final LoggedInViewModel loggedInViewModel;
    private LogoutController logoutController;

    private final JLabel usernameLabel = new JLabel();

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        loggedInViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(e -> logoutController.execute());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.add(usernameLabel);
        topBar.add(logoutButton);

        JLabel placeholder = new JLabel("Dashboard goes here", JLabel.CENTER);
        placeholder.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        add(topBar, BorderLayout.NORTH);
        add(placeholder, BorderLayout.CENTER);
    }

    public String getViewName() {
        return loggedInViewModel.getViewName();
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoggedInState state = (LoggedInState) evt.getNewValue();
        usernameLabel.setText("Logged in as: " + state.getUsername());
    }
}
