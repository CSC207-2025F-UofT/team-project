package view;
import interface_adapter.Reviews.ReviewsViewModel;
import interface_adapter.Reviews.ReviewsState;
import interface_adapter.Reviews.ReviewsController;
import interface_adapter.Reviews.ReviewsViewModelListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReviewsView extends JPanel implements ReviewsViewModelListener {
    private final ReviewsViewModel viewModel;
    private final ReviewsController controller;
    private final JTextArea reviewsArea = new JTextArea();

    public ReviewsView(ReviewsViewModel viewModel, ReviewsController controller){
        this.viewModel = viewModel;
        this.controller = controller;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Movie Reviews");

        reviewsArea.setEditable(false);
        reviewsArea.setLineWrap(true);
        reviewsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(reviewsArea);

        add(title, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        viewModel.addListener(this);
    }

    public void ShowReviewsMovie(int movieId){
        reviewsArea.setText("Loading reviews...");
        controller.execute(movieId);
    }

    @Override
    public void stateChanged(){
        ReviewsState state = viewModel.getState();

        if(!state.getErrorMessages().isEmpty()){
            reviewsArea.setText(state.getErrorMessages());
            return;
        }
        List<String> reviews = state.getReviews();

        if(reviews.isEmpty()){
            reviewsArea.setText("No reviews");
        } else {
            reviewsArea.setText(String.join("\n\n-------------------\n\n", reviews));
        }
    }

}
