package view;

import interface_adapter.explore.ExploreState;
import interface_adapter.explore.ExploreViewModel;
import interface_adapter.explore.ExploreViewModelListener;
import interface_adapter.explore.MoveController;
import interface_adapter.explore.PickUpItemController;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * The Explore screen (Use Case 2): shows the current room, its items and
 * exits, the player's inventory, and buttons for moving between rooms.
 *
 * <p>The view has two jobs: (1) when the player clicks something, call a
 * controller; (2) when the view model changes, redraw. It contains no game
 * rules. For example, it never decides whether a move is allowed.</p>
 *
 * <p>Layout:</p>
 * <pre>
 * +------------------------------------+--------------+
 * | Floor 1 - Armory                   | Inventory    |
 * | room description...                | (1/3)        |
 * | [Items in this room]               | - Potion     |
 * | [Examine item]                     |              |
 * +------------------------------------+--------------+
 * |            message to the player                  |
 * |                 [North]                           |
 * |          [West]  You  [East]                      |
 * |                 [South]                           |
 * +---------------------------------------------------+
 * </pre>
 */
public class ExploreView extends JPanel implements ExploreViewModelListener {
    /**
     * The four direction names, in the order N, W, E, S.
     * (Not used anywhere at the moment, so it could be removed.)
     */
    private static final String[] DIRECTIONS = {"NORTH", "WEST", "EAST", "SOUTH"};

    /** Where this view gets its data from. */
    private final ExploreViewModel viewModel;
    /** Called when a direction button is clicked. Set by the app builder. */
    private MoveController moveController;
    /** Called when the player saves an item. Set by the app builder. */
    private PickUpItemController pickUpItemController;

    // ----- Swing components -----
    private final JLabel titleLabel = new JLabel();
    private final JTextArea descriptionArea = new JTextArea(5, 40);
    /** The data behind the room items list; changing it updates the JList. */
    private final DefaultListModel<String> roomItemsModel = new DefaultListModel<>();
    private final JList<String> roomItemsList = new JList<>(roomItemsModel);
    /** The data behind the inventory list. */
    private final DefaultListModel<String> inventoryModel = new DefaultListModel<>();
    private final JList<String> inventoryList = new JList<>(inventoryModel);
    private final JLabel inventoryLabel = new JLabel("Inventory");
    /** One line of feedback, e.g. "You enter the Armory." (a space keeps its height when empty). */
    private final JLabel messageLabel = new JLabel(" ");
    private final JButton examineButton = new JButton("Examine item");
    private final JButton northButton = new JButton("North");
    private final JButton southButton = new JButton("South");
    private final JButton eastButton = new JButton("East");
    private final JButton westButton = new JButton("West");

