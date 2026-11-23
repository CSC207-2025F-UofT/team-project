package app;

import data_access.FileUserDataAccessObject;
import data_access.InMemoryPetDataAccessObject;
import data_access.GoogleDesktopIdentityGateway;
import data_access.InMemoryTransactionDataAccessObject;
import data_access.InMemoryBudgetDataAccessObject;

import data_access.ApiPetImageDataAccessObject;
import entity.UserFactory;

import interface_adapter.ViewManagerModel;
import interface_adapter.google_login.GoogleLoginController;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.record_transaction.RecordTransactionController;
import interface_adapter.record_transaction.RecordTransactionPresenter;
import interface_adapter.record_transaction.RecordTransactionState;
import interface_adapter.record_transaction.RecordTransactionViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.view_budget_progress.ViewBudgetProgressController;
import interface_adapter.view_budget_progress.ViewBudgetProgressPresenter;
import interface_adapter.view_budget_progress.ViewBudgetProgressViewModel;

import interface_adapter.adopt_pet.AdoptPetViewModel;
import interface_adapter.interact_with_pet.InteractWithPetController;
import interface_adapter.interact_with_pet.InteractWithPetPresenter;
import interface_adapter.interact_with_pet.InteractWithPetViewModel;
import interface_adapter.get_pet_picture.GetPetPictureController;
import interface_adapter.get_pet_picture.GetPetPicturePresenter;
import interface_adapter.adopt_pet.AdoptPetController;
import interface_adapter.adopt_pet.AdoptPetPresenter;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.google_login.GoogleLoginInteractor;
import use_case.google_login.GoogleIdentityGateway;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.record_transaction.RecordTransactionInputBoundary;
import use_case.record_transaction.RecordTransactionInteractor;
import use_case.record_transaction.RecordTransactionOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.view_budget_progress.ViewBudgetProgressInteractor;

import use_case.interact_with_pet.InteractWithPetInteractor;
import use_case.get_pet_picture.GetPetPictureInteractor;
import use_case.get_pet_picture.GetPetPictureOutputBoundary;
import use_case.adopt_pet.AdoptPetInteractor;
import view.LoggedInView;
import view.LoginView;
import view.RecordTransactionView;
import view.SignupView;
import view.ViewManager;
import view.ViewBudgetProgressView;
import view.AdoptPetView;
import view.InteractWithPetView;


