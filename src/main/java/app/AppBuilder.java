package app;

import data_access.FileUserDataAccessObject;
import data_access.InMemoryTransactionDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.upload_statement.UploadStatementController;
import interface_adapter.upload_statement.UploadStatementPresenter;
import interface_adapter.upload_statement.UploadStatementViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.upload_statement.UploadStatementInputBoundary;
import use_case.upload_statement.UploadStatementInteractor;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    final InMemoryTransactionDataAccessObject transactionDataAccessObject = new InMemoryTransactionDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private UploadStatementViewModel uploadStatementViewModel;
    private UploadStatementView uploadStatementView;

    // Transaction Categorizer
    private TransactionCategorizerView transactionCategorizerView;
    private CategorizerViewModel categorizerViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    // ----------------- Views -----------------
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addUploadStatementView() {
        uploadStatementViewModel = new UploadStatementViewModel();
        uploadStatementView = new UploadStatementView(uploadStatementViewModel);
        cardPanel.add(uploadStatementView, uploadStatementView.getViewName());
        return this;
    }

    public AppBuilder addTransactionCategorizerView() {
        categorizerViewModel = new CategorizerViewModel();
        transactionCategorizerView = new TransactionCategorizerView(categorizerViewModel);
        cardPanel.add(transactionCategorizerView, transactionCategorizerView.getViewName());
        return this;
    }

    // ----------------- Use Cases -----------------
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
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        return this;
    }

    public AppBuilder addUploadStatementUseCase() {
        final UploadStatementPresenter uploadStatementOutputBoundary =
                new UploadStatementPresenter(viewManagerModel, uploadStatementViewModel);

        final UploadStatementInputBoundary uploadStatementInteractor = new UploadStatementInteractor(transactionDataAccessObject,
                uploadStatementOutputBoundary);
        UploadStatementController uploadStatementController = new UploadStatementController(uploadStatementInteractor);
        loggedInView.setUploadStatementController(uploadStatementController);
        return this;
    }

    public AppBuilder addTransactionCategorizerUseCase() {
        // The view itself handles dummy categorization, no complex use case needed
        return this;
    }

    // ----------------- Build -----------------
    public JFrame build() {
        final JFrame application = new JFrame("Finance App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        // Default to signup view
        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

    // ----------------- Getters -----------------
    public TransactionCategorizerView getTransactionCategorizerView() {
        if (transactionCategorizerView == null) {
            throw new IllegalStateException("TransactionCategorizerView not initialized. Call addTransactionCategorizerView() first.");
        }
        return transactionCategorizerView;
    }

    public CategorizerViewModel getCategorizerViewModel() {
        if (categorizerViewModel == null) {
            throw new IllegalStateException("CategorizerViewModel not initialized. Call addTransactionCategorizerView() first.");
        }
        return categorizerViewModel;
    }
}
