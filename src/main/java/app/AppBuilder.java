package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.FileApplicationDataAccessObject;
import data_access.FilePetDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.InMemoryPetDataAccess;
import entity.Pet;
import entity.UserFactory;
import interface_adapter.ApplicationNavigator;
import interface_adapter.ViewManagerModel;
import interface_adapter.admin_signup.AdminSignupController;
import interface_adapter.admin_signup.AdminSignupPresenter;
import interface_adapter.admin_signup.AdminSignupViewModel;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.BrowsePetsPresenter;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.manage_pet.ManagePetController;
import interface_adapter.manage_pet.ManagePetPresenter;
import interface_adapter.manage_pet.ManagePetViewModel;
import interface_adapter.submit_application.SubmitApplicationController;
import interface_adapter.submit_application.SubmitApplicationPresenter;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsController;
import interface_adapter.track_applications.TrackApplicationsPresenter;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import use_case.admin_signup.AdminSignupInteractor;
import use_case.browse_pets.BrowsePetsDataAccessInterface;
import use_case.browse_pets.BrowsePetsInteractor;
import use_case.login.LoginInteractor;
import use_case.manage_pet.ManagePetDataAccessInterface;
import use_case.manage_pet.ManagePetInteractor;
import use_case.submit_application.SubmitApplicationInteractor;
import use_case.track_applications.TrackApplicationsInteractor;
import view.AdminSignupView;
import view.GalleryView;
import view.LoginView;
import view.ManagePetView;
import view.MyApplicationsView;
import view.SubmitApplicationView;
import view.ViewManager;

/**
 * Builds the window and wires every screen. This is the only place concrete classes are put together.
 */
public final class AppBuilder {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel views = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(views, cardLayout, viewManagerModel);
    private ApplicationNavigator applicationNavigator;

    static AppBuilder forDataDirectory(Path dataDirectory, Path applicationDirectory, String inviteCode)
            throws IOException {
        Files.createDirectories(dataDirectory);
        final Path petFile = dataDirectory.resolve("pets.json");
        final Path previousPetFile = applicationDirectory.resolve("pets.json");
        final List<Pet> initialPets;
        if (Files.exists(petFile)) {
            initialPets = List.of();
        } else if (Files.exists(previousPetFile)) {
            initialPets = new FilePetDataAccessObject(previousPetFile).getPets();
        } else {
            initialPets = InMemoryPetDataAccess.withSampleData().getAvailablePets();
        }
        final FilePetDataAccessObject pets = new FilePetDataAccessObject(petFile, initialPets);
        final UserFactory userFactory = new UserFactory();
        final FileUserDataAccessObject users = new FileUserDataAccessObject(dataDirectory.resolve("users.json"),
                userFactory);
        final AppBuilder builder = new AppBuilder();
        builder.addShelterLogin(users, userFactory, inviteCode);
        builder.addPetManagementView(pets, users);
        builder.addGalleryView(pets, builder.navigationButton("Shelter login", LoginViewModel.VIEW_NAME));
        return builder.addApplicationViews(pets, applicationDirectory.resolve("applications.json"));
    }

    /**
     * Adds the pet gallery screen.
     *
     * @return this builder
     */
    public AppBuilder addGalleryView() {
        addGalleryView(InMemoryPetDataAccess.withSampleData(), null);
        return this;
    }

    private void addGalleryView(BrowsePetsDataAccessInterface petData, JButton shelterLogin) {
        final GalleryViewModel galleryViewModel = new GalleryViewModel();
        final BrowsePetsPresenter presenter = new BrowsePetsPresenter(galleryViewModel);
        final BrowsePetsInteractor interactor = new BrowsePetsInteractor(petData, presenter);
        final BrowsePetsController controller = new BrowsePetsController(interactor);
        final GalleryView galleryView = new GalleryView(controller, galleryViewModel);
        final JPanel gallery = shelterLogin == null ? galleryView : withNavigation(galleryView, shelterLogin);
        views.add(gallery, GalleryView.VIEW_NAME);
        viewManagerModel.addPropertyChangeListener(event -> {
            if (ViewManagerModel.ACTIVE_VIEW.equals(event.getPropertyName())
                    && GalleryView.VIEW_NAME.equals(event.getNewValue())) {
                galleryView.loadAll();
            }
        });
    }

    private void addShelterLogin(FileUserDataAccessObject users, UserFactory userFactory, String inviteCode) {
        final LoginViewModel loginViewModel = new LoginViewModel();
        final LoginPresenter loginPresenter = new LoginPresenter(viewManagerModel, loginViewModel);
        final LoginView loginView = new LoginView(loginViewModel, () -> {
            loginViewModel.setState(new LoginState());
            loginViewModel.notifyStateChanged();
            viewManagerModel.setActiveView(GalleryView.VIEW_NAME);
        });
        loginView.setLoginController(new LoginController(new LoginInteractor(users, loginPresenter)));
        if (inviteCode == null || inviteCode.isBlank()) {
            views.add(loginView, LoginViewModel.VIEW_NAME);
        } else {
            final AdminSignupViewModel signupViewModel = new AdminSignupViewModel();
            final AdminSignupPresenter signupPresenter =
                    new AdminSignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
            final AdminSignupView signupView = new AdminSignupView(signupViewModel);
            final AdminSignupInteractor signupInteractor =
                    new AdminSignupInteractor(users, signupPresenter, userFactory, inviteCode);
            signupView.setSignupController(new AdminSignupController(signupInteractor));
            views.add(signupView, AdminSignupViewModel.VIEW_NAME);
            final JButton signup = navigationButton("Create shelter account", AdminSignupViewModel.VIEW_NAME);
            views.add(withNavigation(loginView, signup), LoginViewModel.VIEW_NAME);
        }
    }

