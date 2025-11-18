package app;


import app.GUI.GUI;
import view.CreateAccountView;
import view.HomePageView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();
        JFrame application = gui
                .addHomePageView()
                .addCreateAccountView()
                .addHomePageUseCase()
                .addLoggedInHomePageView()
                .addLoggedInSearchView()
                .addLoggedInFavouriteView()
                .addLoggedInHomePageUseCases()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
