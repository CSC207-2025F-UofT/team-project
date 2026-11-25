package app;

import data_access.DefaultUserLocationDataAccess;
import data_access.InMemorySideQuestDataAccessObject;
import entity.SearchArea;
import interface_adapter.find_nearby_quests.FindNearbyQuestsController;
import interface_adapter.find_nearby_quests.FindNearbyQuestsPresenter;
import interface_adapter.find_nearby_quests.FindNearbyQuestsViewModel;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import use_case.find_nearby_quests.FindNearbyQuestsInteractor;
import use_case.find_nearby_quests.QuestLocationDataAccessInterface;
import view.MapView;
import view.journal.JournalStyle;

/**
 * Creates every object the app needs and connects them. Each teammate's use case
 * gets its own add... method so they can be wired in one place.
 *
 * <p>For now the window shows only the Map page. When other pages are wired in,
 * the team adds navigation between them here (a home page or a tab bar).
 */
public class AppBuilder {
    // Sample quests until the CSV storage can load quests. Then switch to:
    // new FileQuestLocationDataAccess(new FileSideQuestDataAccessObject(path))
    private QuestLocationDataAccessInterface questDataAccess =
            InMemorySideQuestDataAccessObject.withSampleQuests();

    private MapView mapView;
    private FindNearbyQuestsController findNearbyQuestsController;

    /**
     * Uses a different source of quests for the map, for example the file-based one.
     *
     * @param dataAccess the quest source
     * @return this builder
     */
    public AppBuilder withQuestDataAccess(QuestLocationDataAccessInterface dataAccess) {
        this.questDataAccess = dataAccess;
        return this;
    }

    /**
     * Adds the Map page (Use Case 4) and wires its use case.
     *
     * @return this builder
     */
    public AppBuilder addFindNearbyQuestsUseCase() {
        FindNearbyQuestsViewModel viewModel =
                new FindNearbyQuestsViewModel(SearchArea.DEFAULT_RADIUS_KM);
        FindNearbyQuestsPresenter presenter = new FindNearbyQuestsPresenter(viewModel);
        FindNearbyQuestsInteractor interactor = new FindNearbyQuestsInteractor(
                questDataAccess, new DefaultUserLocationDataAccess(), presenter);
        findNearbyQuestsController = new FindNearbyQuestsController(interactor);

        mapView = new MapView(viewModel, findNearbyQuestsController);
        // Placeholder until the quest page exists: the quest page's owner replaces this.
        mapView.setOpenQuestAction(questId -> JOptionPane.showMessageDialog(
                mapView, "The quest page for \"" + questId + "\" will open here.",
                "Open quest", JOptionPane.INFORMATION_MESSAGE));
        return this;
    }

    /**
     * Builds the main window, sized like the Journal pages but never taller than
     * the screen. Showing the Map page runs Use Case 4 (its trigger).
     *
     * @return the window, not yet visible
     */
    public JFrame build() {
        JFrame frame = new JFrame("Linked Out");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (mapView != null) {
            mapView.setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
            frame.setContentPane(mapView);
            findNearbyQuestsController.execute(mapView.getSelectedRadiusKm());
        }
        frame.setResizable(false);
        frame.pack();

        Rectangle usable =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        if (frame.getHeight() > usable.height) {
            frame.setSize(frame.getWidth(), usable.height);
        }
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
