package view;

import javax.swing.*;
import java.awt.*;

public class EventListView extends JPanel {

    public static final String VIEW_NAME = "event_list";

    // Header
    private final JLabel titleLabel = new JLabel("Lists");
    private final JButton createListButton = new JButton("Create List");

    // Master list
    private final JPanel masterListPanel = new JPanel();
    private final JLabel masterListLabel = new JLabel("Master List");

    // User-created lists
    private final JPanel listsContainer = new JPanel();

    public EventListView() {

        // BorderLayout used
        setLayout(new BorderLayout());

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(createListButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // CENTRAL AREA
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Master list section
        masterListPanel.setLayout(new BorderLayout());
        masterListPanel.add(masterListLabel, BorderLayout.NORTH);
        centerPanel.add(masterListPanel);

        // Lists container
        listsContainer.setLayout(new BoxLayout(listsContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listsContainer);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

    }

    public JButton addListRow(String listName) {
        JPanel row = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(listName);
        JButton deleteButton = new JButton("Delete");

        row.add(nameLabel, BorderLayout.WEST);
        row.add(deleteButton, BorderLayout.EAST);

        listsContainer.add(row);
        // Controller will take care of the actions
        return deleteButton;
    }

    // Getters for controllers in the future
    public JButton getCreateListButton() {
        return createListButton;
    }

    public JPanel getListsContainer() {
        return listsContainer;
    }

    public JPanel getMasterListPanel() {
        return masterListPanel;
    }




    // Preview for testing, to be deleted
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test EventListView");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);

            EventListView view = new EventListView();

            // Examplar Lists
            view.addListRow("My Favourites");

            frame.add(view);
            frame.setVisible(true);
        });
    }
}