import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    private final InMemoryTransactionDataAccessObject transactionDataAccessObject =
            new InMemoryTransactionDataAccessObject(userDataAccessObject);

    private final InMemoryBudgetDataAccessObject budgetDataAccessObject =
            new InMemoryBudgetDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private SwingWorker<String, Void> googleLoginWorker;
    private final InMemoryPetDataAccessObject petDataAccessObject =
            new InMemoryPetDataAccessObject();

    private AdoptPetViewModel adoptPetViewModel;
    private InteractWithPetViewModel interactWithPetViewModel;

    private AdoptPetView adoptPetView;
    private InteractWithPetView interactWithPetView;

    private RecordTransactionView recordTransactionView;
    private RecordTransactionViewModel recordTransactionViewModel;

    private ViewBudgetProgressViewModel budgetProgressViewModel;
    private ViewBudgetProgressView budgetProgressView;

    public AppBuilder() {
        view.settings.ThemeManager.initialize();

        // cardPanel is created before this constructor runs.
        SwingUtilities.updateComponentTreeUI(cardPanel);

        cardPanel.setLayout(cardLayout);

        // FOR TEST PURPOSE
        budgetDataAccessObject.saveDailyLimit(
                "username_test", new java.math.BigDecimal("100.00"));
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, SignupViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, LoginViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, LoggedInViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addAdoptPetView() {
        adoptPetViewModel = new AdoptPetViewModel();
        adoptPetView = new AdoptPetView(adoptPetViewModel);

        cardPanel.add(adoptPetView, AdoptPetViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addInteractWithPetView() {
        interactWithPetViewModel = new InteractWithPetViewModel();
        interactWithPetView =
                new InteractWithPetView(interactWithPetViewModel);

        cardPanel.add(
                interactWithPetView,
                InteractWithPetViewModel.VIEW_NAME
        );
        return this;
    }

    public AppBuilder addInteractWithPetUseCase() {
        InteractWithPetPresenter presenter =
                new InteractWithPetPresenter(
                        adoptPetViewModel,
                        interactWithPetViewModel,
                        viewManagerModel
                );

        InteractWithPetInteractor interactor =
                new InteractWithPetInteractor(
                        petDataAccessObject,
                        presenter
                );

        InteractWithPetController controller =
                new InteractWithPetController(interactor);

        loggedInView.setInteractWithPetController(controller);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        loginView.setShowSignup(() -> {
            viewManagerModel.setState(SignupViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addGoogleLoginUseCase() {
        String clientId = System.getenv("GOOGLE_OAUTH_CLIENT_ID");
        String clientSecret = System.getenv("GOOGLE_OAUTH_CLIENT_SECRET");
        if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            return this;
        }

        return addGoogleLoginUseCase(new GoogleDesktopIdentityGateway(clientId, clientSecret));
    }

    // Package visibility allows tests to supply an identity gateway without a browser.
    AppBuilder addGoogleLoginUseCase(GoogleIdentityGateway gateway) {
        LoginPresenter presenter = new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        loginView.setGoogleLoginCancelAction(() -> {
            SwingWorker<String, Void> pending = googleLoginWorker;
            if (pending != null) {
                // Invalidate first: an already queued completion must not start a session.
                googleLoginWorker = null;
                pending.cancel(true);
                loginView.setGoogleLoginPending(false);
                presenter.prepareFailView("Google sign-in cancelled. You can try again.");
            }
        });
        loginView.setGoogleLoginAction(() -> {
            if (googleLoginWorker != null) {
                return;
            }
            loginView.setGoogleLoginPending(true);
            googleLoginWorker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return gateway.authenticate();
                }

                @Override
                protected void done() {
                    if (googleLoginWorker != this) {
                        return;
                    }
                    googleLoginWorker = null;
                    loginView.setGoogleLoginPending(false);
                    try {
                        String subject = get();
                        // Commit the session on the EDT, serialized with cancellation.
                        new GoogleLoginController(new GoogleLoginInteractor(
                                () -> subject, userDataAccessObject, presenter)).execute();
                    }
                    catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        presenter.prepareFailView("Google sign-in was interrupted.");
                    }
                    catch (ExecutionException exception) {
                        Throwable cause = exception.getCause();
                        String message = cause instanceof java.io.IOException
                                ? cause.getMessage() : "Google sign-in could not finish. Try again.";
                        presenter.prepareFailView(message);
                    }
                }
            };
            googleLoginWorker.execute();
        });
        return this;
    }

    public AppBuilder addRecordTransactionView() {
        recordTransactionViewModel = new RecordTransactionViewModel();
        recordTransactionView = new RecordTransactionView(recordTransactionViewModel);

        cardPanel.add(recordTransactionView, RecordTransactionViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addRecordTransactionUseCase() {
        final RecordTransactionOutputBoundary presenter =
                new RecordTransactionPresenter(recordTransactionViewModel);

        final RecordTransactionInputBoundary interactor =
                new RecordTransactionInteractor(transactionDataAccessObject, presenter);

        final RecordTransactionController controller =
                new RecordTransactionController(interactor);

        recordTransactionView.setRecordTransactionController(controller);

        loggedInView.setShowRecordTransaction(() -> {
            // Start with a fresh form whenever this screen opens.
            recordTransactionViewModel.setState(new RecordTransactionState());
            recordTransactionViewModel.notifyStateChanged();

            viewManagerModel.setState(RecordTransactionViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });

        recordTransactionView.setBackAction(() -> {
            viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });

        return this;
    }

    public AppBuilder addViewBudgetProgressView() {
        budgetProgressViewModel = new ViewBudgetProgressViewModel();
        budgetProgressView = new ViewBudgetProgressView(budgetProgressViewModel);
        cardPanel.add(budgetProgressView, ViewBudgetProgressViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addViewBudgetProgressUseCase() {
        final ViewBudgetProgressPresenter presenter =
                new ViewBudgetProgressPresenter(budgetProgressViewModel);
        final ViewBudgetProgressInteractor interactor =
                new ViewBudgetProgressInteractor(
                        transactionDataAccessObject, budgetDataAccessObject, presenter);
        final ViewBudgetProgressController controller =
                new ViewBudgetProgressController(interactor);
        budgetProgressView.setController(controller);

        loggedInView.setShowBudgetProgress(() -> {
            controller.execute();
            viewManagerModel.setState(ViewBudgetProgressViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });
        budgetProgressView.setBackAction(() -> {
            viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });
        return this;
        }

    public AppBuilder addGetPetPictureUseCase() {
        ApiPetImageDataAccessObject imageDataAccess =
                new ApiPetImageDataAccessObject();

        GetPetPicturePresenter presenter =
                new GetPetPicturePresenter(adoptPetViewModel);

        GetPetPictureOutputBoundary uiOutput =
                new GetPetPictureOutputBoundary() {
                    @Override
                    public void prepareSuccessView(String imageUrl) {
                        SwingUtilities.invokeLater(
                                () -> presenter.prepareSuccessView(imageUrl)
                        );
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        SwingUtilities.invokeLater(
                                () -> presenter.prepareFailView(errorMessage)
                        );
                    }
                };

        GetPetPictureInteractor interactor =
                new GetPetPictureInteractor(imageDataAccess, uiOutput);

        GetPetPictureController controller =
                new GetPetPictureController(interactor);

        adoptPetView.setGetPetPictureController(controller);

        return this;
    }

    public AppBuilder addAdoptPetUseCase() {
        AdoptPetPresenter presenter = new AdoptPetPresenter(
                adoptPetViewModel,
                interactWithPetViewModel,
                viewManagerModel
        );

        AdoptPetInteractor interactor = new AdoptPetInteractor(
                petDataAccessObject,
                presenter
        );

        AdoptPetController controller = new AdoptPetController(interactor);
        adoptPetView.setAdoptPetController(controller);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("My Virtual Pet");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        view.settings.SettingsMenu.install(application);

        return application;
    }


}
