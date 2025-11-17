package view;

import interface_adapter.search.SearchController;
import interface_adapter.search.SearchViewModel;
import interface_adapter.search.SearchState;
import interface_adapter.search.AddMovieController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TODO: Search view (search field, results list, details panel).
 */
public class SearchView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Search View";
    private final SearchViewModel searchViewModel;

    private final JLabel roomId;
    private final JButton shortList;

    private final JTextField searchInputField = new JTextField(15);
    private final JLabel searchErrorField = new JLabel();

    private final JButton searchButton;
    private SearchController searchController;

    private final DefaultListModel<String> movieListModel = new DefaultListModel<>();
    private final JList<String> movieIDs;
    private String selectedMovieID;

    private AddMovieController addMovieController;

    public SearchView(SearchViewModel searchViewModel) {

        this.searchViewModel = searchViewModel;
        this.searchViewModel.addPropertyChangeListener(this);

        // window title
        // final JLabel title = new JLabel("Search Screen");
        // title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // header; room id, button to the shortlist
        final JPanel header = new JPanel();
        roomId = new JLabel("room code");
        header.add(roomId);
        shortList = new JButton("shortlist");
        header.add(shortList);

        // the search bar
        final JPanel searchComponents = new JPanel();
        final LabelTextPanel searchBar = new LabelTextPanel(
                new JLabel("Search"), searchInputField);
        searchComponents.add(searchBar);
        searchButton = new JButton("search");
        searchComponents.add(searchButton);

        // search button action listener
        searchButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource().equals(searchButton)) {
                            final SearchState currentState = searchViewModel.getState();

                            searchController.execute(
                                    currentState.getSearch()
                            );
                        }
                    }
                }
        );

        // the movie list display
        final JPanel searchListPanel = new JPanel();
        searchListPanel.setLayout(new BoxLayout(searchListPanel, BoxLayout.Y_AXIS));

        // add movie button
        final JButton addButton = new JButton("Add");
        searchListPanel.add(addButton);

        movieIDs = new JList<>(movieListModel);
        movieIDs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(movieIDs);
        searchListPanel.add(scrollPane);

        // add button action listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMovieController.execute(selectedMovieID);
                selectedMovieID = null;
            }
        });

        // add list action listener
        movieIDs.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedMovieID = movieIDs.getSelectedValue();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // keeps components vertical

        // this.add(title);
        this.add(header);
        this.add(searchComponents);
        this.add(searchListPanel);
    }
    // TODO: Wire search input and results selection to SearchController

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Trigger search via controller
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO: Update results list/details panel
    }
}
