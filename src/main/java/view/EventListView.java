package view;

import javax.swing.*;

// A simple skeleton for the "Create Event List" view. To be implemented

public class EventListView {

    // View name
    private final String viewName = "Event List Panel";

    //Title / message label
    private final JLabel messageLabel = new JLabel("Please create a unique list name:");

    // Input field
    private final JTextField listNameField = new JTextField("Enter list name");

    // Buttons
    private final JButton confirmButton = new JButton("Confirm");
    private final JButton viewListsButton = new JButton("View Existing Lists");
    private final JButton returnButton = new JButton("Return to Home");

    // Getter for the view name
    public String getViewName() {
        return viewName;
    }
}