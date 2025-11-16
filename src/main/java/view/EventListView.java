package view;

import javax.swing.*;
import java.awt.*;

/**
 * Main "Lists" screen.
 */
public class EventListView extends JPanel {

    public static final String VIEW_NAME = "event_list";

    // Header
    private final JLabel titleLabel = new JLabel("Lists");
    private final JButton createListButton = new JButton("Create List");

    // Master List row (bigger)
    private final JPanel masterListPanel = new JPanel(new BorderLayout());
    private final JLabel masterListLabel = new JLabel("Master List");

    // Container for user-created lists
    private final JPanel listsContainer = new JPanel();

    public EventListView() {
        setLayout(new BorderLayout()); // A layout that arranges components in NORTH / SOUTH / EAST / WEST / CENTER.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Creates padding around the whole UI

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 32f));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        createListButton.setPreferredSize(new Dimension(140, 40));
        headerPanel.add(createListButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // CENTER PANEL (Master list + user lists)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // MASTER LIST PANEL
        masterListPanel.setBackground(Color.WHITE);
        masterListPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        masterListPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        masterListLabel.setFont(masterListLabel.getFont().deriveFont(Font.BOLD, 18f));
        masterListPanel.add(masterListLabel, BorderLayout.NORTH);

        // Dummy text inside master list
        JPanel masterContent = new JPanel();
        masterContent.setLayout(new BoxLayout(masterContent, BoxLayout.Y_AXIS));
        masterContent.setOpaque(false);

        JLabel dummy1 = new JLabel("• Your saved concerts");
        JLabel dummy2 = new JLabel("• Drake Concert at ScotiaBank Arena");

        masterContent.add(dummy1);
        masterContent.add(Box.createVerticalStrut(5));
        masterContent.add(dummy2);

        masterListPanel.add(masterContent, BorderLayout.SOUTH);

        centerPanel.add(masterListPanel);
        centerPanel.add(Box.createVerticalStrut(40)); // spacing in between Master List and Lists

        // LISTS CONTAINER
        listsContainer.setLayout(new BoxLayout(listsContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(listsContainer);
        scrollPane.setBorder(null);

        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // WIRE "CREATE LIST" BUTTON TO POPUP
        createListButton.addActionListener(e -> openCreateListDialog());
    }

     // Add a row with text & delete button.

    public void addListRow(String listName) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Left: List name
        JLabel nameLabel = new JLabel(listName);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.PLAIN, 16f));

        // Right: Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 40));

        row.add(nameLabel, BorderLayout.WEST);
        row.add(deleteButton, BorderLayout.EAST);

        listsContainer.add(row);
        listsContainer.add(Box.createVerticalStrut(10));

        listsContainer.revalidate();
        listsContainer.repaint();
    }
    // POPUP HANDLER

    private void openCreateListDialog() { // GPT generated code
        // Find the parent Frame (for centering the dialog)
        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
        java.awt.Frame parent = window instanceof java.awt.Frame ? (java.awt.Frame) window : null;

        CreateListDialog dialog = new CreateListDialog(parent);
        dialog.setVisible(true);

        String newName = dialog.getListName();
        if (newName != null && !newName.isBlank()) {
            // TEMP: directly add row to view for now ,should call use_case afterwawrds
            addListRow(newName);
        }
    }

    // TEST MAIN

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test EventListView");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            EventListView view = new EventListView();

            frame.add(view);
            frame.setVisible(true);
        });
    }
}