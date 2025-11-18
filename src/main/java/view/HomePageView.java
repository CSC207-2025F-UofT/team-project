package view;


import interface_adapter.homepage.HomePageController;
import interface_adapter.homepage.HomePageViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HomePageView extends JPanel implements PropertyChangeListener {
    private final String viewName = "home page";
    private final HomePageViewModel homePageViewModel;
    HomePageController homePageController = null;

    private final JLabel welcomeLabel = new JLabel("Welcome!");
    private final JButton logInButton;
    private final JButton createAccountButton;


    public HomePageView(HomePageViewModel homePageViewModel) {
        this.homePageViewModel = homePageViewModel;

        final JPanel buttons = new JPanel();

        createAccountButton = new JButton(HomePageViewModel.TO_CREATE_ACCOUNT_LABEL);
        buttons.add(createAccountButton);

        logInButton = new JButton(HomePageViewModel.TO_LOGIN_LABEL);
        buttons.add(logInButton);


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(buttons);

        createAccountButton.addActionListener(
                e -> homePageController.switchToCreateAccountView()
        );

        logInButton.addActionListener(e -> homePageController.switchToLoginView());

        this.add(Box.createVerticalStrut(40));
        this.add(welcomeLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(buttons);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void setHomePageController(HomePageController controller) {
        this.homePageController = controller;
    }

    public String getViewName() {return viewName;}
}
