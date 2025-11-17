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
import interface_adapter.user_search.SearchUserState;
import java.util.List;

public class SearchUserView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "new chat";

    private final ViewManagerModel viewManagerModel;
    private final SearchUserViewModel searchUserViewModel;
    private final ChatView chatView;

    private SearchUserController searchUserController;

    // UI Components
    private final JTextField searchInputField;
    private final JButton searchExitButton;
    private final JButton startChatButton;

    // JList and its model for displaying users
    private final JList<String> userList;
    private final DefaultListModel<String> userListModel;


    public SearchUserView(ViewManagerModel viewManagerModel, SearchUserViewModel searchUserViewModel, ChatView chatView) { // <-- MODIFIED CONSTRUCTOR

        this.viewManagerModel = viewManagerModel;
        this.searchUserViewModel = searchUserViewModel;
        this.chatView = chatView;
        this.searchUserViewModel.addPropertyChangeListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

        JLabel searchLabel = new JLabel("Search for User: ");
        searchInputField = new JTextField(20);

        // Exit/Cancel Button
        searchExitButton = new JButton("X");
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


        // User List Components (New JList logic)
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Single selection for a one-on-one chat

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        startChatButton = new JButton("Start Chat");
        buttonPanel.add(startChatButton);

        // Listeners
        startChatButton.addActionListener(this);
        searchExitButton.addActionListener(this);

        // Assembly
        this.add(searchPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(scrollPane);
        this.add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(searchExitButton)) {
            // "Exit button to return to the home screen"
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        } else if (evt.getSource().equals(startChatButton)) {
            String selectedUser = userList.getSelectedValue();

            if (selectedUser == null || selectedUser.startsWith("Error:")) {
                JOptionPane.showMessageDialog(this, "Please select a valid user to start a chat.");
                return;
            }

            // Set the chat partner in the ChatView before navigating
            if (chatView != null) {
                chatView.setChatPartner(selectedUser);
            }

            // Navigate to the ChatView
            viewManagerModel.setState("chat");
            viewManagerModel.firePropertyChange();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            SearchUserState state = (SearchUserState) evt.getNewValue();

            userListModel.clear(); // Clear the list before updating

            if (state.getSearchError() != null) {
                // Display error message
                userListModel.addElement("Error: " + state.getSearchError());
            } else if (state.getSearchResults() != null) {
                // Update the JList with search results
                for (String username : state.getSearchResults()) {
                    userListModel.addElement(username);
                }
                if (state.getSearchResults().isEmpty()) {
                    userListModel.addElement("No users found.");
                }
            }
        }
    }


    public String getViewName() {
        return viewName;
    }

    public void setUserSearchController(SearchUserController searchUserController) {
        this.searchUserController = searchUserController;
        searchInputField.addActionListener(e -> {
            if (this.searchUserController != null) {
                // Trigger search when user presses Enter in the search field
                this.searchUserController.execute(searchInputField.getText());
            }
        });

        // Trigger an initial search with an empty query to load all users on startup
        if (this.searchUserController != null) {
            this.searchUserController.execute("");
        }
    }
}