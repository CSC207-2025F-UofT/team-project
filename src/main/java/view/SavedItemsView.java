package view;

import entities.Outfit;
import interface_adapters.SavedItemsController;
import interface_adapters.SavedItemsViewModel;

import javax.swing.*;
import java.awt.*;

/**
 * Swing UI for viewing saved outfits and favourite locations (as Strings).
 */
public class SavedItemsView extends JPanel {

    public SavedItemsView(SavedItemsController controller,
                          SavedItemsViewModel viewModel) {

        setLayout(new BorderLayout());

        JTextArea output = new JTextArea(15, 50);
        output.setEditable(false);

        JButton refreshBtn = new JButton("Load saved items");
        refreshBtn.addActionListener(e -> {
            controller.loadSavedItems();

            output.setText("");
            if (!viewModel.getError().isEmpty()) {
                output.setText("Error: " + viewModel.getError());
                return;
            }

            output.append(viewModel.getMessage() + "\n\n");

            output.append("Favourite Locations:\n");
            for (String city : viewModel.getFavoriteLocations()) {
                output.append("- " + city + "\n");
            }

            output.append("\nSaved Outfits:\n");
            for (Outfit o : viewModel.getOutfits()) {
                output.append("- " + o.getName() + " | " +
                        o.getWeatherProfile() + " | " +
                        o.getLocation() + " | " +
                        o.getItems() + "\n");
            }
        });

        add(refreshBtn, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }
}