    /**
     * Builds the screen and starts listening to the view model.
     *
     * @param viewModel the Explore view model
     */
    public ExploreView(ExploreViewModel viewModel) {
        this.viewModel = viewModel;
        // Observer pattern: stateChanged() and inventoryFull() below run whenever
        // a presenter notifies the view model.
        viewModel.addListener(this);

        // 10-pixel gaps between panels and a 12-pixel margin around the screen.
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildRoomPanel(), BorderLayout.CENTER);
        add(buildInventoryPanel(), BorderLayout.EAST);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        wireActions();
    }

    /**
     * Gives the view its Move controller (called by the app builder).
     *
     * @param moveController the controller
     */
    public void setMoveController(MoveController moveController) {
        this.moveController = moveController;
    }

    /**
     * Gives the view its Pick Up Item controller (called by the app builder).
     *
     * @param pickUpItemController the controller
     */
    public void setPickUpItemController(PickUpItemController pickUpItemController) {
        this.pickUpItemController = pickUpItemController;
    }

    /** @return this screen's name in the CardLayout */
    public String getViewName() {
        return viewModel.getViewName();
    }

    // =========================================================================
    // Layout: building the panels
    // =========================================================================

    /**
     * Builds the left side: room title, description and items.
     *
     * @return the panel
     */
    private JPanel buildRoomPanel() {
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));

        // A read-only text area that wraps long descriptions at word boundaries.
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(descriptionArea.getFont().deriveFont(14f));
        // Match the panel's background so it looks like text, not an input box.
        descriptionArea.setBackground(getBackground());

        // Only one item can be selected at a time.
        roomItemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane itemsScroll = new JScrollPane(roomItemsList);
        itemsScroll.setBorder(BorderFactory.createTitledBorder("Items in this room (double-click to examine)"));
        itemsScroll.setPreferredSize(new Dimension(380, 140));

        JPanel itemsPanel = new JPanel(new BorderLayout(5, 5));
        itemsPanel.add(itemsScroll, BorderLayout.CENTER);
        itemsPanel.add(examineButton, BorderLayout.SOUTH);

        // Stack title, description and items vertically.
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // BoxLayout needs every child aligned the same way, or they shift around.
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        descScroll.setAlignmentX(LEFT_ALIGNMENT);
        itemsPanel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(descScroll);
        panel.add(itemsPanel);
        return panel;
    }

    /**
     * Builds the right side: the inventory list and its "(n/max)" label.
     *
     * @return the panel
     */
    private JPanel buildInventoryPanel() {
        inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(inventoryList);
        scroll.setPreferredSize(new Dimension(200, 200));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        inventoryLabel.setFont(inventoryLabel.getFont().deriveFont(Font.BOLD));
        panel.add(inventoryLabel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Builds the bottom: the message line and the compass of direction buttons.
     *
     * @return the panel
     */
    private JPanel buildBottomPanel() {
        // A 3x3 grid shaped like a compass. Empty JLabels fill the corners:
        //   [ ]   [N]   [ ]
        //   [W]  [You]  [E]
        //   [ ]   [S]   [ ]
        JPanel compass = new JPanel(new GridLayout(3, 3, 4, 4));
        compass.add(new JLabel());
        compass.add(northButton);
        compass.add(new JLabel());
        compass.add(westButton);
        JLabel you = new JLabel("You", SwingConstants.CENTER);
        compass.add(you);
        compass.add(eastButton);
        compass.add(new JLabel());
        compass.add(southButton);
        compass.add(new JLabel());

        // Wrapping in a plain JPanel keeps the compass small and centred
        // instead of stretching across the whole window.
        JPanel compassWrapper = new JPanel();
        compassWrapper.add(compass);

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(messageLabel, BorderLayout.NORTH);
        panel.add(compassWrapper, BorderLayout.CENTER);
        return panel;
    }

    /** Connects buttons and mouse clicks to the controllers. */
    private void wireActions() {
        // Step 1 of the use case: the player chooses a direction.
        northButton.addActionListener(e -> moveController.execute("NORTH"));
        southButton.addActionListener(e -> moveController.execute("SOUTH"));
        eastButton.addActionListener(e -> moveController.execute("EAST"));
        westButton.addActionListener(e -> moveController.execute("WEST"));

        // Step 5: the player chooses an item, by button or by double-clicking it.
        examineButton.addActionListener(e -> examineSelectedItem());
        roomItemsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    examineSelectedItem();
                }
            }
        });
    }

    // =========================================================================
    // User actions
    // =========================================================================

    /**
     * Step 6: show the item's description and let the player save it or drop it.
     */
    private void examineSelectedItem() {
        int index = roomItemsList.getSelectedIndex();
        ExploreState state = viewModel.getState();
        // getSelectedIndex() returns -1 when nothing is selected.
        if (index < 0 || index >= state.getRoomItemNames().size()) {
            JOptionPane.showMessageDialog(this, "Select an item in the room first.");
            return;
        }
        // Names and details are stored in the same order, so the same index works for both.
        String itemName = state.getRoomItemNames().get(index);
        String details = state.getRoomItemDetails().get(index);

        // A dialog with two custom buttons. It returns 0 for the first button,
        // 1 for the second, or -1 if the dialog was closed.
        Object[] options = {"Save to inventory", "Drop it"};
        int choice = JOptionPane.showOptionDialog(this, details, itemName,
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            // Step 7: ask the use case to move the item into the inventory.
            pickUpItemController.execute(itemName);
        }
        // Step 9: "Drop it" (or closing the dialog): nothing happens.
    }

    /**
     * Extension 7a: the inventory is full. Offer to drop one of the carried items.
     *
     * @param state the current state (holds the wanted item and the inventory)
     */
    private void askWhatToDiscard(ExploreState state) {
        String pending = state.getPendingItemName();
        List<String> carried = state.getInventoryItemNames();
        if (pending == null || carried.isEmpty()) {
            return;
        }
        // A dialog with a drop-down of the carried items. Returns null on Cancel.
        Object choice = JOptionPane.showInputDialog(this,
                "Your inventory is full.\nDrop one of your items to take the " + pending
                        + ", or cancel to leave it here.",
                "Inventory full", JOptionPane.WARNING_MESSAGE, null,
                carried.toArray(), carried.get(0));
        if (choice != null) {
            // Try again, this time with an item to drop.
            pickUpItemController.execute(pending, choice.toString());
        }
        // On Cancel the item simply stays in the room.
    }

    // =========================================================================
    // Updates from the view model
    // =========================================================================

    /**
     * Called automatically when a presenter calls notifyStateChanged():
     * redraw everything.
     */
    @Override
    public void stateChanged() {
        setFields(viewModel.getState());
    }

    /**
     * Called automatically when a presenter calls notifyInventoryFull():
     * open the "what do you want to drop?" dialog (extension 7a).
     */
    @Override
    public void inventoryFull() {
        askWhatToDiscard(viewModel.getState());
    }

    /**
     * Copies the state into the Swing components.
     *
     * @param state the state to display
     */
    private void setFields(ExploreState state) {
        titleLabel.setText("Floor " + state.getFloorNumber() + "  -  " + state.getRoomName());
        descriptionArea.setText(state.getRoomDescription());
        // Scroll back to the top of the description.
        descriptionArea.setCaretPosition(0);

        // Refill the room items list.
        roomItemsModel.clear();
        for (String name : state.getRoomItemNames()) {
            roomItemsModel.addElement(name);
        }
        if (roomItemsModel.isEmpty()) {
            // Show a greyed-out placeholder so the player knows the room is empty.
            roomItemsModel.addElement("(nothing here)");
            roomItemsList.setEnabled(false);
        }
        else {
            roomItemsList.setEnabled(true);
        }
        examineButton.setEnabled(!state.getRoomItemNames().isEmpty());

        // Refill the inventory list and its "(n/max)" counter.
        inventoryModel.clear();
        for (String name : state.getInventoryItemNames()) {
            inventoryModel.addElement(name);
        }
        inventoryLabel.setText("Inventory (" + state.getInventoryItemNames().size()
                + "/" + state.getInventoryCapacity() + ")");

        // Only enable buttons for directions that have a door.
        List<String> exits = state.getExits();
        northButton.setEnabled(exits.contains("NORTH"));
        southButton.setEnabled(exits.contains("SOUTH"));
        eastButton.setEnabled(exits.contains("EAST"));
        westButton.setEnabled(exits.contains("WEST"));

        // Show the message in dark red for warnings and dark green otherwise.
        messageLabel.setText(state.getMessage().isEmpty() ? " " : state.getMessage());
        messageLabel.setForeground(state.isError() ? new Color(170, 30, 30) : new Color(30, 110, 40));
    }
}
