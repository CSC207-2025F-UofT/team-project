package app;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import data_access.FileApplicationDataAccessObject;
import interface_adapter.submit_application.SubmitApplicationController;
import interface_adapter.submit_application.SubmitApplicationPresenter;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import interface_adapter.track_applications.TrackApplicationsController;
import interface_adapter.track_applications.TrackApplicationsPresenter;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import use_case.submit_application.SubmitApplicationInteractor;
import use_case.submit_application.SubmitApplicationPetDataAccessInterface;
import use_case.track_applications.TrackApplicationsInteractor;
import use_case.track_applications.TrackApplicationsPetDataAccessInterface;
import view.MyApplicationsView;
import view.SubmitApplicationView;

/**
 * Runs the Submit Application and Track My Applications use cases on their own, before the login
 * and pet screens exist. A fixed adopter applies for a sample cat and can then see the application
 * in My Applications; applications are saved in applications-demo.json. Development tool only.
 */
public class ApplicationsDemo {

    /**
     * Opens the demo window.
     * @param args the command line arguments, not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApplicationsDemo::start);
    }

    private static void start() {
        final String adopterId = "demo-adopter";
        final FileApplicationDataAccessObject applicationDataAccess =
                new FileApplicationDataAccessObject("applications-demo.json");
        final DemoPetDataAccess petDataAccess = new DemoPetDataAccess();

        final SubmitApplicationViewModel submitViewModel = new SubmitApplicationViewModel();
        submitViewModel.getState().setAdopterId(adopterId);
        submitViewModel.getState().setPetId("mochi");
        submitViewModel.getState().setPetName("Mochi");
        final SubmitApplicationView submitView = new SubmitApplicationView(submitViewModel);
        submitView.setSubmitApplicationController(new SubmitApplicationController(
                new SubmitApplicationInteractor(applicationDataAccess, petDataAccess,
                        new SubmitApplicationPresenter(submitViewModel))));

        final TrackApplicationsViewModel trackViewModel = new TrackApplicationsViewModel();
        trackViewModel.getState().setAdopterId(adopterId);
        final MyApplicationsView myApplicationsView = new MyApplicationsView(trackViewModel);
        myApplicationsView.setTrackApplicationsController(new TrackApplicationsController(
                new TrackApplicationsInteractor(applicationDataAccess, petDataAccess,
                        new TrackApplicationsPresenter(trackViewModel))));

        final JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Apply", submitView);
        tabs.addTab("My Applications", myApplicationsView);
        tabs.addChangeListener(event -> myApplicationsView.refresh());
        myApplicationsView.refresh();

        final JFrame frame = new JFrame("PawPortal: applications demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(tabs);
        frame.setSize(560, 420);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Stands in for the real pet data access until the pet use cases are built.
     */
    private static class DemoPetDataAccess implements SubmitApplicationPetDataAccessInterface,
            TrackApplicationsPetDataAccessInterface {

        private final Map<String, String> petNames = new HashMap<>();

        DemoPetDataAccess() {
            petNames.put("mochi", "Mochi");
        }

        @Override
        public boolean isAvailable(String petId) {
            return petNames.containsKey(petId);
        }

        @Override
        public String getPetName(String petId) {
            return petNames.getOrDefault(petId, "(unknown pet)");
        }
    }
}
