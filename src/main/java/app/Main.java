package app;

import data_access.GiphyStickerDataAccessObject;
import interface_adapter.search_sticker.SearchStickerController;
import interface_adapter.search_sticker.SearchStickerPresenter;
import interface_adapter.search_sticker.SearchStickerViewModel;
import use_case.search_sticker.SearchStickerInteractor;
import view.StickerPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Builds the app from the outside in and opens the window.
 * Needs the GIPHY_API_KEY environment variable in the run configuration.
 */
public class Main {
    public static void main(String[] args) {
        // Frameworks and drivers: data access
        final GiphyStickerDataAccessObject giphy =
                new GiphyStickerDataAccessObject(System.getenv("GIPHY_API_KEY"));

        // Interface adapters and use case, each given only what it depends on
        final SearchStickerViewModel viewModel = new SearchStickerViewModel();
        final SearchStickerPresenter presenter = new SearchStickerPresenter(viewModel);
        final SearchStickerInteractor interactor = new SearchStickerInteractor(giphy, presenter);
        final SearchStickerController controller = new SearchStickerController(interactor);

        // View
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame("DoGether");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new StickerPanel(viewModel, controller));
            frame.setSize(720, 560);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}