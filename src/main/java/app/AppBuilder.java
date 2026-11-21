package app;

import data_access.FileUserDataAccessObject;
import data_access.FrankfurterExchangeRateGateway;
import data_access.JsonFileTripDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.daily_budget.DailyBudgetController;
import interface_adapter.daily_budget.DailyBudgetPresenter;
import interface_adapter.daily_budget.DailyBudgetViewModel;
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
import java.awt.*;
import javax.swing.*;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.daily_budget.DailyBudgetInputBoundary;
import use_case.daily_budget.DailyBudgetInteractor;
import use_case.daily_budget.DailyBudgetOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.DailyBudgetView;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

public class AppBuilder {
  private final JPanel cardPanel = new JPanel();
  private final CardLayout cardLayout = new CardLayout();
  final UserFactory userFactory = new UserFactory();
  final ViewManagerModel viewManagerModel = new ViewManagerModel();
  ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

  // ----- Shared data access objects -----
  // Users are saved to users.csv (course starter code).
  final FileUserDataAccessObject userDataAccessObject =
      new FileUserDataAccessObject("users.csv", userFactory);
  // Trips, expenses, contributions and activity ideas are saved to trips.json.
  // Add "implements <YourUseCase>DataAccessInterface" to InMemoryTripDataAccessObject
  // and pass this object to your interactor.
  final JsonFileTripDataAccessObject tripDataAccessObject =
      new JsonFileTripDataAccessObject("trips.json");
  // Live exchange rates from the Frankfurter API.
  final FrankfurterExchangeRateGateway exchangeRateGateway = new FrankfurterExchangeRateGateway();

  private SignupView signupView;
  private SignupViewModel signupViewModel;
  private LoginViewModel loginViewModel;
  private LoggedInViewModel loggedInViewModel;
  private LoggedInView loggedInView;
  private LoginView loginView;
  private DailyBudgetViewModel dailyBudgetViewModel;
  private DailyBudgetView dailyBudgetView;

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

  /** Adds the screen for viewing and updating a trip's budget. */
  public AppBuilder addDailyBudgetView() {
    dailyBudgetViewModel = new DailyBudgetViewModel();
    dailyBudgetView = new DailyBudgetView(dailyBudgetViewModel);
    cardPanel.add(dailyBudgetView, DailyBudgetViewModel.VIEW_NAME);
    return this;
  }

  public AppBuilder addSignupUseCase() {
    final SignupOutputBoundary signupOutputBoundary =
        new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

    final SignupInputBoundary userSignupInteractor =
        new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);

    SignupController controller = new SignupController(userSignupInteractor);
    signupView.setSignupController(controller);
    return this;
  }

  public AppBuilder addLoginUseCase() {
    final LoginOutputBoundary loginOutputBoundary =
        new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
    final LoginInputBoundary loginInteractor =
        new LoginInteractor(userDataAccessObject, loginOutputBoundary);

    LoginController loginController = new LoginController(loginInteractor);
    loginView.setLoginController(loginController);
    return this;
  }

  public AppBuilder addChangePasswordUseCase() {
    final ChangePasswordOutputBoundary changePasswordOutputBoundary =
        new ChangePasswordPresenter(loggedInViewModel);

    final ChangePasswordInputBoundary changePasswordInteractor =
        new ChangePasswordInteractor(
            userDataAccessObject, changePasswordOutputBoundary, userFactory);

    ChangePasswordController changePasswordController =
        new ChangePasswordController(changePasswordInteractor);
    loggedInView.setChangePasswordController(changePasswordController);
    return this;
  }

  /**
   * Adds the Logout Use Case to the application.
   *
   * @return this builder
   */
  public AppBuilder addLogoutUseCase() {
    final LogoutOutputBoundary logoutOutputBoundary =
        new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

    final LogoutInputBoundary logoutInteractor =
        new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

    final LogoutController logoutController = new LogoutController(logoutInteractor);
    loggedInView.setLogoutController(logoutController);
    return this;
  }

  public JFrame build() {
    final JFrame application = new JFrame("Trip Planner & Budgeter");
    application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    application.add(cardPanel);

    viewManagerModel.setState(SignupViewModel.VIEW_NAME);
    viewManagerModel.notifyViewChanged();

    return application;
  }

  /** Wires the budget screen and gives signed-in users access to it. */
  public AppBuilder addDailyBudgetUseCase() {
    final DailyBudgetOutputBoundary presenter = new DailyBudgetPresenter(dailyBudgetViewModel);
    final DailyBudgetInputBoundary interactor = new DailyBudgetInteractor(
        tripDataAccessObject, userDataAccessObject::getCurrentUsername, presenter);
    final DailyBudgetController controller = new DailyBudgetController(interactor);
    dailyBudgetView.setDailyBudgetController(controller);
    dailyBudgetView.setBackAction(() -> showView(LoggedInViewModel.VIEW_NAME));
    loggedInView.setOpenDailyBudgetAction(() -> {
      controller.open();
      showView(DailyBudgetViewModel.VIEW_NAME);
    });
    return this;
  }

  private void showView(String viewName) {
    viewManagerModel.setState(viewName);
    viewManagerModel.notifyViewChanged();
  }
}
