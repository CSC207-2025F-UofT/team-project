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
 * The View for when the user is logging into the program.
 */
public class SearchView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName;

    private final JTextField searchInputField = new JTextField(15);
    private final JLabel searchErrorField = new JLabel();

    private final JButton search =  new JButton("Search");
    private SearchController searchController;

    private final MapPanel mapPanel = new MapPanel();

    public SearchView(SearchViewModel searchViewModel) {

        this.viewName = searchViewModel.getViewName();
        searchViewModel.addPropertyChangeListener(this);

        final JPanel searchLocationPanel = new JPanel(new BorderLayout());
        searchLocationPanel.add(searchInputField, BorderLayout.CENTER);
        searchLocationPanel.add(search, BorderLayout.EAST);
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 上10, 左右20, 下10像素边距
        searchContainer.add(searchLocationPanel, BorderLayout.CENTER);

        search.addActionListener(
                evt -> {
                    if (evt.getSource().equals(search)) {
                        final SearchState currentState = searchViewModel.getState();

                        try {
                            searchController.execute(
                                    currentState.getLocationName()
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

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
