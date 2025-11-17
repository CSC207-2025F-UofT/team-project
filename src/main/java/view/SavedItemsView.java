package view;

import entities.Outfit;
import interface_adapters.EditFavoriteLocationController;
import interface_adapters.SavedItemsController;
import interface_adapters.SavedItemsViewModel;

import javax.swing.*;
import java.awt.*;

/**
 * Swing UI for viewing and editing saved outfits and favourite locations.
 */
public class SavedItemsView extends JPanel {

    public SavedItemsView(SavedItemsController controller,
                          EditFavoriteLocationController editController,
                          SavedItemsViewModel viewModel) {

        setLayout(new BorderLayout());

        // Left: list of favourite locations + rename button
        DefaultListModel<String> favoritesModel = new DefaultListModel<>();
        JList<String> favoritesList = new JList<>(favoritesModel);
        JScrollPane favoritesScroll = new JScrollPane(favoritesList);

        JButton renameBtn = new JButton("Rename selected city");
        renameBtn.addActionListener(e -> {
            String selected = favoritesList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a city to rename.",
                        "No selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newName = JOptionPane.showInputDialog(
                    this,
                    "Enter new name for " + selected + ":",
                    selected
            );

            if (newName == null) {
                // user cancelled
                return;
            }

            editController.rename(selected, newName);

            if (!viewModel.getError().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        viewModel.getError(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // refresh favourites list from view model
            favoritesModel.clear();
            for (String city : viewModel.getFavoriteLocations()) {
                favoritesModel.addElement(city);
            }

            JOptionPane.showMessageDialog(this,
                    viewModel.getMessage(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel favoritesPanel = new JPanel(new BorderLayout());
        favoritesPanel.add(new JLabel("Favourite Locations:"), BorderLayout.NORTH);
        favoritesPanel.add(favoritesScroll, BorderLayout.CENTER);
        favoritesPanel.add(renameBtn, BorderLayout.SOUTH);

        // Right: text area for outfits + messages
        JTextArea output = new JTextArea(15, 50);
        output.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(output);

        // Top: refresh button
        JButton refreshBtn = new JButton("Load saved items");
        refreshBtn.addActionListener(e -> {
            controller.loadSavedItems();

            output.setText("");
            if (!viewModel.getError().isEmpty()) {
                output.setText("Error: " + viewModel.getError());
                return;
            }

            // update favourites list
            favoritesModel.clear();
            for (String city : viewModel.getFavoriteLocations()) {
                favoritesModel.addElement(city);
            }

            output.append(viewModel.getMessage() + "\n\n");

            output.append("Saved Outfits:\n");
            for (Outfit o : viewModel.getOutfits()) {
                output.append("- " + o.getName() + " | " +
                        o.getWeatherProfile() + " | " +
                        o.getLocation() + " | " +
                        o.getItems() + "\n");
            }
        });

        add(refreshBtn, BorderLayout.NORTH);
        add(favoritesPanel, BorderLayout.WEST);
        add(outputScroll, BorderLayout.CENTER);
    }
}
