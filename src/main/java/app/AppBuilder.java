package app;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;


import data_access.InMemoryTripDataAcessObject;
import data_access.SampleData;
import data_access.StorageResolver;
import data_access.image.ImageReader;
import data_access.image.ImageResolver;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardPresenter;
import interface_adapter.dashboard.DashboardViewModel;
import interface_adapter.list_trips.ListTripsController;
import interface_adapter.list_trips.ListTripsPresenter;
import interface_adapter.list_trips.TripListViewModel;
import interface_adapter.load_photos.LoadPhotoController;
import interface_adapter.load_photos.LoadPhotoPresenter;
import interface_adapter.load_photos.PhotoImageViewModel;
import use_case.ImageReaderInterface;
import use_case.dashboard.AnalyticsUseCase;
import use_case.list_trips.ListTripsInputBoundary;
import use_case.list_trips.ListTripsInteractor;
import use_case.list_trips.ListTripsOutputBoundary;
import use_case.load_photos.LoadPhotoInputBoundary;
import use_case.load_photos.LoadPhotoInteractor;
import use_case.load_photos.LoadPhotoOutputBoundary;
import view.NavigationBar;
import view.PlaceholderView;
import view.TripListView;
import view.ViewManager;
import view.dashboard.DashboardView;
import view.components.ThumbnailCache;

/**
 * Composition Root. Only place that creates concrete classes and connects them.
 * Use: new AppBuilder().addTripListView().addMapView()...build()
 */
public class AppBuilder {
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(viewManagerModel);
    private final NavigationBar navigationBar = new NavigationBar(viewManagerModel);


    // Storage. Replace with SQLite implementation later.
    private final InMemoryTripDataAcessObject dataAccess = new InMemoryTripDataAcessObject();
    private final ImageResolver imageResolver = new ImageResolver(new StorageResolver());
    private final ImageReaderInterface imageReader = new ImageReader(imageResolver);

    // Photo loading, shared by every view that shows photos.
    private final PhotoImageViewModel photoImageViewModel = new PhotoImageViewModel();
    private final ThumbnailCache thumbnailCache = buildThumbnailCache();

    private TripListViewModel tripListViewModel;
    private TripListView tripListView;


    public AppBuilder() {
        SampleData.populate(dataAccess, dataAccess, imageResolver);
    }

    private ThumbnailCache buildThumbnailCache() {
        LoadPhotoOutputBoundary presenter = new LoadPhotoPresenter(photoImageViewModel);
        LoadPhotoInputBoundary interactor = new LoadPhotoInteractor(dataAccess, imageReader, presenter);
        return new ThumbnailCache(96, 300, photoImageViewModel, new LoadPhotoController(interactor));
    }

    public AppBuilder addTripListView() {
        tripListViewModel = new TripListViewModel();
        tripListView = new TripListView(tripListViewModel, thumbnailCache);
        return addScreen("Trips", ViewNames.TRIPS, tripListView);
    }

    public AppBuilder addListTripsUseCase() {
        ListTripsOutputBoundary presenter = new ListTripsPresenter(tripListViewModel);
        ListTripsInputBoundary interactor = new ListTripsInteractor(dataAccess, dataAccess, presenter);
        tripListView.setListTripsController(new ListTripsController(interactor));
        return this;
    }

    public AppBuilder addMapView() {
        return addScreen("Map", ViewNames.MAP, new PlaceholderView("Map"));
    }

    public AppBuilder addDashBoardView() {
        DashboardViewModel dashboardViewModel = new DashboardViewModel();
        DashboardPresenter dashboardPresenter = new DashboardPresenter(dashboardViewModel);
        AnalyticsUseCase analyticsUseCase = new AnalyticsUseCase(dataAccess, dataAccess, dashboardPresenter);
        DashboardController dashboardController = new DashboardController(analyticsUseCase);
        return addScreen("Dashboard", ViewNames.DASHBOARD, new DashboardView(dashboardViewModel, dashboardController));
    }

    private AppBuilder addScreen(String label, String viewName, JComponent view) {
        viewManager.addView(viewName, view);
        navigationBar.addNavItem(label, viewName);
        return this;
    }

    public JFrame build() {
        JFrame frame = new JFrame("Journeye");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(navigationBar, BorderLayout.WEST);
        frame.add(viewManager.getCardPanel(), BorderLayout.CENTER);
        frame.setSize(1100, 720);
        frame.setLocationRelativeTo(null);

        viewManagerModel.setActiveView(ViewNames.TRIPS);
        return frame;

    }
}