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

    private final JTextField searchInputField = new JTextField(25);
    private final JLabel searchErrorField = new JLabel(" ");


    private final JButton search;
    private SearchController searchController = null;

    public SearchPageView(SearchBarViewModel searchBarViewModel) {

        this.searchBarViewModel = searchBarViewModel;
        this.searchBarViewModel.addListener(this);

        this.setBackground(new Color(240, 249, 250));

        final JLabel title = new JLabel("Flicksy Search");
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel searchInfo = new LabelTextPanel(
                new JLabel("Search"), searchInputField);


        final JPanel buttons = new JPanel();
        search = new JButton("Search");
        search.setFont(new Font("Serif", Font.PLAIN, 16));
        buttons.add(search);

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

    public void setLoginController(SearchController loginController) {
        this.searchController = loginController;
    }
}
