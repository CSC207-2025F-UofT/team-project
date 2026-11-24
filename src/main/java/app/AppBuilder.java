package app;

import entity.Student;
import interface_adapter.job_filter.JobFilterController;
import interface_adapter.job_filter.JobFilterPresenter;
import interface_adapter.job_filter.JobFilterViewModel;
import use_case.job_filter.JobFilterInteractor;
import view.JobFilterView;

import data_access.FileStudentDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.JobDataAccessObject;
import entity.StudentFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_course.AddCourseController;
import interface_adapter.add_course.AddCoursePresenter;
import interface_adapter.add_course.AddCourseViewModel;
import interface_adapter.job_grouping.CreateJobGroupController;
import interface_adapter.job_grouping.CreateJobGroupPresenter;
import interface_adapter.job_grouping.CreateJobGroupViewModel;
import interface_adapter.job_list.JobListController;
import interface_adapter.job_list.JobListPresenter;
import interface_adapter.job_list.JobListViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.skills_list.SkillListController;
import interface_adapter.skills_list.SkillListPresenter;
import interface_adapter.skills_list.SkillListViewModel;
import use_case.add_course.AddCourseInputBoundary;
import use_case.add_course.AddCourseInteractor;
import use_case.add_course.AddCourseOutputBoundary;
import use_case.course_analysis.AnalysisInputBoundary;
import use_case.course_analysis.AnalysisInteractor;
import use_case.course_analysis.AnalysisOutputBoundary;
import use_case.job_grouping.CreateJobGroupInputBoundary;
import use_case.job_grouping.CreateJobGroupInteractor;
import use_case.job_grouping.CreateJobGroupOutputBoundary;
import use_case.job_match.JobMatchInputBoundary;
import use_case.job_match.JobMatchInteractor;
import use_case.job_match.JobMatchOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.AddCourseView;
import view.CreateJobGroupView;
import view.JobListView;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.SkillsListView;
import view.ViewManager;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final StudentFactory studentFactory = new StudentFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileStudentDataAccessObject userDataAccessObject = new FileStudentDataAccessObject("users.csv", studentFactory);

    // DAO that calls the UofT course API and Gemini for course analysis
    final InMemoryUserDataAccessObject courseApiDataAccessObject = new InMemoryUserDataAccessObject();

    // DAO that fetches jobs from the Careerjet API and caches them in memory
    final JobDataAccessObject jobDataAccessObject = new JobDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private CreateJobGroupViewModel createJobGroupViewModel;
    private CreateJobGroupView createJobGroupView;
    private AddCourseViewModel addCourseViewModel;
    private AddCourseView addCourseView;
    private SkillListViewModel skillListViewModel;
    private SkillsListView skillsListView;
    private JobListViewModel jobListViewModel;
    private JobListView jobListView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        new ViewManager(cardPanel, cardLayout, viewManagerModel);
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
        loggedInView.addFilterJobsListener(event -> {
            Student student = userDataAccessObject.get(loggedInViewModel.getState().getUsername());
            JobFilterViewModel model = new JobFilterViewModel();
            JobFilterController controller = new JobFilterController(
                    new JobFilterInteractor(new JobFilterPresenter(model)));
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(loggedInView),
                    "Filter Jobs", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setContentPane(new JobFilterView(controller, model, student));
            dialog.pack();
            dialog.setLocationRelativeTo(loggedInView);
            dialog.setVisible(true);
        });
        cardPanel.add(loggedInView, LoggedInViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Adds the Create Job Group View to the application.
     * @return this builder
     */
    public AppBuilder addCreateJobGroupView() {
        createJobGroupViewModel = new CreateJobGroupViewModel();

        createJobGroupView = new CreateJobGroupView(
                createJobGroupViewModel,
                new ArrayList<>()
        );

        cardPanel.add(createJobGroupView, CreateJobGroupViewModel.VIEW_NAME);

        return this;
    }

    /**
     * Adds the Add Course View to the application.
     * @return this builder
     */
    public AppBuilder addAddCourseView() {
        addCourseViewModel = new AddCourseViewModel();
        addCourseView = new AddCourseView(addCourseViewModel);
        cardPanel.add(addCourseView, AddCourseViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Adds the Skill List View to the application.
     * @return this builder
     */
    public AppBuilder addSkillListView() {
        skillListViewModel = new SkillListViewModel();
        skillsListView = new SkillsListView(skillListViewModel);
        cardPanel.add(skillsListView, SkillListViewModel.VIEW_NAME);
        return this;
    }

    /**
     * Adds the Job List View to the application.
     * @return this builder
     */
    public AppBuilder addJobListView() {
        jobListViewModel = new JobListViewModel();
        jobListView = new JobListView(jobListViewModel);
        cardPanel.add(jobListView, JobListViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, studentFactory);

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

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    /**
     * Adds the Create Job Group Use Case to the application.
     * @return this builder
     */
    public AppBuilder addCreateJobGroupUseCase() {
        final CreateJobGroupOutputBoundary outputBoundary =
                new CreateJobGroupPresenter(createJobGroupViewModel);

        final CreateJobGroupInputBoundary interactor =
                new CreateJobGroupInteractor(userDataAccessObject, outputBoundary);

        final CreateJobGroupController controller =
                new CreateJobGroupController(interactor);

        createJobGroupView.setController(controller);

        return this;
    }

    /**
     * Adds the Add Course Use Case to the application.
     * @return this builder
     */
    public AppBuilder addAddCourseUseCase() {
        final AddCourseOutputBoundary addCourseOutputBoundary = new AddCoursePresenter(viewManagerModel, addCourseViewModel);

        final AddCourseInputBoundary addCourseInteractor = new AddCourseInteractor(
                userDataAccessObject, userDataAccessObject, addCourseOutputBoundary, studentFactory);

        final AddCourseController addCourseController = new AddCourseController(addCourseInteractor);
        addCourseView.setAddCourseController(addCourseController);
        loggedInView.setAddCourseController(addCourseController);
        return this;
    }

    /**
     * Adds the Course Analysis Use Case to the application.
     * @return this builder
     */
    public AppBuilder addCourseAnalysisUseCase() {
        final AnalysisOutputBoundary analysisOutputBoundary =
                new SkillListPresenter(viewManagerModel, skillListViewModel);

        final AnalysisInputBoundary analysisInteractor = new AnalysisInteractor(
                courseApiDataAccessObject, userDataAccessObject, analysisOutputBoundary, studentFactory);

        final SkillListController skillListController = new SkillListController(analysisInteractor);
        skillsListView.setSkillListController(skillListController);
        loggedInView.setSkillListController(skillListController);
        return this;
    }

    /**
     * Adds the Job Match Use Case to the application.
     * @return this builder
     */
    public AppBuilder addJobMatchUseCase() {
        final JobMatchOutputBoundary jobMatchOutputBoundary =
                new JobListPresenter(viewManagerModel, jobListViewModel);

        final JobMatchInputBoundary jobMatchInteractor =
                new JobMatchInteractor(jobDataAccessObject, userDataAccessObject, jobMatchOutputBoundary);

        final JobListController jobListController = new JobListController(jobMatchInteractor);
        jobListView.setJobListController(jobListController);
        loggedInView.setJobListController(jobListController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(SignupViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
