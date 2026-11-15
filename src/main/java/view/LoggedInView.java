package view;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInViewModelListener;

import javax.swing.*;

public class LoggedInView extends JPanel implements LoggedInViewModelListener {
    private final LoggedInViewModel loggedInViewModel;
    private final JLabel username = new JLabel();

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Logged in as:"));
        add(username);
    }

    @Override
    public void stateChanged() {
        final LoggedInState state = loggedInViewModel.getState();
        username.setText(state.getUsername());
    }
}
