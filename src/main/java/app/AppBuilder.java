package app;

import data_access.FileUserDataAccessObject;
import data_access.GoogleDesktopIdentityGateway;
import data_access.InMemoryTransactionDataAccessObject;

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

import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.google_login.GoogleLoginInteractor;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.record_transaction.RecordTransactionInputBoundary;
import use_case.record_transaction.RecordTransactionInteractor;
import use_case.record_transaction.RecordTransactionOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;

import view.LoggedInView;
import view.LoginView;
import view.RecordTransactionView;
import view.SignupView;
import view.ViewManager;

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

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;

    private RecordTransactionView recordTransactionView;
    private RecordTransactionViewModel recordTransactionViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
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

        LoginPresenter presenter = new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        LoginOutputBoundary uiOutput = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData response) {
                SwingUtilities.invokeLater(() -> presenter.prepareSuccessView(response));
            }

            @Override
            public void prepareFailView(String error) {
                SwingUtilities.invokeLater(() -> presenter.prepareFailView(error));
            }
        };
        GoogleLoginController controller = new GoogleLoginController(new GoogleLoginInteractor(
                new GoogleDesktopIdentityGateway(clientId, clientSecret), userDataAccessObject, uiOutput));
        loginView.setGoogleLoginAction(() -> {
            loginView.setGoogleLoginPending(true);
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    controller.execute();
                    return null;
                }

                @Override
                protected void done() {
                    loginView.setGoogleLoginPending(false);
                    try {
                        get();
                    }
                    catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        presenter.prepareFailView("Google sign-in was interrupted.");
                    }
                    catch (ExecutionException exception) {
                        presenter.prepareFailView("Google sign-in could not finish. Try again.");
                    }
                }
            }.execute();
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

    public JFrame build() {
        final JFrame application = new JFrame("My Virtual Pet");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }


}
