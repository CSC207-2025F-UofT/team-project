package app;

import data_access.DefaultUserLocationDataAccess;
import data_access.InMemorySideQuestDataAccessObject;
import entity.SearchArea;
import interface_adapter.find_nearby_quests.FindNearbyQuestsController;
import interface_adapter.find_nearby_quests.FindNearbyQuestsPresenter;
import interface_adapter.find_nearby_quests.FindNearbyQuestsViewModel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import use_case.find_nearby_quests.FindNearbyQuestsInteractor;
import use_case.find_nearby_quests.QuestLocationDataAccessInterface;
import view.HomeView;
import view.MapView;
import view.journal.JournalStyle;

/**
 * Creates every object the app needs and connects them. Each teammate's use case
 * gets its own add... method so they can be wired in one place.
 *
 * <p>The window shows one page at a time (a {@link CardLayout}), like a phone app:
 * the home page has a button per page, and each page's back arrow returns home.
 */
public class AppBuilder {
    private static final String HOME_PAGE = "home";
    private static final String MAP_PAGE = "map";

    private final CardLayout pageLayout = new CardLayout();
    private final JPanel pages = new JPanel(pageLayout);
    private final HomeView homeView = new HomeView();

    private QuestLocationDataAccessInterface questDataAccess =
            InMemorySideQuestDataAccessObject.withSampleQuests();

    /** Creates the builder with only the home page. */
    public AppBuilder() {
        pages.add(homeView, HOME_PAGE);
    }

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
     * Adds the Map page (Use Case 4) and wires its use case. Opening the page runs
     * the use case (the trigger in the use case).
     *
     * @return this builder
     */
    public AppBuilder addFindNearbyQuestsUseCase() {
        FindNearbyQuestsViewModel viewModel =
                new FindNearbyQuestsViewModel(SearchArea.DEFAULT_RADIUS_KM);
        FindNearbyQuestsPresenter presenter = new FindNearbyQuestsPresenter(viewModel);
        FindNearbyQuestsInteractor interactor = new FindNearbyQuestsInteractor(
                questDataAccess, new DefaultUserLocationDataAccess(), presenter);
        FindNearbyQuestsController controller = new FindNearbyQuestsController(interactor);

        MapView mapView = new MapView(viewModel, controller);
        mapView.setBackAction(() -> pageLayout.show(pages, HOME_PAGE));
        // Placeholder until the quest page exists: the quest page's owner replaces this.
        mapView.setOpenQuestAction(questId -> JOptionPane.showMessageDialog(
                mapView, "The quest page for \"" + questId + "\" will open here.",
                "Open quest", JOptionPane.INFORMATION_MESSAGE));

        pages.add(mapView, MAP_PAGE);
        homeView.addPageButton("Map", () -> {
            pageLayout.show(pages, MAP_PAGE);
            controller.execute(mapView.getSelectedRadiusKm());
        });
        return this;
    }

    /**
     * Builds the main window, sized like the Journal pages but never taller than
     * the screen.
     *
     * @return the window, not yet visible
     */
    public JFrame build() {
        JFrame frame = new JFrame("Linked Out");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pages.setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
        frame.setContentPane(pages);
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
