package org.example.cocktailcraft.app;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addSearchView()
                .addSearchUseCase()
                .addClickUseCase()
                .addCustomizeUseCase(() -> null)
                .addMainPageView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);

        }
    }

