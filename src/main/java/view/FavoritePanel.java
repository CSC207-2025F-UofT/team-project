package view;

import interface_adapter.Favorite.FavoriteController;
import interface_adapter.Favorite.FavoriteViewModel;
import interface_adapter.Favorite.FavoriteViewModelListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.util.function.Supplier;

/** The add-to-favourites action for the currently displayed movie. */
public final class FavoritePanel extends JPanel {
    private final JButton addButton = new JButton("Add to Favourites");
    private final JLabel feedback = new JLabel(" ");
    private final FavoriteViewModel viewModel;
    private final FavoriteViewModelListener listener = state -> feedback.setText(state.getMessage());
    private int movieId;

    public FavoritePanel(FavoriteController controller, FavoriteViewModel viewModel,
                         Supplier<String> currentUsername) {
        super(new FlowLayout(FlowLayout.LEADING));
        this.viewModel = viewModel;
        add(addButton);
        add(feedback);
        setMovieId(0);
        addButton.addActionListener(event -> controller.addFavorite(currentUsername.get(), movieId));
    }

    /** Set the displayed TMDB movie ID, or pass zero if no details are available. */
    public void setMovieId(int movieId) {
        this.movieId = movieId;
        addButton.setEnabled(movieId > 0);
        feedback.setText(" ");
    }

    @Override
    public void addNotify() {
        super.addNotify();
        viewModel.addListener(listener);
    }

    @Override
    public void removeNotify() {
        viewModel.removeListener(listener);
        super.removeNotify();
    }
}
