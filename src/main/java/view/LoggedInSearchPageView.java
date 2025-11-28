package view;

import api.googlePlacesAPI.PlaceFetcher;
import entity.placeSuggestions.PlaceSuggestion;
import interface_adapter.loggedInHomePage.LoggedInHomePageState;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageController;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageState;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageViewModel;
import jdk.jshell.SourceCodeAnalysis;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Timer;

public class LoggedInSearchPageView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Logged In Search View";
    private final LoggedInSearchPageViewModel loggedInSearchPageViewModel;
    LoggedInSearchPageController loggedInSearchPageController = null;

    private final JTextField searchInputField = new JTextField(30);
    private final JPopupMenu suggestionsPopup = new JPopupMenu();


    private final JButton search;
    private final JButton goBack;

    private Timer debounceTimer;


    public LoggedInSearchPageView(LoggedInSearchPageViewModel loggedInSearchViewModel) {
        this.loggedInSearchPageViewModel = loggedInSearchViewModel;
        loggedInSearchPageViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(LoggedInSearchPageViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        //** Build Search bar **//
        final LabelTextPanel searchInput = new LabelTextPanel(
                new JLabel(LoggedInSearchPageViewModel.SEARCH_BAR_LABEL), searchInputField);

        //** Build buttons **//
        final JPanel buttons = new JPanel();
        search = new JButton(LoggedInSearchPageViewModel.SEARCH_BUTTON_LABEL);
        buttons.add(search);
        goBack = new JButton(LoggedInSearchPageViewModel.GO_BACK_LABEL);
        buttons.add(goBack);

        //** Build View **//
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.add(searchInput);
        row.add(buttons);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20)); // spacing under title
        this.add(row);

        goBack.addActionListener(
                e -> loggedInSearchPageController.switchToLoggedInHomePageView()
        );

        search.addActionListener(
                e -> {
                    String query = searchInputField.getText();
                    loggedInSearchPageController.execute(query);
                }
        );

        searchInputField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (debounceTimer == null) {
                    return;
                }
                if (debounceTimer.isRunning()) {
                    debounceTimer.restart();
                } else {
                    debounceTimer.start();
                }
            }
        });


        debounceTimer = new Timer(1000, e -> {
            if (loggedInSearchPageController == null) {
                return;
            }
            String query = searchInputField.getText().trim();
            if (query.isEmpty()) {
                loggedInSearchPageController.clearSuggestions();
                return;
            }

            new Thread(() -> {
                try {
                    loggedInSearchPageController.fetchSuggestions(query);
                } catch (PlaceFetcher.PlaceNotFoundException ex) {
                    loggedInSearchPageController.clearSuggestions();
                }
            }, "Suggestion-Fetcher-Thread").start();

        });
        debounceTimer.setRepeats(false);
    }

    private void showSuggestions(List<PlaceSuggestion> suggestions) {
        suggestionsPopup.setVisible(false);
        suggestionsPopup.removeAll();

        if (suggestions == null || suggestions.isEmpty()) {
            return;
        }

        for (PlaceSuggestion s : suggestions) {
            JMenuItem item = new JMenuItem(s.getMainText() + " " + s.getSecondaryText());
            item.addActionListener(e -> {

                searchInputField.setText(s.getMainText() + ", " + s.getSecondaryText());
                searchInputField.requestFocusInWindow();
                searchInputField.setCaretPosition(searchInputField.getText().length());
                suggestionsPopup.setVisible(false);
            });
            suggestionsPopup.add(item);
        }


        suggestionsPopup.show(searchInputField, 0, searchInputField.getHeight());
    }

    public void setSearchPageController(LoggedInSearchPageController controller) {
        this.loggedInSearchPageController = controller;
    }

    public String getViewName() {return viewName;}

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoggedInSearchPageState state = loggedInSearchPageViewModel.getState();
        List<PlaceSuggestion> suggestions = state.getSuggestions();
        SwingUtilities.invokeLater(() -> {
            showSuggestions(suggestions);
            searchInputField.requestFocusInWindow();
            searchInputField.setCaretPosition(searchInputField.getText().length());
        });
    }
}
