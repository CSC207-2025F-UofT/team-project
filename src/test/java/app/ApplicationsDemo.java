package app;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import data_access.FileApplicationDataAccessObject;
import interface_adapter.submit_application.SubmitApplicationController;
import interface_adapter.submit_application.SubmitApplicationPresenter;
import interface_adapter.submit_application.SubmitApplicationViewModel;
import use_case.submit_application.SubmitApplicationInteractor;
import use_case.submit_application.SubmitApplicationPetDataAccessInterface;
import view.SubmitApplicationView;

/**
 * Runs the Submit Application use case on its own, before the login and pet screens exist. A fixed adopter applies for a sample cat; applications are saved in
 * applications-demo.json. Development tool only.
 */
public class ApplicationsDemo {

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

        final JFrame frame = new JFrame("PawPortal: applications demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(submitView);
        frame.setSize(560, 420);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Stands in for the real pet data access until the pet use cases are built.
     */
    private static class DemoPetDataAccess implements SubmitApplicationPetDataAccessInterface {

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
