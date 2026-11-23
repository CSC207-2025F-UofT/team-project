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
import use_case.continue_game.ContinueGameInteractor;
import use_case.rest.RestInteractor;
import view.ResidenceView;
import view.TitleView;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Temporary entry point: title screen and residence. To be replaced by the shared campus navigation.
 */
public class Main {
    /** Real date matched to day 1 (team rule still to be finalized). */
    private static final LocalDate START_DATE = LocalDate.of(2025, 9, 2);
    private static final int MAX_ENERGY = 100;
    private static final Path SAVE_FILE =
            Path.of(System.getProperty("user.home"), ".stressdew-valley", "checkpoint.json");

    public static void main(String[] args) {
        FileGameStateDataAccessObject gameState = new FileGameStateDataAccessObject(
                SAVE_FILE, new GameDay(1, START_DATE), new Player(MAX_ENERGY, MAX_ENERGY));
        OpenMeteoWeatherDataAccessObject weather = new OpenMeteoWeatherDataAccessObject(
                OpenMeteoWeatherDataAccessObject.DEFAULT_BASE_URL, new BundledWeatherDataAccessObject());

        ViewManagerModel viewManagerModel = new ViewManagerModel();
        RestViewModel residenceViewModel = new RestViewModel();
        TitleViewModel titleViewModel = new TitleViewModel();

        RestController restController = new RestController(
                new RestInteractor(gameState, weather, new RestPresenter(residenceViewModel)));
        ContinueGameController continueController = new ContinueGameController(new ContinueGameInteractor(
                gameState, new ContinueGamePresenter(titleViewModel, residenceViewModel, viewManagerModel)));

        SwingUtilities.invokeLater(() -> {
            CardLayout cards = new CardLayout();
            JPanel views = new JPanel(cards);
            views.add(new TitleView(titleViewModel, continueController, () -> {
                residenceViewModel.setState(ResidenceFormat.day(1, START_DATE), ResidenceFormat.noWeather(),
                        ResidenceFormat.energy(MAX_ENERGY, MAX_ENERGY));
                viewManagerModel.setActiveView(RestViewModel.VIEW_NAME);
            }), TitleViewModel.VIEW_NAME);
            views.add(new ResidenceView(residenceViewModel, restController), RestViewModel.VIEW_NAME);
            viewManagerModel.addPropertyChangeListener(evt -> cards.show(views, viewManagerModel.getActiveView()));

            JFrame frame = new JFrame("Stressdew Valley");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(views);
            frame.setSize(480, 260);
            frame.setVisible(true);
            viewManagerModel.setActiveView(TitleViewModel.VIEW_NAME);
        });
    }
}
