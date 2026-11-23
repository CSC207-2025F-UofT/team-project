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
import java.awt.Dimension;
import java.nio.file.Path;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import use_case.continue_game.ContinueGameInteractor;
import use_case.rest.RestInteractor;
import view.GameView;
import view.ResidenceView;
import view.TitleView;

public class Main {
    private static final LocalDate START_DATE = LocalDate.of(2025, 9, 2);
    private static final int MAX_ENERGY = 100;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Stressdew Valley");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Path saveFile = Path.of(System.getProperty("user.home"), ".stressdew-valley", "checkpoint.json");
            window.setContentPane(createView(saveFile));
            window.setSize(800, 550);
            window.setMinimumSize(new Dimension(720, 480));
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

    public static JPanel createView(Path saveFile) {
        JPanel root = new JPanel(new BorderLayout());
        showGame(root, saveFile, "title");
        return root;
    }

    private static void showGame(JPanel root, Path saveFile, String firstPage) {
        // New Game starts fresh in memory; the checkpoint stays unchanged.
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

        TitleView title = new TitleView(titleModel, continueGame,
                () -> showGame(root, saveFile, "street"));
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
