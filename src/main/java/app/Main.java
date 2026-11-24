package app;

import data_access.InMemoryProfileDataAccessObject;
import entity.ChildProfile;
import entity.CommunicationMessage;
import entity.Pictogram;
import interface_adapter.view_history.ViewHistoryController;
import interface_adapter.view_history.ViewHistoryPresenter;
import interface_adapter.view_history.ViewHistoryViewModel;
import use_case.view_history.ViewHistoryInteractor;
import view.AppFrame;
import view.HistoryPanel;
import view.ScreenNames;

import javax.swing.SwingUtilities;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Builds the app: creates the data access, use cases and screens,
 * and connects them together.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InMemoryProfileDataAccessObject dataAccess =
                    new InMemoryProfileDataAccessObject(createSampleProfile());

            AppFrame frame = new AppFrame();
            addViewHistoryUseCase(frame, dataAccess);
            frame.setVisible(true);
        });
    }

    private static void addViewHistoryUseCase(AppFrame frame,
                                              InMemoryProfileDataAccessObject dataAccess) {
        ViewHistoryViewModel viewModel = new ViewHistoryViewModel();
        ViewHistoryPresenter presenter = new ViewHistoryPresenter(viewModel);
        ViewHistoryInteractor interactor = new ViewHistoryInteractor(dataAccess, presenter);
        ViewHistoryController controller = new ViewHistoryController(interactor);

        frame.addScreen(new HistoryPanel(frame, controller, viewModel), ScreenNames.HISTORY);
    }

    /**
     * Sample data for development only. Replace once JSON storage is ready.
     */
    private static ChildProfile createSampleProfile() {
        ChildProfile profile = new ChildProfile();
        Pictogram water = new Pictogram(1, "water", "water.png");
        Pictogram apple = new Pictogram(2, "apple", "apple.png");
        LocalDateTime now = LocalDateTime.now();

        profile.addMessage(new CommunicationMessage(
                List.of(apple), "I want apple.", now.minusDays(1).withHour(15).withMinute(20)));
        profile.addMessage(new CommunicationMessage(
                List.of(water), "I want water.", now.withHour(10).withMinute(0)));
        profile.addMessage(new CommunicationMessage(
                List.of(water, water), "I want water water.", now.withHour(11).withMinute(30)));
        return profile;
    }
}