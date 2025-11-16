package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewEventListView extends JPanel {

    public static final String VIEW_NAME = "view_event_list";

    // Header: shows which list we're looking at, e.g. "List: My Favourites"
    private final JLabel listTitleLabel = new JLabel("List:");

    // Center: list of event names
    private final DefaultListModel<String> eventListModel = new DefaultListModel<>();
    private final JList<String> eventList = new JList<>(eventListModel);

    // Bottom: back button to go back to EventListView
    private final JButton backButton = new JButton("Back");

    public ViewEventListView() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(listTitleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Events
        JScrollPane scrollPane = new JScrollPane(eventList);
        add(scrollPane, BorderLayout.CENTER);

        // Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Methods for interface adapter
    public String getViewName() {
        return VIEW_NAME;
    }

    public JButton getBackButton() {
        return backButton;
    }

    // Text for title
    public void setListName(String listName) {
        listTitleLabel.setText("List: " + listName);
    }

    // View events in the Created List
    public void setEventNames(List<String> names) {
        eventListModel.clear();
        eventListModel.addAll(names);
    }
}