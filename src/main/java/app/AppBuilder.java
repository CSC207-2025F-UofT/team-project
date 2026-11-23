package app;

import data_access.DefaultUserLocationDataAccess;
import data_access.InMemorySideQuestDataAccessObject;
import entity.SearchArea;
import interface_adapter.find_nearby_quests.FindNearbyQuestsController;
import interface_adapter.find_nearby_quests.FindNearbyQuestsPresenter;
import interface_adapter.find_nearby_quests.FindNearbyQuestsViewModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import use_case.find_nearby_quests.FindNearbyQuestsInteractor;
import use_case.find_nearby_quests.QuestLocationDataAccessInterface;
import view.MapView;

/**
 * Creates every object the app needs and connects them. Each teammate's use case
 * gets its own add... method so they can be wired in one place.
 */
public class AppBuilder {
    private static final String MAP_TAB = "Map";

    private final JTabbedPane tabs = new JTabbedPane();
    private QuestLocationDataAccessInterface questDataAccess =
            InMemorySideQuestDataAccessObject.withSampleQuests();

    private FindNearbyQuestsViewModel findNearbyQuestsViewModel;
    private FindNearbyQuestsController findNearbyQuestsController;
    private MapView mapView;

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
     * Adds the Map tab (Use Case 4) and wires its use case.
     *
     * @return this builder
     */
    public AppBuilder addFindNearbyQuestsUseCase() {
        findNearbyQuestsViewModel = new FindNearbyQuestsViewModel(SearchArea.DEFAULT_RADIUS_KM);
        FindNearbyQuestsPresenter presenter =
                new FindNearbyQuestsPresenter(findNearbyQuestsViewModel);
        FindNearbyQuestsInteractor interactor = new FindNearbyQuestsInteractor(
                questDataAccess, new DefaultUserLocationDataAccess(), presenter);
        findNearbyQuestsController = new FindNearbyQuestsController(interactor);

        mapView = new MapView(findNearbyQuestsViewModel, findNearbyQuestsController);
        // Placeholder until the quest page exists: the quest page's owner replaces this.
        mapView.setOpenQuestAction(questId -> JOptionPane.showMessageDialog(
                mapView, "The quest page for \"" + questId + "\" will open here.",
                "Open quest", JOptionPane.INFORMATION_MESSAGE));
        tabs.addTab(MAP_TAB, mapView);
        return this;
    }

    /**
     * Builds the main window. Opening the Map tab runs Use Case 4.
     *
     * @return the window, not yet visible
     */
    public JFrame build() {
        JFrame frame = new JFrame("Linked Out");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(tabs);

        if (mapView != null) {
            tabs.addChangeListener(event -> {
                if (tabs.getSelectedComponent() == mapView) {
                    findNearbyQuestsController.execute(mapView.getSelectedRadiusKm());
                }
            });
            if (tabs.getSelectedComponent() == mapView) {
                findNearbyQuestsController.execute(mapView.getSelectedRadiusKm());
            }
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
