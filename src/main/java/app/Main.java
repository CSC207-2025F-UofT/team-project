package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();

        JFrame application = appBuilder;
            .addSignupView();

        application.pack(); //Automatically sizes JFrame to fit the component inside
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
