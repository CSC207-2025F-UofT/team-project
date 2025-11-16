package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import interface_adapter.user_search.SearchUserController;
import interface_adapter.user_search.SearchUserViewModel;

public class SearchUserView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "new chat";

    // Final fields must be initialized in the constructor.
    private final ViewManagerModel viewManagerModel;
    private final SearchUserViewModel searchUserViewModel;

    private SearchUserController searchUserController;

    // UI Components
    private final JTextField searchInputField;
    private final JButton searchExitButton;
    private final JButton startChatButton;
    private final JPanel userListPanel; // Container for dynamic checkboxes/users

    public SearchUserView(ViewManagerModel viewManagerModel, SearchUserViewModel searchUserViewModel) {

        // --- FIX: Initialize final fields immediately ---
        this.viewManagerModel = viewManagerModel;
        this.searchUserViewModel = searchUserViewModel;
        // -----------------------------------------------

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

        JLabel searchLabel = new JLabel("Search for User: ");
        searchInputField = new JTextField(20);

        // Exit/Cancel Button
        searchExitButton = new JButton("X"); // Using Unicode X for the button
        searchExitButton.setFont(new Font("Oxygen", Font.BOLD, 16));
        searchExitButton.setFocusPainted(false);
        searchExitButton.setBorderPainted(false);
        searchExitButton.setContentAreaFilled(false);

        searchPanel.add(searchLabel);
        searchPanel.add(searchInputField);
        searchPanel.add(Box.createHorizontalStrut(5));
        searchPanel.add(searchExitButton);

        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPanel.getPreferredSize().height));


        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        // Placeholder Users
        addPlaceholderUsers(new String[]{"User1", "User2", "User3", "User4", "User5"}); // Temporary list of users

        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        startChatButton = new JButton("Start Chat");
        buttonPanel.add(startChatButton);

        // Listeners
        startChatButton.addActionListener(this);
        searchExitButton.addActionListener(this);

        // Add listener to the search field for real-time searching
        // searchInputField.addActionListener(e -> {
        //     if (searchUserController != null) {
        //         searchUserController.execute(searchInputField.getText());
        //     }
        // });


        // Assembly
        this.add(searchPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(scrollPane);
        this.add(buttonPanel);
    }

    /**
     * Helper to create placeholder users with checkboxes.
     */
    private void addPlaceholderUsers(String[] users) {
        for (String user : users) {
            JCheckBox checkBox = new JCheckBox(user);
            checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            userListPanel.add(checkBox);
        }
    }


    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(searchExitButton)) {
            // "Exit button to return to the home screen" (which is LoggedInView)
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        } else if (evt.getSource().equals(startChatButton)) {
            // Navigate to the ChatView
            viewManagerModel.setState("chat");
            viewManagerModel.firePropertyChange();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Will be used to update the user list based on search results
        // You'll likely need to use searchUserViewModel.getState().getUsers() here
    }

    public String getViewName() {
        return viewName;
    }

    public void setUserSearchController(SearchUserController searchUserController) {
        this.searchUserController = searchUserController;
        // You would typically attach listeners to buttons/fields here
        // If you want search on Enter key press:
        searchInputField.addActionListener(e -> {
            if (this.searchUserController != null) {
                this.searchUserController.execute(searchInputField.getText());
            }
        });
    }
}