package view;

import interface_adapter.MovieDetail.MovieDetailController;
import interface_adapter.MovieDetail.MovieDetailState;
import interface_adapter.MovieDetail.MovieDetailViewModel;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for displaying movie details and poster information.
 */
public class MovieDetailView extends JPanel implements PropertyChangeListener {

    private final MovieDetailViewModel viewModel;
    private final JLabel titleLabel = new JLabel("Title: ");
    private final JLabel ratingLabel = new JLabel("Rating: ");
    private final JLabel releaseDateLabel = new JLabel("Release Date: ");
    private final JLabel posterLabel = new JLabel("Poster: ");
    private final JTextArea descriptionArea = new JTextArea(4, 30);
    private MovieDetailController controller;

    public MovieDetailView(MovieDetailViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        add(titleLabel);
        add(ratingLabel);
        add(releaseDateLabel);
        add(posterLabel);
        add(descriptionArea);

        refreshFromState(viewModel.getState());
    }

    public void setController(MovieDetailController controller) {
        this.controller = controller;
    }

    public void loadMovie(int movieID) {
        if (controller != null) {
            controller.execute(movieID);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof MovieDetailState) {
            refreshFromState((MovieDetailState) evt.getNewValue());
        } else {
            refreshFromState(viewModel.getState());
        }
    }

    private void refreshFromState(MovieDetailState state) {
        if (state == null) {
            return;
        }
        if (state.getErrorMessage() != null && !state.getErrorMessage().isBlank()) {
            titleLabel.setText("Error: " + state.getErrorMessage());
            ratingLabel.setText("Rating: -");
            releaseDateLabel.setText("Release Date: -");
            posterLabel.setText("Poster: -");
            descriptionArea.setText("");
            return;
        }

        titleLabel.setText("Title: " + state.getName());
        ratingLabel.setText("Rating: " + state.getRating());
        releaseDateLabel.setText("Release Date: " + state.getReleaseDate());
        String posterInfo = (state.getPosterPath() == null || state.getPosterPath().isBlank())
                ? "No poster"
                : "https://image.tmdb.org/t/p/w500" + state.getPosterPath();
        posterLabel.setText("Poster: " + posterInfo);
        descriptionArea.setText(state.getDescription());
    }
}