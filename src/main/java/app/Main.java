package app;


import api.googlePlacesAPI.GooglePlacesFetcher;
import api.googlePlacesAPI.PlaceFetcher;
import app.GUI.GUI;
import entity.placeSuggestions.PlaceSuggestion;
import view.CreateAccountView;
import view.HomePageView;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws PlaceFetcher.PlaceNotFoundException {
        GUI gui = new GUI();
        JFrame application = gui
                .addHomePageView()
                .addCreateAccountView()
                .addSignInView()
                .addLoggedInFavouritePageView()
                .addLoggedInHomePageView()
                .addLoggedInSearchPageView()
                .addWeatherReportView()
                .addCreateAccountUseCases()
                .addHomePageUseCase()
                .addSignInViewUseCase()
                .addLoggedInSearchPageUseCases()
                .addLoggedInFavouritePageUseCases()
                .addLoggedInHomePageUseCases()
                .addCurrentWeatherUseCases()
                .addLogoutUseCases()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
