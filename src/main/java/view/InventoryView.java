package view;

// imports
import entity.User;
import interface_adapter.InventoryUseItem.InventoryUseItemController;
import interface_adapter.InventoryUseItem.InventoryUseItemViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class InventoryView extends JPanel implements PropertyChangeListener {
    private final String viewName = "UseItem";
    private final InventoryUseItemViewModel viewModel;
    private InventoryUseItemController controller;
    // ui
    private final JComboBox<String> inventoryDropdown = new JComboBox<>();
    private final JButton useItemButton = new JButton("Use Item");

    public InventoryView(InventoryUseItemViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        // layout
        setLayout(new BorderLayout(5,5));
        setBorder(BorderFactory.createTitledBorder("Inventory"));
        // use item button not normally available
        useItemButton.setEnabled(false);

        // actual inventory dropdown
        inventoryDropdown.addActionListener(e -> {
            String selectedItemName = (String) inventoryDropdown.getSelectedItem();
            useItemButton.setEnabled(selectedItemName != null); });

        // use item button action listener
        useItemButton.addActionListener(e -> {
            String selectedItemName = (String) inventoryDropdown.getSelectedItem();
            if (selectedItemName != null && controller != null) {
                controller.useItem(selectedItemName);
                updateDropdown();
                useItemButton.setEnabled(false); } }); }

    public String getViewName() {
        return viewName;
    }

    // set controller
    public void setController(InventoryUseItemController controller) { this.controller = controller; }

    //update dropdown with most recent inventory
    private void updateDropdown() {
        inventoryDropdown.removeAllItems();
        User user = viewModel.getState().getUser();
        if (user != null) {
            for (String item: user.getInventory().getItemsList()) {
                inventoryDropdown.addItem(item); } } }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("inventory".equals(evt.getPropertyName()) || "state".equals(evt.getPropertyName())) {
            updateDropdown();
        }
    }
}
