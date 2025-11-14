package view;

import entity.Review;
import interface_adapter.view_profile_reviews.ProfileReviewsController;
import interface_adapter.view_profile_reviews.ProfileReviewsViewModel;

import javax.swing.*;
        import java.awt.*;
        import java.util.List;

public class UserProfileView extends JPanel {

    private final ProfileReviewsViewModel viewModel;
    private final ProfileReviewsController controller;

    private final JLabel usernameLabel = new JLabel();
    private final JPanel reviewsPanel = new JPanel();
    private final JButton backButton = new JButton("Back");
    private final JButton refreshButton = new JButton("Refresh");

    public UserProfileView(ProfileReviewsViewModel viewModel,
                           ProfileReviewsController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        setLayout(new BorderLayout());

        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(usernameLabel, BorderLayout.NORTH);

        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(reviewsPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> controller.goBackToHome());
        refreshButton.addActionListener(e -> controller.refreshProfile());

        refresh();
    }

    public void refresh() {
        usernameLabel.setText("Reviews by " + viewModel.getUsername());
        reviewsPanel.removeAll();

        List<Review> reviews = viewModel.getReviews();

        for (Review r : reviews) {
            JPanel row = new JPanel(new BorderLayout());
            String header = r.getSong().getTitle() + "  -  " + r.getRating() + "/5";
            row.add(new JLabel(header), BorderLayout.NORTH);
            row.add(new JLabel(r.getComment()), BorderLayout.CENTER);
            JLabel upvotesLabel = new JLabel("↑ " + r.getUpvotes());
            row.add(upvotesLabel, BorderLayout.EAST);
            reviewsPanel.add(row);
        }

        reviewsPanel.revalidate();
        reviewsPanel.repaint();
    }
}
