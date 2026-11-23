package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import data_access.GameDataAccessObject;
import data_access.InMemoryPlayerDataAccessObject;
import data_access.JSONCityDataAccessObject;
import data_access.MapboxImageDataAccessObject;
import data_access.PlaceholderImageDataAccessObject;
import interface_adapter.ViewManagerModel;
import use_case.shared.ImageDataAccessInterface;
import view.GameView;
import view.MainMenuView;
import view.ResultView;
import view.StatsView;
import view.ViewManager;

/**
 * Wires the whole app together.
 *
 * This is the ONLY file that names concrete data access classes. To swap a fake for the real
 * version (e.g. InMemoryPlayerDataAccessObject -> a SQLite one), change the one line below.
 *
 * Each person fills in only their own add...UseCase() method.
 */
public class AppBuilder {
    public static final int IMAGE_SIZE = 640;
    private static final String ANSWER_CITIES_PATH = "src/main/resources/data/answer_cities.json";
    private static final String GUESSABLE_CITIES_PATH = "src/main/resources/data/guessable_cities.json";

    // ---- Data access: each created once and shared by every interactor that needs it ----
    private final JSONCityDataAccessObject cityDataAccessObject =
            new JSONCityDataAccessObject(ANSWER_CITIES_PATH, GUESSABLE_CITIES_PATH);
    private final InMemoryPlayerDataAccessObject playerDataAccessObject = new InMemoryPlayerDataAccessObject();
    private final GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();
    private final ImageDataAccessInterface imageDataAccessObject = createImageDataAccessObject();

    // ---- Views and navigation ----
    private final JFrame application = new JFrame("CityGuesser");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private MainMenuView mainMenuView;
    private GameView gameView;
    private StatsView statsView;
    private ResultView resultView;

    /** Uses Mapbox when MAPBOX_ACCESS_TOKEN is set, otherwise a placeholder picture. */
    private static ImageDataAccessInterface createImageDataAccessObject() {
        String token = System.getenv("MAPBOX_ACCESS_TOKEN");
        if (token == null || token.isBlank()) {
            System.err.println("MAPBOX_ACCESS_TOKEN is not set; using placeholder images.");
            return new PlaceholderImageDataAccessObject(IMAGE_SIZE);
        }
        return new MapboxImageDataAccessObject(token, IMAGE_SIZE);
    }

    // ================= Views =================

    public AppBuilder addMainMenuView() {
        mainMenuView = new MainMenuView(viewManagerModel);
        cardPanel.add(mainMenuView, MainMenuView.VIEW_NAME);
        return this;
    }

    public AppBuilder addGameView() {
        gameView = new GameView(viewManagerModel);
        cardPanel.add(gameView, GameView.VIEW_NAME);
        return this;
    }

    public AppBuilder addStatsView() {
        statsView = new StatsView(viewManagerModel);
        cardPanel.add(statsView, StatsView.VIEW_NAME);
        return this;
    }

    public AppBuilder addResultView() {
        resultView = new ResultView(application, viewManagerModel);
        viewManager.addPopup(ResultView.VIEW_NAME, resultView);
        return this;
    }

    // ================= Use cases =================
    // Pattern for each one:
    //   ViewModel vm = new XViewModel();
    //   OutputBoundary presenter = new XPresenter(vm, viewManagerModel);
    //   InputBoundary interactor = new XInteractor(<data access objects>, presenter);
    //   XController controller = new XController(interactor);
    //   <the view>.setXController(controller);

    /** Use Case 1 (Regan). Interactor gets: playerDataAccessObject (as EnterNameDataAccessInterface),
     *  gameDataAccessObject (as CurrentPlayerDataAccessInterface). */
    public AppBuilder addEnterNameUseCase() {
        return this;
    }

    /** Use Case 2 (Felix). Interactor gets: cityDataAccessObject (as StartRoundDataAccessInterface),
     *  imageDataAccessObject, gameDataAccessObject (as CurrentRoundDataAccessInterface). */
    public AppBuilder addStartRoundUseCase() {
        return this;
    }

    /** Use Case 3 (Elliot). Interactor gets: cityDataAccessObject (as SubmitGuessDataAccessInterface),
     *  imageDataAccessObject, gameDataAccessObject (as CurrentRoundDataAccessInterface). */
    public AppBuilder addSubmitGuessUseCase() {
        return this;
    }

    /** Use Case 4 (Ingrid). Interactor gets: playerDataAccessObject (as FinishRoundDataAccessInterface),
     *  gameDataAccessObject (as both CurrentPlayerDataAccessInterface and CurrentRoundDataAccessInterface). */
    public AppBuilder addFinishRoundUseCase() {
        return this;
    }

    /** Use Case 5 (Nikola). Interactor gets: playerDataAccessObject (as ViewStatsDataAccessInterface),
     *  gameDataAccessObject (as CurrentPlayerDataAccessInterface). */
    public AppBuilder addViewStatsUseCase() {
        return this;
    }

    // ================= Build =================

    public JFrame build() {
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setContentPane(cardPanel);

        viewManagerModel.setState(MainMenuView.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        application.pack();
        application.setLocationRelativeTo(null);
        return application;
    }
}
