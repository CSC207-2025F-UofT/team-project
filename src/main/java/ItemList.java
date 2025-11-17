import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class ItemList extends JPanel {
    public ArrayList<Item> list;
    private JPanel itemDisplayPanel;
    private JTextField inputTextField;

    public ItemList(){
        this.list = new ArrayList<Item>();

        // Set the layout to BorderLayout to organize components
        this.setLayout(new BorderLayout());

        // Create the top panel to display items vertically
        itemDisplayPanel = new JPanel();
        itemDisplayPanel.setLayout(new BoxLayout(itemDisplayPanel, BoxLayout.Y_AXIS));
        itemDisplayPanel.setBackground(Color.WHITE);

        // Create a scroll pane for the item display
        JScrollPane scrollPane = new JScrollPane(itemDisplayPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(scrollPane, BorderLayout.CENTER);

        // Create the bottom panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel label = new JLabel("Add Item:");
        inputTextField = new JTextField();
        inputTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addItemFromInput();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        inputPanel.add(label, BorderLayout.WEST);
        inputPanel.add(inputTextField, BorderLayout.CENTER);

        this.add(inputPanel, BorderLayout.SOUTH);
    }

    public void addItem(Item item){
        this.list.add(item);
        updateDisplay();
    }

    public void removeItem(Item item){
        // remove item by searching for it
        this.list.remove(item);
        updateDisplay();
    }

    /**
     * Adds an item from the text field input
     */
    private void addItemFromInput() {
        String itemName = inputTextField.getText().trim();

        if (!itemName.isEmpty()) {
            // Create new item and add to model
            Item newItem = new Item(itemName);
            this.list.add(newItem);

            // Update the display
            updateDisplay();

            // Clear the input field
            inputTextField.setText("");
            inputTextField.requestFocus();
        }
    }

    /**
     * Updates the display panel to show all items
     */
    private void updateDisplay() {
        itemDisplayPanel.removeAll();

        for (Item item : this.list) {
            JLabel itemLabel = new JLabel(item.values[0]);
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            itemLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemDisplayPanel.add(itemLabel);
        }

        itemDisplayPanel.revalidate();
        itemDisplayPanel.repaint();
    }
}
