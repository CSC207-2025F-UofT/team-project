package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.accounts.AccountsViewModel;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The default view shown when the application starts.
 */
public class HomeView extends JPanel implements ActionListener {
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final JButton accounts;

    public HomeView(ViewManagerModel viewManagerModel, LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;

        final JLabel title = new JLabel("Home");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = new JPanel();
        accounts = new JButton("Accounts");
        buttons.add(accounts);
        accounts.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(buttons);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(accounts)) {
            final String username = loggedInViewModel.getState().getUsername();
            if (username != null && !username.isEmpty()) {
                viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
            }
            else {
                viewManagerModel.setState(AccountsViewModel.VIEW_NAME);
            }
            viewManagerModel.notifyViewChanged();
        }
    }
}
