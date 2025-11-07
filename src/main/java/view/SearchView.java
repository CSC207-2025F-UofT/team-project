package view;

import interface_adapter.search.SearchController;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is browsing map and search for location.
 */
public class SearchView extends JPanel implements ActionListener, PropertyChangeListener {

    /* Name of the card*/
    private final String viewName;
    /* Search Panel for textField and buton*/
    final JPanel searchLocationPanel = new JPanel(new BorderLayout());
    /* search input JTextField */
    private final JTextField searchInputField = new JTextField(15);
    /* The search button */
    private final JButton search =  new JButton("Search");
    /* JLabel of search error */
    private final JLabel searchErrorField = new JLabel();
    /* Controller of search view */
    private transient SearchController searchController = null;
    /* initialize a default mapPanel view */
    private final MapPanel mapPanel = new MapPanel();

    /**
     * Construct the SearchView JPanel from its SearchViewModel (contain states of the search view)
     */
    public SearchView(SearchViewModel searchViewModel) {
        /* Get view name from Model */
        this.viewName = searchViewModel.getViewName();
        /* Add implemented evt logic to ViewModel so it react to fires */
        searchViewModel.addPropertyChangeListener(this);

        /* Add listener to search button */
        search.addActionListener(
                evt -> {
                    if (evt.getSource().equals(search)) {
                        final SearchState currentState = searchViewModel.getState();

                        try {
                            searchController.execute(
                                    currentState.getLocationName()
                            );
                        } catch (Exception e) {
                            // Todo throw a search error
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        /* Change LocationName in search state when typing to search field */
        searchInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SearchState currentState = searchViewModel.getState();
                currentState.setLocationName(searchInputField.getText());
                searchViewModel.setState(currentState);
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

        /* Deal with UI appearance */
        searchLocationPanel.add(searchInputField, BorderLayout.CENTER);
        searchLocationPanel.add(search, BorderLayout.EAST);
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 上10, 左右20, 下10像素边距
        searchContainer.add(searchLocationPanel, BorderLayout.CENTER);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(searchContainer);
        this.add(searchErrorField);
        this.add(mapPanel);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SearchState state = (SearchState) evt.getNewValue();
        setFields(state);
        searchErrorField.setText(state.getSearchError());
        mapPanel.setCenter(state.getLatitude(), state.getLongitude());
    }

    private void setFields(SearchState state) {
        searchInputField.setText(state.getLocationName());
    }

    public String getViewName() {
        return viewName;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

}
