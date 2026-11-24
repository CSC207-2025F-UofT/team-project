package view;

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

    public SearchPageView(SearchBarViewModel searchBarViewModel) {

        this.searchBarViewModel = searchBarViewModel;
        this.searchBarViewModel.addListener(this);

        this.setBackground(new Color(240, 249, 250));

        final JLabel title = new JLabel("Flicksy Search");
        title.setFont(new Font("SansSerif", Font.BOLD, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel searchInfo = new LabelTextPanel(
                new JLabel("Search"), searchInputField);
        searchInfo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchInfo.setOpaque(false);


        final JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        search = new JButton("Search");
        search.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buttons.add(search);

        favourites = new JButton("Favourites");
        favourites.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buttons.add(favourites);

        watchlist = new JButton("Watchlist");
        watchlist.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buttons.add(watchlist);

        search.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(search)) {
                            final SearchState currentState = searchBarViewModel.getState();

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


        this.add(title);
        this.add(searchInfo);
        this.add(searchErrorField);
        this.add(buttons);
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

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }
}
