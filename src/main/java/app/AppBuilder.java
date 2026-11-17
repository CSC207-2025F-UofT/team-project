package app;

import data_access.FileUserDataAccessObject;
import data_access.InMemoryCourseDataAccessObject;
import entity.CourseFactory;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.calculate_grade.CalculateGradeController;
import interface_adapter.calculate_grade.CalculateGradePresenter;
import interface_adapter.calculate_grade.GradeCalculatorViewModel;
import interface_adapter.course_progress.CourseProgressViewModel;
import interface_adapter.enrol_course.EnrolCourseController;
import interface_adapter.enrol_course.EnrolCoursePresenter;
import interface_adapter.enrol_course.EnrolViewModel;
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
import use_case.calculate_grade.CalculateGradeInputBoundary;
import use_case.calculate_grade.CalculateGradeInteractor;
import use_case.calculate_grade.CalculateGradeOutputBoundary;
import use_case.enrol_course.EnrolCourseInputBoundary;
import use_case.enrol_course.EnrolCourseInteractor;
import use_case.enrol_course.EnrolCourseOutputBoundary;
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
import view.EnrolView;
import view.GradeCalculatorDialog;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

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

    final CourseFactory courseFactory = new CourseFactory();
    // TODO: swap for the persistent course DAO once it exists; courses are lost on restart for now.
    final InMemoryCourseDataAccessObject courseDataAccessObject = new InMemoryCourseDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private EnrolViewModel enrolViewModel;
    private EnrolView enrolView;

    // The main window. It is created early so that pop-ups can use it as their owner.
    private final JFrame application = new JFrame("University Learning Progress Tracker");
    private final CourseProgressViewModel courseProgressViewModel = new CourseProgressViewModel();
    private GradeCalculatorDialog gradeCalculatorDialog;

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

    public AppBuilder addEnrolView() {
        enrolViewModel = new EnrolViewModel();
        enrolView = new EnrolView(enrolViewModel);
        cardPanel.add(enrolView, EnrolViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Adds the Enrol Course Use Case to the application.
     * Call after addLoggedInView and addEnrolView.
     * @return this builder
     */
    public AppBuilder addEnrolCourseUseCase() {
        final EnrolCourseOutputBoundary enrolCourseOutputBoundary =
                new EnrolCoursePresenter(viewManagerModel, enrolViewModel);
        final EnrolCourseInputBoundary enrolCourseInteractor = new EnrolCourseInteractor(
                courseDataAccessObject, enrolCourseOutputBoundary, courseFactory);

        final EnrolCourseController enrolCourseController =
                new EnrolCourseController(enrolCourseInteractor, loggedInViewModel);
        enrolView.setEnrolCourseController(enrolCourseController);
        loggedInView.setEnrolCourseController(enrolCourseController);
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

    /**
     * Adds the Calculate Grade Use Case (use case 5) to the application.
     * The course progress page opens the pop-up with getGradeCalculatorDialog().open().
     * @return this builder
     */
    public AppBuilder addCalculateGradeUseCase() {
        final GradeCalculatorViewModel gradeCalculatorViewModel = new GradeCalculatorViewModel();
        gradeCalculatorDialog = new GradeCalculatorDialog(application, gradeCalculatorViewModel,
                courseProgressViewModel);

        final CalculateGradeOutputBoundary calculateGradeOutputBoundary =
                new CalculateGradePresenter(gradeCalculatorViewModel, courseProgressViewModel);
        final CalculateGradeInputBoundary calculateGradeInteractor =
                new CalculateGradeInteractor(courseDataAccessObject, calculateGradeOutputBoundary);

        final CalculateGradeController calculateGradeController =
                new CalculateGradeController(calculateGradeInteractor);
        gradeCalculatorDialog.setCalculateGradeController(calculateGradeController);
        return this;
    }

    public CourseProgressViewModel getCourseProgressViewModel() {
        return courseProgressViewModel;
    }

    public GradeCalculatorDialog getGradeCalculatorDialog() {
        return gradeCalculatorDialog;
    }

    public JFrame build() {
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(SignupViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }


}
