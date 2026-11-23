package app;

import java.awt.CardLayout;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.FileApplicationDataAccessObject;
import data_access.FilePetDataAccessObject;
import data_access.InMemoryPetDataAccess;
import interface_adapter.SwitchViewController;
import interface_adapter.ApplicationNavigator;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.BrowsePetsPresenter;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.manage_pet.ManagePetController;
import interface_adapter.manage_pet.ManagePetPresenter;
import interface_adapter.manage_pet.ManagePetViewModel;
import interface_adapter.submit_application.SubmitApplicationController;
import interface_adapter.submit_application.SubmitApplicationPresenter;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsController;
import interface_adapter.track_applications.TrackApplicationsPresenter;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import use_case.manage_pet.ManagePetDataAccessInterface;
import use_case.browse_pets.BrowsePetsInteractor;
import use_case.manage_pet.ManagePetInteractor;
import interface_adapter.view_pet_details.PetDetailViewModel;
import interface_adapter.view_pet_details.ViewPetDetailsController;
import interface_adapter.view_pet_details.ViewPetDetailsPresenter;
import use_case.view_pet_details.ViewPetDetailsInteractor;
import use_case.submit_application.SubmitApplicationInteractor;
import use_case.track_applications.TrackApplicationsInteractor;
import view.GalleryView;
import view.PetDetailView;
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

    /**
     * Adds the pet gallery screen.
     *
     * @return this builder
     */
    public AppBuilder addGalleryView() {
        final InMemoryPetDataAccess petData = InMemoryPetDataAccess.withSampleData();

        final PetDetailViewModel detailViewModel = new PetDetailViewModel();
        final ViewPetDetailsPresenter detailsPresenter =
                new ViewPetDetailsPresenter(detailViewModel, viewManagerModel);
        final ViewPetDetailsController detailsController =
                new ViewPetDetailsController(new ViewPetDetailsInteractor(petData, detailsPresenter));
        final PetDetailView detailView = new PetDetailView(
                detailViewModel, new SwitchViewController(viewManagerModel), GalleryView.VIEW_NAME);
        views.add(detailView, PetDetailViewModel.VIEW_NAME);

        final GalleryViewModel galleryViewModel = new GalleryViewModel();
        final BrowsePetsPresenter presenter = new BrowsePetsPresenter(galleryViewModel);
        final BrowsePetsInteractor interactor = new BrowsePetsInteractor(petData, presenter);
        final BrowsePetsController controller = new BrowsePetsController(interactor);
        final GalleryView galleryView = new GalleryView(controller, galleryViewModel, detailsController);
        galleryView.loadAll();
        views.add(galleryView, GalleryView.VIEW_NAME);
        return this;
    }

    /**
     * Adds the application form (Use Case 5) and My Applications (Use Case 6).
     * Both screens share the same application and pet files, so a submitted
     * application shows up in My Applications and is kept after a restart.
     *
     * @return this builder
     */
    public AppBuilder addApplicationViews() {
        final FileApplicationDataAccessObject applicationData =
                new FileApplicationDataAccessObject("applications.json");
        final FilePetDataAccessObject petData = new FilePetDataAccessObject(Path.of("pets.json"));

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
        frame.add(views);
        if (applicationNavigator != null) {
            frame.setJMenuBar(createMenuBar());
        }
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        viewManagerModel.setActiveView(GalleryView.VIEW_NAME);
        return frame;
    }
}
