package view;

import interface_adapter.createAccount.CreateAccountController;
import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.loggedInFavourites.LoggedInFavouritesController;
import interface_adapter.loggedInFavourites.LoggedInFavouritesViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoggedInFavouritesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Logged In Favourites View";
    private final LoggedInFavouritesViewModel loggedInFavouritesViewModel;
    LoggedInFavouritesController loggedInFavouritesController  = null;

    private final JButton createAccount;


    public LoggedInFavouritesView(LoggedInFavouritesViewModel loggedInFavouritesViewModel) {
        this.loggedInFavouritesViewModel = loggedInFavouritesViewModel;

        final JPanel buttons = new JPanel();
        createAccount = new JButton(HomePageViewModel.TO_CREATE_ACCOUNT_LABEL);
        buttons.add(createAccount);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(buttons);
    }

    public void setCreateController(LoggedInFavouritesController controller) {
        this.loggedInFavouritesController = controller;
    }

    public String getViewName() {return viewName;}

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
