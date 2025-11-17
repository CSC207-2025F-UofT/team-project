package view;

import interface_adapter.post_review.PostController;
import interface_adapter.view_song.ViewSongController;
import interface_adapter.view_song.ViewSongViewModel;

import javax.swing.*;
import java.awt.*;


public class SongProfileView extends JPanel {
    private final ViewSongController viewSongController;
    private final PostController postController;
    private final ViewSongViewModel viewModel;

    private final JLabel songNameLabel = new JLabel();
    private final JLabel artistLabel = new JLabel();
    private final JLabel averageRatingLabel = new JLabel();

    private final JPanel reviewsPanel = new JPanel();

    private final JButton addReview = new JButton("Write a Review");
    private final JButton backButton = new JButton("Back");
    private final JButton refreshButton = new JButton("Refresh");

    public SongProfileView(ViewSongController viewSongController, ViewSongViewModel viewModel,  PostController postController) {
        this.viewSongController = viewSongController;
        this.viewModel = viewModel;
        this.postController = postController;


        setLayout(new BorderLayout());


        //  Song Information  //
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        songNameLabel.setText(viewSongController.getSongName);
        artistLabel.setText("By" + viewSongController.getArtist);
        songNameLabel.setText(viewSongController.getAverageRating);
        labelPanel.add(songNameLabel);
        labelPanel.add(artistLabel);
        labelPanel.add(averageRatingLabel);
        add(labelPanel, BorderLayout.NORTH);


        // Buttons //
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // from viewmodel -> add list of reviews to reviewsScrollPane
        // REVIEWS SECTION //
        reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.X_AXIS));
        JScrollPane reviewsScrollPane = new JScrollPane();
        // TODO: Add reviews from view model in in scrollpane()
        reviewsPanel.add(reviewsScrollPane);

        reviewsPanel.add(reviewsScrollPane, BorderLayout.CENTER);
        reviewsPanel.add(addReview, BorderLayout.EAST);

        add(reviewsPanel, BorderLayout.CENTER);

        //LISTENERS//
        addReview.addActionListener(e -> openWriteReviewDialog());
        backButton.addActionListener(e -> viewSongController.goBackToHome);

    }


    private void openWriteReviewDialog() {
        JDialog postReviewDialog = new JDialog((Frame) null, "Write a Review", true);
        // set pixel size?
        postReviewDialog.setLocationRelativeTo(this);
        postReviewDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Comment area as scrollpane //
        JTextArea comment = new JTextArea();
        postReviewDialog.add(comment, BorderLayout.CENTER);
        JScrollPane commentScrollPane = new JScrollPane(comment);

        //Rating Dropdown menu//
        String[] ratings = {"1", "2", "3", "4", "5"};
        JComboBox<String> selected_ratings = new JComboBox<>(ratings);

        // Post Button //
        JButton postButton = new JButton("Post Review");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(postButton);

        postReviewDialog.setLayout(new BoxLayout(postReviewDialog, BoxLayout.Y_AXIS));
        postReviewDialog.add(commentScrollPane, BorderLayout.CENTER);
        postReviewDialog.add(selected_ratings, BorderLayout.SOUTH);

        postReviewDialog.add(buttonPanel);

        postReviewDialog.setVisible(true);


        postButton.addActionListener(e -> {
            String content = comment.getText();
            Object selected = selected_ratings.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(postReviewDialog, "Please select a rating");
            }
            else {
                int rating = Integer.parseInt((String) selected);
                int songID = viewSongController.getSongId;
                String username = viewSongController.getUsername;
                postController.post(content, rating, username, songID);
                postReviewDialog.dispose();
            }
        });

    }
}
