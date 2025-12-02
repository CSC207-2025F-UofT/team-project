package app;

import dataAccess.FileUserDataAccessObject;
import entity.UserFactory;
import service.Backend;
import service.Frontend;

public class Main {
    /**
     * The entry point of the application.
     *
     * @param args the input arguments
     */

    public static void main(String[] args) {
        final int frontendWidth = 1200;
        final int frontendHeight = 800;

        // 1 Backend Data Loading
        final Backend backend = new Backend();
        backend.fromJsonFiles(
                "users.json",
                "pets.json",
                "applications.json"
        );

        // 2 Create a single window Frontend
        final Frontend frontend = new Frontend(backend);

        // 3 User DAO
        final UserFactory userFactory = new UserFactory();
        final FileUserDataAccessObject userDao =
                new FileUserDataAccessObject("users.json", userFactory);

        // 4 Use AppBuilder to deploy LoginChoose/Login/Signup, and other screens to the frontend.
        final AppBuilder appBuilder = new AppBuilder(frontend, backend, userDao);

        appBuilder
                .addLoginChooseView()
                .addUserLoginView()
                .addCreateUserAccountView()
                .addLoginChoosePresenter()
                .addDashboardViews(backend)
                .addUpdateProfileUseCase()
                .addViewPetsUseCase(backend)
                .addUserLoginUseCase()
                .addUserLogoutUseCase()
                .addSignupUseCase()
                .build();

        // 5 show Frontend
        frontend.pack();
        frontend.setLocationRelativeTo(null);
        frontend.setVisible(true);
        frontend.setSize(frontendWidth, frontendHeight);

    }
}
