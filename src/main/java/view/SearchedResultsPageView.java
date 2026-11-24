package view;

import interface_adapter.Reviews.ReviewsController;
import interface_adapter.Search.*;
//import interface_adapter.login.LoginState;
//import interface_adapter.login.LoginViewModel;

//import interface_adapter.login.LoginViewModelListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * The View for when the user is logging into the program.
 */
public class SearchedResultsPageView extends JPanel implements ActionListener, SearchViewModelListener {

    private final SearchedViewModel searchedViewModel;

    private final JTextField searchInputField = new JTextField(25);
    private final JLabel searchErrorField = new JLabel(" ");


    private final JButton search;
    private final JButton favourites;
    private final JButton watchlist;
//    private final JButton home;
    private SearchController searchController = null;
    private ReviewsController reviewsController = null;

    public SearchedResultsPageView(SearchedViewModel searchedViewModel){

        this.searchedViewModel = searchedViewModel;
        this.searchedViewModel.addListener(this);

        this.setBackground(new Color(240, 249, 250));

        final JLabel title = new JLabel("Flicksy Search");
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel searchInfo = new LabelTextPanel(
                new JLabel("Search"), searchInputField);
        searchInfo.setOpaque(false);

        final JPanel buttons = new JPanel();
        buttons.setOpaque(false);

        search = new JButton("Search");
        search.setFont(new Font("Serif", Font.PLAIN, 16));
        buttons.add(search);

        favourites = new JButton("Favorites");
        favourites.setFont(new Font("Serif", Font.PLAIN, 16));
        buttons.add(favourites);

        watchlist = new JButton("Watchlist");
        watchlist.setFont(new Font("Serif", Font.PLAIN, 16));
        buttons.add(watchlist);

//        home = new JButton("Home");
//        home.setFont(new Font("Serif", Font.PLAIN, 16));
//        buttons.add(home);

        final JPanel searchResults = new JPanel();
//        searchResults.setOpaque(false);
        searchResults.setBackground(new Color(240, 249, 250));
        // helps each movie data be properly laid out
        searchResults.setLayout(new BoxLayout(searchResults, BoxLayout.Y_AXIS));

        String[][] results = searchedViewModel.getState().getOutputMovieData();
        if (results == null) {
            results = new String[0][];
        }

        //results returned in the form of {name, description, poster path}

        for(String[] data: results){
            int movieId = Integer.parseInt(data[3]);
            JPanel movieData = new JPanel();
            movieData.setOpaque(false);
            try{
                URL url = new URL(data[2]);
                BufferedImage poster = ImageIO.read(url);
                int targetWidth = 200;
                int targetHeight = 300;


                Image scaledImage = poster.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);


                ImageIcon icon = new ImageIcon(scaledImage);

                movieData.add(new JLabel(icon));
//                movieData.add(new JLabel(new ImageIcon(poster)));
            } catch (Exception e) {
                movieData.add(new JLabel("No Poster Available"));
            }
//            JLabel description = new JLabel(data[1]);
//            movieData.add(description);

//            JTextArea description = new JTextArea(data[1]);
//            description.setWrapStyleWord(true);      // Wrap at word boundaries, not mid-word
//            description.setLineWrap(true);           // Enable line wrapping
//            description.setOpaque(false);            // Make background transparent like JLabel
//            description.setEditable(false);          // Prevent user editing
//            description.setFocusable(false);         // Remove focus border/cursor
//            description.setFont(new Font("Serif", Font.PLAIN, 14));
//            movieData.add(description);

            JPanel details = new JPanel();
            details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

            JLabel movieTitle = new JLabel(data[0]);
            movieTitle.setFont(new Font("Serif", Font.PLAIN, 14));
            movieTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            details.add(movieTitle);

            JLabel releaseDate = new JLabel("Release Date: " + data[1]);
            releaseDate.setFont(new Font("Serif", Font.PLAIN, 14));
            details.add(releaseDate);




            JPanel button = new JPanel();
            button.setOpaque(false);
            JButton viewDetails = new JButton("View Details");
            viewDetails.setFont(new Font("Serif", Font.PLAIN, 14));
            button.add(viewDetails);

            JButton reviews = new JButton("Reviews");
            reviews.setFont(new Font("Serif", Font.PLAIN, 14));
            button.add(reviews);

            viewDetails.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(e.getSource().equals(viewDetails)){
                                // TODO: the controller for the details controller
                            }
                        }
                    }
            );

            reviews.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(e.getSource().equals(reviews)){
                                reviewsController.execute(movieId);
                            }
                        }
                    }
            );


            details.add(button);
            movieData.add(details);
            searchResults.add(movieData);

        }
        JScrollPane scrollPane = new JScrollPane(searchResults);

        search.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(search)) {
                            final SearchState currentState = searchedViewModel.getState();

                            // TODO: the controller needs to be the get description page
                            searchController.execute(
                                    currentState.getTitle()
                            );
                        }

                        if (evt.getSource().equals(favourites)) {
//                            final SearchState currentState = searchBarViewModel.getState();
//
//                            searchController.execute(
//                                    currentState.getTitle()
//                            ); SHOULD CHANGE BASED ON THE FAVOURITES PAGE VIEW

                        }

                        if (evt.getSource().equals(watchlist)) {
//                            final SearchState currentState = searchBarViewModel.getState();
//
//                            searchController.execute(
//                                    currentState.getTitle()
//                            ); SHOULD CHANGE BASED ON THE WATCHLIST PAGE VIEW

                        }

                    }
                }
        );
//        home.addActionListener(
//                new ActionListener() {
//                    public void actionPerformed(ActionEvent evt) {
//                        if (evt.getSource().equals(search)) {
//                            final SearchState currentState = searchedViewModel.getState();
//
//                            searchController.execute(
//                                    currentState.getTitle()
//                            );
//                        }
//                    }
//                }
//        );


        searchInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SearchState currentState = searchedViewModel.getState();
                currentState.setTitle(searchInputField.getText());
                searchedViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        this.add(title);
        this.add(searchInfo);
        this.add(searchErrorField);
        this.add(buttons);
        this.add(scrollPane);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void stateChanged() {
        setFields(searchedViewModel.getState());
        searchErrorField.setText(searchedViewModel.getState().getSearchError());
        // TODO: confirm if the error of the state should be different
    }

    private void setFields(SearchState state) {
        searchInputField.setText(state.getTitle());
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void setReviewsController(ReviewsController reviewsController) {
        this.reviewsController = reviewsController;
    }
}
