package view;

import interface_adapter.createAccount.CreateAccountController;
import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.loggedInSearch.LoggedInSearchController;
import interface_adapter.loggedInSearch.LoggedInSearchViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoggedInSeachView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Logged In Search View";
    private final LoggedInSearchViewModel loggedInSearchViewModel;
    LoggedInSearchController loggedInSearchController = null;

    private final JButton createAccount;


    public LoggedInSeachView(LoggedInSearchViewModel loggedInSearchViewModel) {
        this.loggedInSearchViewModel = loggedInSearchViewModel;

        final JPanel buttons = new JPanel();
        createAccount = new JButton(HomePageViewModel.TO_CREATE_ACCOUNT_LABEL);
        buttons.add(createAccount);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(buttons);
    }

    public void setCreateController(LoggedInSearchController controller) {
        this.loggedInSearchController = controller;
    }

    public String getViewName() {return viewName;}

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