    private void addPetManagementView(ManagePetDataAccessInterface pets, FileUserDataAccessObject users) {
        final JButton signOut = new JButton("Sign out");
        signOut.addActionListener(event -> {
            users.setCurrentUserId(null);
            viewManagerModel.setActiveView(GalleryView.VIEW_NAME);
        });
        views.add(withNavigation(buildPetManagement(pets), signOut), ManagePetViewModel.VIEW_NAME);
    }

    private JButton navigationButton(String label, String destination) {
        final JButton button = new JButton(label);
        button.addActionListener(event -> viewManagerModel.setActiveView(destination));
        return button;
    }

    private static JPanel withNavigation(JPanel screen, JButton button) {
        final JPanel panel = new JPanel(new BorderLayout());
        final JPanel navigation = new JPanel();
        navigation.add(button);
        panel.add(navigation, BorderLayout.NORTH);
        panel.add(screen, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Adds the application form (Use Case 5) and My Applications (Use Case 6).
     * Both screens share the same application and pet files, so a submitted
     * application shows up in My Applications and is kept after a restart.
     *
     * @return this builder
     */
    public AppBuilder addApplicationViews() {
        return addApplicationViews(new FilePetDataAccessObject(Path.of("pets.json")), Path.of("applications.json"));
    }

    private AppBuilder addApplicationViews(FilePetDataAccessObject petData, Path applicationFile) {
        final FileApplicationDataAccessObject applicationData =
                new FileApplicationDataAccessObject(applicationFile.toString());

        final SubmitApplicationViewModel submitViewModel = new SubmitApplicationViewModel();
        final SubmitApplicationView submitView = new SubmitApplicationView(submitViewModel);
        submitView.setSubmitApplicationController(new SubmitApplicationController(
                new SubmitApplicationInteractor(applicationData, petData,
                        new SubmitApplicationPresenter(submitViewModel))));
        views.add(submitView, SubmitApplicationViewModel.VIEW_NAME);

        final TrackApplicationsViewModel trackViewModel = new TrackApplicationsViewModel();
        final MyApplicationsView myApplicationsView = new MyApplicationsView(trackViewModel);
        myApplicationsView.setTrackApplicationsController(new TrackApplicationsController(
                new TrackApplicationsInteractor(applicationData, petData,
                        new TrackApplicationsPresenter(trackViewModel))));
        views.add(myApplicationsView, TrackApplicationsViewModel.VIEW_NAME);

        applicationNavigator = new ApplicationNavigator(viewManagerModel, submitViewModel, trackViewModel);
        return this;
    }

    /**
     * Returns the navigator other screens use to open the application screens,
     * for example login (to set the adopter) and the gallery (to apply for a pet).
     *
     * @return the navigator, or null if addApplicationViews was not called
     */
    public ApplicationNavigator getApplicationNavigator() {
        return applicationNavigator;
    }

    /**
     * Builds the pet management screen for the admin flow.
     * @param petDataAccess storage shared with the other pet features
     * @return the wired management view
     */
    public ManagePetView buildPetManagement(ManagePetDataAccessInterface petDataAccess) {
        final ManagePetViewModel viewModel = new ManagePetViewModel();
        final ManagePetPresenter presenter = new ManagePetPresenter(viewModel);
        final ManagePetInteractor interactor = new ManagePetInteractor(petDataAccess, presenter);
        final ManagePetController controller = new ManagePetController(interactor);
        final ManagePetView view = new ManagePetView(controller, viewModel);
        controller.refresh();
        return view;
    }

    private JMenuBar createMenuBar() {
        final JMenuItem gallery = new JMenuItem("Pet Gallery");
        gallery.addActionListener(event -> viewManagerModel.setActiveView(GalleryView.VIEW_NAME));
        final JMenuItem myApplications = new JMenuItem("My Applications");
        myApplications.addActionListener(event -> applicationNavigator.showMyApplications());

        final JMenu menu = new JMenu("Go to");
        menu.add(gallery);
        menu.add(myApplications);
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        viewManagerModel.addPropertyChangeListener(event -> {
            final Object activeView = event.getNewValue();
            menu.setEnabled(!ManagePetViewModel.VIEW_NAME.equals(activeView)
                    && !AdminSignupViewModel.VIEW_NAME.equals(activeView)
                    && !LoginViewModel.VIEW_NAME.equals(activeView));
        });
        return menuBar;
    }

    /**
     * Creates the window showing the first screen.
     *
     * @return the main window, not yet visible
     */
    public JFrame build() {
        final JFrame frame = new JFrame("PawPortal");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(buildViews());
        if (applicationNavigator != null) {
            frame.setJMenuBar(createMenuBar());
        }
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        return frame;
    }

    JPanel buildViews() {
        viewManagerModel.setActiveView(GalleryView.VIEW_NAME);
        return views;
    }
}
