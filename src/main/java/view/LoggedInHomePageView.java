package view;

import data_access.FileUserDataAccessObjectWithLocations;
import entity.user.User;
import interface_adapter.loggedInHomePage.LoggedInHomePageController;
import interface_adapter.loggedInHomePage.LoggedInHomePageState;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoggedInHomePageView extends JPanel implements PropertyChangeListener {
    //** DEFINING VARIABLES **//
    private final String viewName = "logged in home page";
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    LoggedInHomePageController loggedInHomePageController = null;
    private LogoutController logoutController = null;
    private FileUserDataAccessObjectWithLocations fileUserDataAccessObjectWithLocations;

    private final JLabel title;
    private final JButton toSearch;
    private final JButton toFavourites;
    private final JButton settings;

    //** CONSTRUCTOR **//
    public LoggedInHomePageView(LoggedInHomePageViewModel loggedInHomePageViewModel,
                                FileUserDataAccessObjectWithLocations fileUserDataAccessObjectWithLocations) {
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.fileUserDataAccessObjectWithLocations = fileUserDataAccessObjectWithLocations;

        loggedInHomePageViewModel.addPropertyChangeListener(this);

        // ===== TITLE =====
        title = new JLabel(LoggedInHomePageViewModel.WELCOME_LABEL, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== Top buttons row =====
        toSearch = new JButton(LoggedInHomePageViewModel.TO_SEARCH_LABEL);
        toFavourites = new JButton(LoggedInHomePageViewModel.TO_FAVOURITES_LABEL);
        settings = new JButton(LoggedInHomePageViewModel.SETTINGS_LABEL);

        final JPanel buttonsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        buttonsRow.add(toSearch);
        buttonsRow.add(toFavourites);
        buttonsRow.add(settings);
        buttonsRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // ===== Outer centering + spacing =====
        this.removeAll();
        this.setLayout(new GridBagLayout());

        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        content.setPreferredSize(new Dimension(560, 420));
        content.add(title);
        content.add(Box.createVerticalStrut(16));

        content.add(buttonsRow);

        content.add(Box.createVerticalStrut(18));
        content.add(new JSeparator());
        content.add(Box.createVerticalStrut(18));
        this.add(content);

        //** EVENT LISTENERS **//
        toSearch.addActionListener(
                e -> loggedInHomePageController.switchToLoggedInSearchView()
        );

        toFavourites.addActionListener(
                e -> loggedInHomePageController.switchToLoggedInFavouritesView()
        );

        settings.addActionListener(e -> {
            LoggedInHomePageState state = loggedInHomePageViewModel.getState();
            loggedInHomePageController.switchToSettings(state.getUsername());
        });
    }

    //** CREATE AN INSTANCE OF THE CONTROLLER **//
    public void setHomePageController(LoggedInHomePageController controller) {
        this.loggedInHomePageController = controller;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    //** RETURNS NAME OF THIS VIEW **//
    public String getViewName() {return viewName;}

    // WHEN SET STATE / FIRE PROPERTY CHANGED (AKA LOG IN) CALL THIS:
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoggedInHomePageState state = loggedInHomePageViewModel.getState();
        // Update the text of the JLabel directly
        title.setText(LoggedInHomePageViewModel.WELCOME_LABEL + " " + state.getUsername());
    }
}
