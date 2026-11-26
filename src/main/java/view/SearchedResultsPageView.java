package view;

import interface_adapter.AddToWatchlist.AddToWatchlistController;
import interface_adapter.MovieDetail.MovieDetailController;
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
import java.util.function.IntFunction;

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
    private MovieDetailController movieDetailController = null;
    private AddToWatchlistController addToWatchlistController = null;
    private String username = "";

    public SearchedResultsPageView(SearchedViewModel searchedViewModel) {
        this(searchedViewModel, null);
    }

    /** The favourite action saves the selected movie and returns the presenter's feedback. */
    public SearchedResultsPageView(SearchedViewModel searchedViewModel, IntFunction<String> addFavorite) {

        this.searchedViewModel = searchedViewModel;
        this.searchedViewModel.addListener(this);

        this.setBackground(new Color(240, 249, 250));

        final JLabel title = new JLabel("Flicksy Search");
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel searchInfo = new LabelTextPanel(
                new JLabel("Search"), searchInputField);
        searchInfo.setOpaque(false);

        final JPanel buttons = new JPanel();
        buttons.setOpaque(false);

        search = new JButton("Search");
        search.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buttons.add(search);

        favourites = new JButton("Favorites");
        favourites.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buttons.add(favourites);

        watchlist = new JButton("Watchlist");
        watchlist.setFont(new Font("SansSerif", Font.PLAIN, 16));
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
            String movieTitleValue = data[0];
            String posterURL = data[2];

            JPanel movieData = new JPanel(new BorderLayout(20, 5));
            movieData.setAlignmentX(Component.LEFT_ALIGNMENT);
            movieData.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            movieData.setOpaque(false);
            try{
                URL url = new URL(posterURL);
                BufferedImage poster = ImageIO.read(url);
                int targetWidth = 200;
                int targetHeight = 300;


                Image scaledImage = poster.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);


                ImageIcon icon = new ImageIcon(scaledImage);

                movieData.add(new JLabel(icon), BorderLayout.WEST);
//                movieData.add(new JLabel(new ImageIcon(poster)));
            } catch (Exception e) {
                JLabel noPoster = new JLabel("No Poster Available");
                noPoster.setPreferredSize(new Dimension(150, 225));
                movieData.add(noPoster, BorderLayout.WEST);
            }

            JPanel details = new JPanel();
            details.setOpaque(false);
            details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

            JLabel movieTitle = new JLabel(movieTitleValue);
            movieTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
            movieTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            details.add(movieTitle);

            JLabel releaseDate = new JLabel("Release Date: " + data[1]);
            releaseDate.setFont(new Font("SansSerif", Font.PLAIN, 16));
            releaseDate.setAlignmentX(Component.CENTER_ALIGNMENT);
            details.add(releaseDate);

//            details.add(Box.createRigidArea(new Dimension(0, 15)));



            JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            button.setMaximumSize(new Dimension(
                    Integer.MAX_VALUE,
                    button.getPreferredSize().height
            ));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            button.setOpaque(false);
            JButton viewDetails = new JButton("View Details");
            viewDetails.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.add(viewDetails);

            JButton reviews = new JButton("Reviews");
            reviews.setFont(new Font("SansSerif", Font.PLAIN, 14));
            button.add(reviews);

            JLabel favoriteFeedback = new JLabel();
            favoriteFeedback.setAlignmentX(Component.CENTER_ALIGNMENT);
            JButton favoriteButton = new JButton("Add to Favorites");
            favoriteButton.setFont(reviews.getFont());
            favoriteButton.setEnabled(addFavorite != null && movieId > 0);
            favoriteButton.addActionListener(event -> favoriteFeedback.setText(addFavorite.apply(movieId)));
            button.add(favoriteButton);

            JButton watchlistButton = new JButton("Add to Watchlist");
            watchlistButton.setFont(reviews.getFont());
//            watchlistButton.setEnabled(false);
            button.add(watchlistButton);

            viewDetails.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(e.getSource().equals(viewDetails)){
                                movieDetailController.execute(movieId);
                            }
                        }
                    }
            );

            watchlistButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(e.getSource().equals(watchlistButton)){
                                addToWatchlistController.execute(
                                        username,
                                        String.valueOf(movieId),
                                        movieTitleValue, posterURL);
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
            details.add(Box.createRigidArea(new Dimension(0, 5)));
            details.add(favoriteFeedback);
            details.add(Box.createVerticalGlue());
            movieData.add(details, BorderLayout.CENTER);

            searchResults.add(movieData);

//            details.add(button);
//            details.add(Box.createRigidArea(new Dimension(0, 5)));
//            details.add(favoriteFeedback);
//            details.add(Box.createVerticalGlue());
//
//            movieData.add(details, BorderLayout.CENTER);
//
//            movieData.setMaximumSize(new Dimension(
//                    Integer.MAX_VALUE,
//                    movieData.getPreferredSize().height
//            ));
//
//            searchResults.add(movieData);
//            searchResults.add(new JSeparator(SwingConstants.HORIZONTAL));

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
                    }
                }
        );


//        watchlist.addActionListener(
//                new ActionListener() {
//                    public void actionPerformed(ActionEvent evt) {
//                        if (evt.getSource().equals(watchlist)) {
//                            final SearchState currentState = searchBarViewModel.getState();
//
//                            watchlistController.execute(
//                                    "Still need to add user",
//                            );
//                        }
//                    }
//                }
//        );
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

    public void onOpenFavorites(Runnable action) {
        favourites.addActionListener(event -> action.run());
    }

    public void onOpenWatchlist(Runnable action) {
        watchlist.addActionListener(event -> action.run());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void setReviewsController(ReviewsController reviewsController) {
        this.reviewsController = reviewsController;
    }

    public void setMovieDetailController(MovieDetailController movieDetailController) {
        this.movieDetailController = movieDetailController;
    }

    public void setAddToWatchlistController(AddToWatchlistController addToWatchlistController){
        this.addToWatchlistController = addToWatchlistController;
    }
}
