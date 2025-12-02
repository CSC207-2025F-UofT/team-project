package view;

// imports
import entity.Item;

import interface_adapter.InventoryAddItem.InventoryAddItemViewModel;
import interface_adapter.InventoryAddItem.InventoryAddItemController;

import javax.swing.*;
import java.awt.*;



public class ItemView extends JPanel {
    private final String viewName = "AddItem";
    private final InventoryAddItemViewModel viewModel;
    private final JLabel itemNameLabel;
    private final JTextArea itemDescription;
    private InventoryAddItemController controller;
    private final JButton okbutton = new JButton("Ok");

    public ItemView(InventoryAddItemViewModel viewModel) {
        this.viewModel = viewModel;
        // borders
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Item Name
        itemNameLabel = new JLabel("Item Name", SwingConstants.CENTER);
        itemNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // Item Description
        itemDescription = new JTextArea(5, 20);
        itemDescription.setEditable(false);
        itemDescription.setLineWrap(true);
        itemDescription.setWrapStyleWord(true);
        // add components
        add(itemNameLabel, BorderLayout.NORTH);
        add(itemDescription, BorderLayout.CENTER);
        add(okbutton, BorderLayout.SOUTH);

        // add action listener for button
        okbutton.addActionListener(e-> {
            if (controller == null) {
                JOptionPane.showMessageDialog(this, "Controller is not set.");
                return; }
            Item item = viewModel.getState().getAddedItem();
            if (item != null) {
                controller.addItem(viewModel.getState().getUser(), item);
                JOptionPane.showMessageDialog(this, "Item added.");
            }
        });
    }

    public String getViewName() {
        return viewName;
    }

    // set controller
    public void setController(InventoryAddItemController controller) {
        this.controller = controller;
    }
}