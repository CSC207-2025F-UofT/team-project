package app;

import javax.swing.*;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import service.Backend;
import service.Frontend;

public class Main {

    public static void main(String[] args) {

        // 1 Backend Data Loading
        Backend backend = new Backend();
        backend.fromJsonFiles(
                "users.json",
                "pets.json",
                "admins.json",
                "applications.json"
        );

        // 2 Create a single window Frontend
        Frontend frontend = new Frontend(backend);

        // 3 User DAO
        UserFactory userFactory = new UserFactory();
        FileUserDataAccessObject userDao =
                new FileUserDataAccessObject("users.csv", userFactory);

        // 4 Use AppBuilder to deploy LoginChoose/Login/Signup, and other screens to the frontend.
//        AppBuilder appBuilder = new AppBuilder(frontend, userDao);
        AppBuilder appBuilder = new AppBuilder(frontend, userDao);

        appBuilder
                .addLoginChooseView()
                .addUserLoginView()
                .addLoginChoosePresenter()
                .addCreateUserAccountView()
                .addDashboardViews(backend)
                .addUserLoginUseCase()
                .addUserLogoutUseCase()
                .addSignupUseCase()
                .build();

        // 5 show Frontend
        frontend.pack();
        frontend.setLocationRelativeTo(null);
        frontend.setVisible(true);
        frontend.setSize(1200, 800);

    }
}
