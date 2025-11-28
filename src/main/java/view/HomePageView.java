package view;


import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;

import javax.imageio.plugins.tiff.TIFFTag;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HomePageView extends JPanel implements PropertyChangeListener {
    private final String viewName = "Home page";

    private final HomePageViewModel homePageViewModel;
    HomePageController homePageController = null;

    private final JButton logInButton;
    private final JButton createAccountButton;

    // ** CONSTRUCTOR ** //
    public HomePageView(HomePageViewModel homePageViewModel) {
        this.homePageViewModel = homePageViewModel;

        final JLabel title = new JLabel(HomePageViewModel.WELCOME_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        //** BUTTONS **//
        final JPanel buttons = new JPanel();

        createAccountButton = new JButton(HomePageViewModel.TO_CREATE_ACCOUNT_LABEL);
        buttons.add(createAccountButton);

        logInButton = new JButton(HomePageViewModel.TO_LOGIN_LABEL);
        buttons.add(logInButton);


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20)); // spacing under title
        this.add(buttons);

        createAccountButton.addActionListener(
                e -> homePageController.switchToCreateAccountView()
        );

        logInButton.addActionListener(
                e -> homePageController.switchToSigninView()
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void setHomePageController(HomePageController controller) {
        this.homePageController = controller;
    }

    public String getViewName() {return viewName;}

}
