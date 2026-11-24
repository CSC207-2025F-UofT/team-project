package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;

/**
 * Placeholder shown after logging in. Replace with the adopter home page
 * (browse pets) or the admin dashboard when those parts are ready.
 * Log out is handled by another teammate.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {
    private final JLabel welcomeLabel = FormUtils.title("Welcome!");

    public LoggedInView(LoggedInViewModel viewModel) {
        setLayout(new BorderLayout());
        setName(LoggedInViewModel.VIEW_NAME);
        viewModel.addPropertyChangeListener(this);

        final JPanel content = FormUtils.verticalPanel();
        content.add(welcomeLabel);
        add(content, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoggedInState state = (LoggedInState) evt.getNewValue();
        welcomeLabel.setText("Welcome, " + state.getUsername() + " (" + state.getRole() + ")");
    }
}
