package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.InMemoryPetDataAccess;
import interface_adapter.SwitchViewController;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.BrowsePetsController;
import interface_adapter.browse_pets.BrowsePetsPresenter;
import interface_adapter.browse_pets.GalleryViewModel;
import interface_adapter.manage_pet.ManagePetController;
import interface_adapter.manage_pet.ManagePetPresenter;
import interface_adapter.manage_pet.ManagePetViewModel;
import use_case.manage_pet.ManagePetDataAccessInterface;
import use_case.browse_pets.BrowsePetsInteractor;
import use_case.manage_pet.ManagePetInteractor;
import interface_adapter.view_pet_details.PetDetailViewModel;
import interface_adapter.view_pet_details.ViewPetDetailsController;
import interface_adapter.view_pet_details.ViewPetDetailsPresenter;
import use_case.view_pet_details.ViewPetDetailsInteractor;
import view.GalleryView;
import view.PetDetailView;
import view.ManagePetView;
import view.ViewManager;

/**
 * Builds the window and wires every screen. This is the only place concrete classes are put together.
 */
public final class AppBuilder {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel views = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(views, cardLayout, viewManagerModel);

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

    /**
     * Creates the window showing the first screen.
     *
     * @return the main window, not yet visible
     */
    public JFrame build() {
        final JFrame frame = new JFrame("PawPortal");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(views);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        viewManagerModel.setActiveView(GalleryView.VIEW_NAME);
        return frame;
    }
}
