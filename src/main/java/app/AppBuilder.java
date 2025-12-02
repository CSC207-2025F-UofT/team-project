package app;

import dataAccess.FileUserDataAccessObject;
import dataAccess.PetDataAccessInterface;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.FilterPets.PetListViewModel;
import interface_adapter.ViewPets.ViewPetsController;
import interface_adapter.ViewPets.ViewPetsPresenter;
import interface_adapter.homepage.LoginChoosePresenter;
import interface_adapter.homepage.LoginChooseViewModel;
import interface_adapter.sign_up.SignupController;
import interface_adapter.sign_up.SignupPresenter;
import interface_adapter.sign_up.SignupViewModel;
import interface_adapter.update_profile.UpdateUserProfileController;
import interface_adapter.update_profile.UpdateUserProfilePresenter;
import interface_adapter.user_login.UserLoginController;
import interface_adapter.user_login.UserLoginPresenter;
import interface_adapter.user_login.UserLoginViewModel;
import interface_adapter.user_logout.UserLogoutController;
import interface_adapter.user_logout.UserLogoutPresenter;
import service.Backend;
import service.Frontend;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.update_profile.UpdateUserProfileInputBoundary;
import use_case.update_profile.UpdateUserProfileInteractor;
import use_case.update_profile.UpdateUserProfileOutputBoundary;
import use_case.user_login.UserLoginInputBoundary;
import use_case.user_login.UserLoginInteractor;
import use_case.user_login.UserLoginOutputBoundary;
import use_case.user_logout.UserLogoutInputBoundary;
import use_case.user_logout.UserLogoutInteractor;
import use_case.user_logout.UserLogoutOutputBoundary;
import use_case.view_pets.ViewPetsInputBoundary;
import use_case.view_pets.ViewPetsInteractor;
import use_case.view_pets.ViewPetsOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;
/**
 * A builder class used to register all views, view models, controllers, and use cases
 * for the application and attach them to the Frontend. This class uses the Builder pattern
 * to allow chained configuration steps.
 */
public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager;

    private final FileUserDataAccessObject userDataAccessObject;
//    private final PetDataAccessInterface petDataAccess;

    private final Frontend frontend;
    private final Backend backend;

    // ViewModels
    private final LoginChooseViewModel loginChooseViewModel = new LoginChooseViewModel();
    private final UserLoginViewModel userLoginViewModel = new UserLoginViewModel();
    private final SignupViewModel signupViewModel= new SignupViewModel();
    private final PetListViewModel petListViewModel = new PetListViewModel();
    private ViewPetsController viewPetsController;
    
    // Views
    private LoginChooseView loginChooseView;
    private CreateUserAccountView createUserAccountView;
    private UserLoginView userLoginView;


    /**
     * Constructs an AppBuilder that initializes all required managers and shared
     * components for the application UI.
     *
     * @param frontend The frontend window where views will be displayed.
     * @param dao      The DAO used for accessing user data.
     */
//    public AppBuilder(Frontend frontend, Backend backend, FileUserDataAccessObject dao, PetDataAccessInterface petDataAccess) {
    public AppBuilder(Frontend frontend, Backend backend, FileUserDataAccessObject dao) {
        this.frontend = frontend;
        this.backend = backend;
        this.userDataAccessObject = dao;
        this.viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the LoginChoose view into the application.
     *
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addLoginChooseView() {
        loginChooseView = new LoginChooseView(loginChooseViewModel);
        cardPanel.add(loginChooseView, loginChooseView.getViewName());
        return this;
    }

    /**
     * Adds the presenter for the LoginChoose view.
     *
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addLoginChoosePresenter() {

//        LoginChoosePresenter presenter =
//                new LoginChoosePresenter(viewManagerModel, userLoginView);

        LoginChoosePresenter presenter =
                new LoginChoosePresenter(viewManagerModel, userLoginViewModel);
        loginChooseViewModel.addPropertyChangeListener(presenter);

        return this;
    }

    /**
     * Adds the CreateUserAccountView to the application.
     *
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addCreateUserAccountView() {
        createUserAccountView = new CreateUserAccountView(signupViewModel);
        cardPanel.add(createUserAccountView, createUserAccountView.getViewName());
        return this;
    }

    /**
     * Adds the UserLogin view and registers it with the ViewManager.
     *
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addUserLoginView() {
        userLoginView = new UserLoginView(userLoginViewModel);
        cardPanel.add(userLoginView, userLoginView.getViewName());
        return this;
    }

    /**
     * Registers the UserLogin use case (controller, interactor, presenter).
     *
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addUserLoginUseCase() {

        UserLoginOutputBoundary output =
                new UserLoginPresenter(viewManagerModel, userLoginViewModel, frontend, backend);

        UserLoginInputBoundary interactor =
                new UserLoginInteractor(userDataAccessObject, output, backend);

        UserLoginController controller = new UserLoginController(interactor);
        userLoginView.setLoginController(controller);

        return this;
    }

    /**
     * Registers the UserLogout use case (controller, interactor, presenter).
     *
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addUserLogoutUseCase() {

        final UserLogoutOutputBoundary presenter =
                new UserLogoutPresenter(viewManagerModel, userLoginViewModel, frontend);

        final UserLogoutInputBoundary interactor =
                new UserLogoutInteractor(userDataAccessObject, presenter);

        final UserLogoutController controller = new UserLogoutController(interactor);
        frontend.setUserLogoutController(controller);

        return this;
    }

    /**
     * Adds dashboard views for admin, staff, and user, based on role.
     *
     * @param backend The backend instance providing data access.
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addDashboardViews(Backend backend) {

        final JPanel admin = new MainDashboardView(frontend, backend, "admin");
        final JPanel staff = new MainDashboardView(frontend, backend, "staff");
        final JPanel user = new MainDashboardView(frontend, backend, "user");
        
        cardPanel.add(admin, "adminDashboard");
        cardPanel.add(staff, "staffDashboard");
        cardPanel.add(user, "userDashboard");

        return this;
    }

    /**
     * Registers the Signup use case.
     *
     * @return this AppBuilder instance for chaining.
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, userLoginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        createUserAccountView.setSignupController(controller);
        return this;
    }
    

    public AppBuilder addUpdateProfileUseCase() {

    	final UpdateUserProfileOutputBoundary output =
                new UpdateUserProfilePresenter(viewManagerModel, frontend);

    	final UpdateUserProfileInputBoundary interactor =
                new UpdateUserProfileInteractor(userDataAccessObject, output);

        UpdateUserProfileController controller =
                new UpdateUserProfileController(interactor);

        frontend.setUpdateProfileController(controller);

        return this;
    }


    public AppBuilder addViewPetsUseCase(Backend backend) {

        ViewPetsOutputBoundary presenter =
                new ViewPetsPresenter(petListViewModel);

        ViewPetsInputBoundary interactor =
                new ViewPetsInteractor(backend, presenter);

        viewPetsController =
                new ViewPetsController(interactor, petListViewModel);

        frontend.setViewPetsController(viewPetsController);

        return this;
    }

    
    /**
     * Finalizes the building of the app by attaching the card panel to the frontend
     * and refreshing the UI.
     */
    public void build() {
        frontend.setCardPanel(cardPanel, cardLayout);
        frontend.revalidate();
        frontend.repaint();
    }
    
}
