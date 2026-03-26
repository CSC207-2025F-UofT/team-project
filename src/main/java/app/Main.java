package app;


import api.googlePlacesAPI.PlaceFetcher;
import app.GUI.Flatlaf;
import app.GUI.GUI;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Flatlaf.init();
        GUI gui = new GUI();
        JFrame application = gui
                .addHomePageView()
                .addCheckOutfitView()
                .addCreateAccountView()
                .addSignInView()
                .addLoggedInFavouritePageView()
                .addLoggedInHomePageView()
                .addLoggedInSearchPageView()
                .addWeatherReportView()
                .addSettingsView()
                .addHourlyForecastView()
                .addCreateAccountUseCases()
                .addHomePageUseCase()
                .addSignInViewUseCase()
                .addLoggedInSearchPageUseCases()
                .addLoggedInFavouritePageUseCases()
                .addLoggedInHomePageUseCases()
                .addCurrentWeatherUseCases()
                .addSettingsUseCases()
                .addCheckOutfitUseCases()
                .addHourlyForecastUseCases()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
