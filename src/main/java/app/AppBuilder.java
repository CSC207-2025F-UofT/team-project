package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import data_access.CatApiBreedDataAccess;
import data_access.FileApplicationDataAccessObject;
import data_access.FilePetDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.InMemoryPetDataAccess;
import data_access.PetCatalog;
import data_access.RescueGroupsPetDataAccess;
import entity.Pet;
import entity.UserFactory;
import interface_adapter.ApplicationNavigator;
import interface_adapter.SwitchViewController;
import interface_adapter.ViewManagerModel;
import interface_adapter.admin_signup.AdminSignupController;
import interface_adapter.admin_signup.AdminSignupPresenter;
import interface_adapter.admin_signup.AdminSignupViewModel;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.BrowsePetsPresenter;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.view_pet_details.PetDetailViewModel;
import interface_adapter.view_pet_details.ViewPetDetailsController;
import interface_adapter.view_pet_details.ViewPetDetailsPresenter;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.manage_pet.ManagePetController;
import interface_adapter.manage_pet.ManagePetPresenter;
import interface_adapter.manage_pet.ManagePetViewModel;
import interface_adapter.review_applications.ReviewApplicationsController;
import interface_adapter.review_applications.ReviewApplicationsPresenter;
import interface_adapter.review_applications.ReviewApplicationsViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.submit_application.SubmitApplicationController;
import interface_adapter.submit_application.SubmitApplicationPresenter;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsController;
import interface_adapter.track_applications.TrackApplicationsPresenter;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import interface_adapter.update_application_status.UpdateApplicationController;
import interface_adapter.update_application_status.UpdateApplicationPresenter;
import interface_adapter.update_application_status.UpdateApplicationViewModel;
import use_case.admin_signup.AdminSignupInteractor;
import use_case.browse_pets.BrowsePetsDataAccessInterface;
import use_case.browse_pets.BrowsePetsInteractor;
import use_case.view_pet_details.ViewPetDetailsDataAccessInterface;
import use_case.view_pet_details.ViewPetDetailsInteractor;
import use_case.login.LoginInteractor;
import use_case.manage_pet.BreedLookupInteractor;
import use_case.manage_pet.ManagePetDataAccessInterface;
import use_case.manage_pet.ManagePetInteractor;
import use_case.review_applications.ReviewApplicationsInteractor;
import use_case.signup.SignupInteractor;
import use_case.submit_application.SubmitApplicationInteractor;
import use_case.track_applications.TrackApplicationsInteractor;
import use_case.update_application_status.UpdateApplicationInteractor;
import view.AdminSignupView;
import view.GalleryView;
import view.LoginView;
import view.ManagePetView;
import view.PetDetailView;
import view.MyApplicationsView;
import view.ReviewApplicationsView;
import view.SignupView;
import view.SubmitApplicationView;
import view.UpdateApplicationView;
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
    private PetDetailView petDetailView;

    static AppBuilder forDataDirectory(Path dataDirectory, Path applicationDirectory, String inviteCode)
            throws IOException {
        return forDataDirectory(dataDirectory, applicationDirectory, inviteCode, null);
    }

    static AppBuilder forDataDirectory(Path dataDirectory, Path applicationDirectory, String inviteCode,
                                       String rescueGroupsKey) throws IOException {
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
        final RescueGroupsPetDataAccess rescueGroups = new RescueGroupsPetDataAccess(rescueGroupsKey);
        final PetCatalog catalog = new PetCatalog(pets, rescueGroups);
        final GalleryView gallery = builder.addGalleryView(catalog, catalog,
                builder.navigationButton("Shelter login", LoginViewModel.VIEW_NAME));
        if (rescueGroups.isConfigured()) {
            builder.petDetailView.setExternalPhotoLoader(rescueGroups::loadPhoto);
            loadRescueGroups(rescueGroups, gallery);
        }
        return builder.addApplicationViews(pets, users, applicationDirectory.resolve("applications.json"));
    }

    private static void loadRescueGroups(RescueGroupsPetDataAccess rescueGroups, GalleryView gallery) {
        gallery.showRescueStatus("Loading RescueGroups listings near Toronto…");
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws IOException {
                rescueGroups.refresh();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    gallery.loadAll();
                    gallery.showRescueStatus(rescueGroups.getAvailablePets().isEmpty()
                            ? "No RescueGroups listings found near Toronto." : "");
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    gallery.showRescueStatus("RescueGroups listings could not be loaded.");
                } catch (ExecutionException exception) {
                    gallery.showRescueStatus("RescueGroups listings could not be loaded. Local listings are shown.");
                }
            }
        }.execute();
    }

    /**
     * Adds the pet gallery screen.
     *
     * @return this builder
     */
    public AppBuilder addGalleryView() {
        final InMemoryPetDataAccess sampleData = InMemoryPetDataAccess.withSampleData();
        addGalleryView(sampleData, sampleData, null);
        return this;
    }

    private GalleryView addGalleryView(BrowsePetsDataAccessInterface petData,
                                       ViewPetDetailsDataAccessInterface detailData, JButton shelterLogin) {
        final PetDetailViewModel detailViewModel = new PetDetailViewModel();
        final ViewPetDetailsPresenter detailsPresenter =
                new ViewPetDetailsPresenter(detailViewModel, viewManagerModel);
        final ViewPetDetailsController detailsController =
                new ViewPetDetailsController(new ViewPetDetailsInteractor(detailData, detailsPresenter));
        petDetailView = new PetDetailView(
                detailViewModel, new SwitchViewController(viewManagerModel), GalleryView.VIEW_NAME);
        views.add(petDetailView, PetDetailViewModel.VIEW_NAME);
        connectApplyButton();

        final GalleryViewModel galleryViewModel = new GalleryViewModel();
        final BrowsePetsPresenter presenter = new BrowsePetsPresenter(galleryViewModel);
        final BrowsePetsInteractor interactor = new BrowsePetsInteractor(petData, presenter);
        final BrowsePetsController controller = new BrowsePetsController(interactor);
        final GalleryView galleryView = new GalleryView(controller, galleryViewModel, detailsController);
        final JPanel gallery = shelterLogin == null ? galleryView : withNavigation(galleryView, shelterLogin);
        views.add(gallery, GalleryView.VIEW_NAME);
        viewManagerModel.addPropertyChangeListener(event -> {
            if (ViewManagerModel.ACTIVE_VIEW.equals(event.getPropertyName())
                    && GalleryView.VIEW_NAME.equals(event.getNewValue())) {
                galleryView.loadAll();
            }
        });
        return galleryView;
    }

    private void connectApplyButton() {
        if (petDetailView != null && applicationNavigator != null) {
            petDetailView.setApplyHandler(applicationNavigator::startApplication);
        }
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
        final JButton signup = addSignupView(users, userFactory, loginViewModel);
        if (inviteCode == null || inviteCode.isBlank()) {
            views.add(withNavigation(loginView, signup), LoginViewModel.VIEW_NAME);
        } else {
            final AdminSignupViewModel adminSignupViewModel = new AdminSignupViewModel();
            final AdminSignupPresenter adminSignupPresenter =
                    new AdminSignupPresenter(viewManagerModel, adminSignupViewModel, loginViewModel);
            final AdminSignupView adminSignupView = new AdminSignupView(adminSignupViewModel);
            final AdminSignupInteractor adminSignupInteractor =
                    new AdminSignupInteractor(users, adminSignupPresenter, userFactory, inviteCode);
            adminSignupView.setSignupController(new AdminSignupController(adminSignupInteractor));
            views.add(adminSignupView, AdminSignupViewModel.VIEW_NAME);
            final JButton adminSignup =
                    navigationButton("Create shelter account", AdminSignupViewModel.VIEW_NAME);
            views.add(withNavigation(loginView, signup, adminSignup), LoginViewModel.VIEW_NAME);
        }
    }

    /**
     * Adds the adopter sign-up screen. A successful sign-up returns to login with the email filled in.
     *
     * @return a button that opens the sign-up screen
     */
    private JButton addSignupView(FileUserDataAccessObject users, UserFactory userFactory,
                                  LoginViewModel loginViewModel) {
        final SignupViewModel signupViewModel = new SignupViewModel();
        final SignupPresenter signupPresenter =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);
        final SignupView signupView = new SignupView(signupViewModel);
        signupView.setSignupController(new SignupController(
                new SignupInteractor(users, signupPresenter, userFactory)));
        views.add(signupView, SignupViewModel.VIEW_NAME);
        return navigationButton("Create account", SignupViewModel.VIEW_NAME);
    }

    private void addPetManagementView(ManagePetDataAccessInterface pets, FileUserDataAccessObject users) {
        final JButton signOut = new JButton("Sign out");
        signOut.addActionListener(event -> {
            users.setCurrentUserId(null);
            viewManagerModel.setActiveView(GalleryView.VIEW_NAME);
        });
        final JButton reviewApplications =
                navigationButton("Review applications", ReviewApplicationsViewModel.VIEW_NAME);
        views.add(withNavigation(buildPetManagement(pets), reviewApplications, signOut),
                ManagePetViewModel.VIEW_NAME);
    }

    private JButton navigationButton(String label, String destination) {
        final JButton button = new JButton(label);
        button.addActionListener(event -> viewManagerModel.setActiveView(destination));
        return button;
    }

    private static JPanel withNavigation(JPanel screen, JButton... buttons) {
        final JPanel panel = new JPanel(new BorderLayout());
        final JPanel navigation = new JPanel();
        for (JButton button : buttons) {
            navigation.add(button);
        }
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
        return addApplicationViews(new FilePetDataAccessObject(Path.of("pets.json")), null,
                Path.of("applications.json"));
    }

    /**
     * Adds the adopter's application screens and, when users are given, the admin's
     * Review Applications and Update Application Status screens. All of them share one
     * application store, so a change made on one screen is seen by the others.
     */
    private AppBuilder addApplicationViews(FilePetDataAccessObject petData, FileUserDataAccessObject users,
                                           Path applicationFile) {
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
        if (users != null) {
            addReviewAndUpdateViews(petData, applicationData, users);
        }
        connectApplyButton();
        return this;
    }

    /**
     * Adds Review Applications (Use Case 7) and Update Application Status (Use Case 8) for the admin.
     */
    private void addReviewAndUpdateViews(FilePetDataAccessObject petData,
                                         FileApplicationDataAccessObject applicationData,
                                         FileUserDataAccessObject users) {
        // Each presenter writes to both view models, so create the view models first.
        final ReviewApplicationsViewModel reviewViewModel = new ReviewApplicationsViewModel();
        final UpdateApplicationViewModel updateViewModel = new UpdateApplicationViewModel();

        final ReviewApplicationsPresenter reviewPresenter =
                new ReviewApplicationsPresenter(reviewViewModel, updateViewModel, viewManagerModel);
        final ReviewApplicationsView reviewView = new ReviewApplicationsView(reviewViewModel);
        reviewView.setReviewApplicationsController(new ReviewApplicationsController(
                new ReviewApplicationsInteractor(applicationData, petData, users, reviewPresenter)));
        views.add(withNavigation(reviewView, navigationButton("Manage pets", ManagePetViewModel.VIEW_NAME)),
                ReviewApplicationsViewModel.VIEW_NAME);

        final UpdateApplicationPresenter updatePresenter =
                new UpdateApplicationPresenter(updateViewModel, viewManagerModel, reviewViewModel);
        final UpdateApplicationView updateView = new UpdateApplicationView(updateViewModel);
        updateView.setUpdateApplicationController(new UpdateApplicationController(
                new UpdateApplicationInteractor(updatePresenter, petData, applicationData)));
        views.add(updateView, UpdateApplicationViewModel.VIEW_NAME);

        // The review view is wrapped in a navigation panel, so it does not get componentShown itself.
        // Reload the list whenever its screen becomes active, for example after a status update.
        viewManagerModel.addPropertyChangeListener(event -> {
            if (ViewManagerModel.ACTIVE_VIEW.equals(event.getPropertyName())
                    && ReviewApplicationsViewModel.VIEW_NAME.equals(event.getNewValue())) {
                reviewView.refresh();
            }
        });
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
        final CatApiBreedDataAccess breedDataAccess = new CatApiBreedDataAccess(System.getenv("THE_CAT_API_KEY"));
        final ManagePetController controller = new ManagePetController(interactor,
                new BreedLookupInteractor(breedDataAccess));
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
                    && !ReviewApplicationsViewModel.VIEW_NAME.equals(activeView)
                    && !UpdateApplicationViewModel.VIEW_NAME.equals(activeView)
                    && !AdminSignupViewModel.VIEW_NAME.equals(activeView)
                    && !SignupViewModel.VIEW_NAME.equals(activeView)
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
