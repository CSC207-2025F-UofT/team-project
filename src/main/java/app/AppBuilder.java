package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.CSVTransactionDAO;
import data_access.FileSpendingLimitsDAO;
import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.categorizer.CategorizerController;
import interface_adapter.categorizer.CategorizerPresenter;
import interface_adapter.categorizer.CategorizerViewModel;
import interface_adapter.filter_search.FilterSearchController;
import interface_adapter.filter_search.FilterSearchPresenter;
import interface_adapter.filter_search.FilterSearchViewModel;
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
import interface_adapter.spending_limits.SpendingLimitsController;
import interface_adapter.spending_limits.SpendingLimitsPresenter;
import interface_adapter.spending_limits.SpendingLimitsViewModel;
import interface_adapter.upload_statement.UploadStatementController;
import interface_adapter.upload_statement.UploadStatementPresenter;
import interface_adapter.upload_statement.UploadStatementViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.filter_search.FilterSearchInputBoundary;
import use_case.filter_search.FilterSearchInteractor;
import use_case.filter_search.FilterSearchOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.spending_limits.SpendingLimitsInputBoundary;
import use_case.spending_limits.SpendingLimitsInteractor;
import use_case.spending_limits.SpendingLimitsOutputBoundary;
import use_case.spending_report.GenerateReportController;
import use_case.spending_report.GenerateReportInteractor;
import use_case.spending_report.GenerateReportPresenter;
import use_case.spending_report.SpendingReportViewModel;
import use_case.transaction_categorizer.CategorizerInputBoundary;
import use_case.transaction_categorizer.CategorizerInteractor;
import use_case.transaction_categorizer.CategorizerOutputBoundary;
import use_case.transaction_categorizer.GeminiClient;
import use_case.transaction_categorizer.TransactionCategorizerService;
import use_case.upload_statement.UploadStatementInputBoundary;
import use_case.upload_statement.UploadStatementInteractor;
import view.FilterSearchView;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.SpendingLimitsView;
import view.SpendingReportView;
import view.TransactionCategorizerView;
import view.UploadStatementView;
import view.ViewManager;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);
    final CSVTransactionDAO transactionDataAccessObject = new CSVTransactionDAO("transactions.csv");

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private SpendingLimitsViewModel spendingLimitsViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private UploadStatementViewModel uploadStatementViewModel;
    private UploadStatementView uploadStatementView;
    private SpendingLimitsView spendingLimitsView;
    private SpendingReportView spendingReportView;
    private SpendingReportViewModel spendingReportViewModel;
    private CategorizerViewModel categorizerViewModel;
    private TransactionCategorizerView categorizerView;
    private FilterSearchViewModel filterSearchViewModel;
    private FilterSearchView filterSearchView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

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

    public AppBuilder addFilterSearchView() {
        filterSearchViewModel = new FilterSearchViewModel();
        filterSearchView = new FilterSearchView(filterSearchViewModel);
        cardPanel.add(filterSearchView, filterSearchView.getViewName());
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

    public AppBuilder addSpendingLimitsView() {
        spendingLimitsViewModel = new SpendingLimitsViewModel();
        spendingLimitsView = new SpendingLimitsView(spendingLimitsViewModel);
        cardPanel.add(spendingLimitsView, spendingLimitsView.getViewName());
        return this;
    }

    public AppBuilder addCategorizerView() {
        categorizerViewModel = new CategorizerViewModel();
        final CategorizerOutputBoundary categorizerOutputBoundary = new CategorizerPresenter(categorizerViewModel,
                viewManagerModel, spendingReportViewModel);
        CategorizerInputBoundary categorizerInputBoundary
                = new CategorizerInteractor(new TransactionCategorizerService(new GeminiClient()), categorizerOutputBoundary);
        CategorizerController categorizerController = new CategorizerController(categorizerInputBoundary);
        categorizerView = new TransactionCategorizerView(categorizerViewModel);
        categorizerView.setCategorizerController(categorizerController);
        cardPanel.add(categorizerView, categorizerView.getViewName());
        return this;
    }

    public AppBuilder addCategorizerUseCase() {
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
                loggedInViewModel, loginViewModel, signupViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor
                = new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);

        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    public AppBuilder addSpendingReportView() {
        spendingReportViewModel = new SpendingReportViewModel();
        spendingReportView = new SpendingReportView(spendingReportViewModel);
        cardPanel.add(spendingReportView, spendingReportView.getViewName());
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     *
     * @return this builder
     */
    public AppBuilder addFilterSearchUseCase() {

        FilterSearchOutputBoundary presenter
                = new FilterSearchPresenter(filterSearchViewModel, viewManagerModel);

        FilterSearchInputBoundary interactor
                = new FilterSearchInteractor(transactionDataAccessObject, presenter);

        FilterSearchController controller
                = new FilterSearchController(interactor);

        filterSearchView.setController(controller);
        uploadStatementView.setFilterSearchController(controller);

        return this;
    }

    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor
                = new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addSpendingLimitsUseCase() {
        final SpendingLimitsOutputBoundary spendingLimitsOutputBoundary = new SpendingLimitsPresenter(viewManagerModel,
                spendingLimitsViewModel, uploadStatementViewModel);
        final SpendingLimitsInputBoundary spendingLimitsInteractor
                = new SpendingLimitsInteractor(spendingLimitsOutputBoundary, new FileSpendingLimitsDAO());
        spendingLimitsView.setController(new SpendingLimitsController(spendingLimitsInteractor));
        return this;
    }

    public AppBuilder addUploadStatementUseCase() {
        final UploadStatementPresenter uploadStatementOutputBoundary
                = new UploadStatementPresenter(viewManagerModel, uploadStatementViewModel, spendingLimitsViewModel,
                        spendingReportViewModel, categorizerViewModel);
        final UploadStatementInputBoundary uploadStatementInteractor = new UploadStatementInteractor(transactionDataAccessObject,
                uploadStatementOutputBoundary);
        UploadStatementController uploadStatementController = new UploadStatementController(uploadStatementInteractor);
        uploadStatementView.setUploadStatementController(uploadStatementController);
        loggedInView.setUploadStatementController(uploadStatementController);
        return this;
    }

    public AppBuilder addSpendingReportUseCase() {
        final GenerateReportPresenter presenter = new GenerateReportPresenter(spendingReportView, viewManagerModel);
        final GenerateReportInteractor interactor = new GenerateReportInteractor(
                transactionDataAccessObject, presenter);
        final GenerateReportController controller = new GenerateReportController(interactor);
        spendingReportView.setGenerateReportController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User App Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }

    // getter to test categorizer outside GUI
    public TransactionCategorizerView getCategorizerView() {
        return categorizerView;
    }
}
