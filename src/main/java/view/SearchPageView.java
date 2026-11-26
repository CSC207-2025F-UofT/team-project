package view;

import interface_adapter.AddToWatchlist.AddToWatchlistController;
import interface_adapter.Favorite.FavoriteController;
import interface_adapter.Search.SearchBarViewModel;
import interface_adapter.Search.SearchState;
import interface_adapter.Search.SearchViewModelListener;
import interface_adapter.Search.SearchController;
//import interface_adapter.login.LoginState;
//import interface_adapter.login.LoginViewModel;

//import interface_adapter.login.LoginViewModelListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for when the user is logging into the program.
 */
public class SearchPageView extends JPanel implements ActionListener, SearchViewModelListener {

    private final SearchBarViewModel searchBarViewModel;

    private final JTextField searchInputField = new JTextField(30);
    private final JLabel searchErrorField = new JLabel(" ");


    private final JButton search;
    private final JButton favourites;
    private final JButton watchlist;
    private SearchController searchController = null;
    private FavoriteController favouriteController = null;
    private AddToWatchlistController watchlistController = null;

    public SearchPageView(SearchBarViewModel searchBarViewModel) {

        this.searchBarViewModel = searchBarViewModel;
        this.searchBarViewModel.addListener(this);

        this.setBackground(new Color(240, 249, 250));

        final JLabel title = new JLabel("Flicksy Search");
        title.setFont(new Font("Verdana", Font.BOLD, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel searchInfo = new LabelTextPanel(
                new JLabel("Search"), searchInputField);
        searchInfo.setFont(new Font("Verdana", Font.PLAIN, 16));
        searchInfo.setOpaque(false);
        searchInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 2));
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        searchErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        search.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(search)) {
                            final SearchState currentState = searchBarViewModel.getState();

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


        searchInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SearchState currentState = searchBarViewModel.getState();
                currentState.setTitle(searchInputField.getText());
                searchBarViewModel.setState(currentState);
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

        searchInfo.setMaximumSize(searchInfo.getPreferredSize());
        searchErrorField.setMaximumSize(searchErrorField.getPreferredSize());
        buttons.setMaximumSize(buttons.getPreferredSize());

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createVerticalGlue());

        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 30)));
        this.add(searchInfo);

        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(searchErrorField);
        this.add(buttons);
        this.add(Box.createVerticalGlue());

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
        setFields(searchBarViewModel.getState());
        searchErrorField.setText(searchBarViewModel.getState().getSearchError());
    }

    private void setFields(SearchState state) {
        searchInputField.setText(state.getTitle());
    }

    public void onOpenFavorites(Runnable action) {
        favourites.addActionListener(event -> action.run());
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void setWatchlistController(AddToWatchlistController watchlistController) {
        this.watchlistController = watchlistController;
    }

    public void setFavouriteController(FavoriteController favouriteController) {
        this.favouriteController = favouriteController;
    }
}
