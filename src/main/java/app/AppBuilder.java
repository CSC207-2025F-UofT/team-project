package app;

import data_access.BundledWeatherDataAccessObject;
import data_access.FileGameStateDataAccessObject;
import data_access.OpenMeteoWeatherDataAccessObject;
import entity.GameDay;
import entity.Player;
import interface_adapter.ViewManagerModel;
import interface_adapter.continue_game.ContinueGameController;
import interface_adapter.continue_game.ContinueGamePresenter;
import interface_adapter.continue_game.TitleViewModel;
import interface_adapter.rest.ResidenceFormat;
import interface_adapter.rest.RestController;
import interface_adapter.rest.RestPresenter;
import interface_adapter.rest.RestViewModel;
import java.awt.BorderLayout;
import java.nio.file.Path;
import java.time.LocalDate;
import javax.swing.JPanel;
import use_case.continue_game.ContinueGameInteractor;
import use_case.rest.RestInteractor;
import view.GameView;
import view.ResidenceView;
import view.TitleView;

public class AppBuilder {
    private static final LocalDate START_DATE = LocalDate.of(2025, 9, 2);
    private static final int MAX_ENERGY = 100;

    private final Path saveFile;
    private final JPanel root = new JPanel(new BorderLayout());

    public AppBuilder(Path saveFile) {
        this.saveFile = saveFile;
    }

    public JPanel build() {
        showGame(TitleViewModel.VIEW_NAME);
        return root;
    }

    private void showGame(String firstPage) {
        // A new session resets memory without overwriting the saved checkpoint.
        FileGameStateDataAccessObject gameState = new FileGameStateDataAccessObject(
                saveFile, new GameDay(1, START_DATE), new Player(MAX_ENERGY, MAX_ENERGY));
        OpenMeteoWeatherDataAccessObject weather = new OpenMeteoWeatherDataAccessObject(
                OpenMeteoWeatherDataAccessObject.DEFAULT_BASE_URL, new BundledWeatherDataAccessObject());
        ViewManagerModel navigation = new ViewManagerModel();
        RestViewModel residenceModel = new RestViewModel();
        TitleViewModel titleModel = new TitleViewModel();

        RestController rest = new RestController(
                new RestInteractor(gameState, weather, new RestPresenter(residenceModel)));
        ContinueGameController continueGame = new ContinueGameController(new ContinueGameInteractor(
                gameState, new ContinueGamePresenter(titleModel, residenceModel, navigation)));

        TitleView title = new TitleView(titleModel, continueGame, () -> showGame("street"));
        ResidenceView residence = new ResidenceView(residenceModel, rest,
                () -> navigation.setActiveView("street"));
        GameView game = new GameView(title, residence, navigation);
        residenceModel.setState(ResidenceFormat.day(1, START_DATE), ResidenceFormat.noWeather(),
                ResidenceFormat.energy(MAX_ENERGY, MAX_ENERGY));
        navigation.setActiveView(firstPage);

        root.removeAll();
        root.add(game, BorderLayout.CENTER);
        root.revalidate();
        root.repaint();
    }
}
