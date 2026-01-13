package view;

// imports
import entity.Item;
import interface_adapter.InventoryAddItem.InventoryAddItem_ViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ItemView extends JPanel implements PropertyChangeListener {
    private final InventoryAddItem_ViewModel viewModel;
    private final JLabel itemNameLabel;
    private final JTextArea itemDescription;


    public ItemView(InventoryAddItem_ViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Item Name
        itemNameLabel = new JLabel("Item Name", SwingConstants.CENTER);
        itemNameLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Item Description
        itemDescription = new JTextArea(5,20);
        itemDescription.setEditable(false);
        itemDescription.setLineWrap(true);
        itemDescription.setWrapStyleWord(true);

        add(itemNameLabel, BorderLayout.NORTH);
        add(itemDescription, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            Item item = viewModel.getState().getAddedItem();
            if (item != null) {
                itemNameLabel.setText(item.getName());
                itemDescription.setText(item.getDescription());
            }
        }
    }

}
