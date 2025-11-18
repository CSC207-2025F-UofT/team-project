package view;

import interface_adapter.loggedInHomePage.LoggedInHomePageController;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoggedInHomePageView extends JPanel implements ActionListener, PropertyChangeListener {
    //** DEFINING VARIABLES **//
    private final String viewName = "logged in home page";
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    LoggedInHomePageController loggedInHomePageController = null;

    private final JButton toSearch;
    private final JButton toFavourites;

    //** CONSTRUCTOR **//
    public LoggedInHomePageView(LoggedInHomePageViewModel loggedInHomePageViewModel) {
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;

        final JPanel buttons = new JPanel();
        //** Go to search page button **//
        toSearch = new JButton(LoggedInHomePageViewModel.TO_SEARCH_LABEL);
        buttons.add(toSearch);
        //** Go to Favourites page button **//
        toFavourites = new JButton(LoggedInHomePageViewModel.TO_FAVOURITES_LABEL);
        buttons.add(toFavourites);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(buttons);

        toSearch.addActionListener(
                e -> loggedInHomePageController.switchToLoggedInSearchView()
        );

        toFavourites.addActionListener(
                e -> loggedInHomePageController.switchToLoggedInFavouritesView()
        );
    }

    //** CREATE AN INSTANCE OF THE CONTROLLER **//
    public void setHomePageController(LoggedInHomePageController controller) {
        this.loggedInHomePageController = controller;
    }

    //** RETURNS NAME OF THIS VIEW **//
    public String getViewName() {return viewName;}

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
