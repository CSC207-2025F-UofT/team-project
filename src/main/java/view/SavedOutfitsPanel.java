package view;

import entity.Outfit;
import interface_adapter.save_outfit.SaveOutfitController;
import interface_adapter.save_outfit.SaveOutfitViewModel;

import javax.swing.*;
import java.awt.*;

public class SaveOutfitView extends JPanel {

    private final JTextField outfitNameField = new JTextField(15);
    private final JTextField itemsField = new JTextField(30);
    private final JTextField weatherProfileField = new JTextField(20);
    private final JTextField locationField = new JTextField(20);
    private final JCheckBox overwriteCheckBox = new JCheckBox("Overwrite if duplicate");
    private final JLabel messageLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();
    private final JTextArea savedOutfitsArea = new JTextArea(10, 40);

    private final SaveOutfitController controller;
    private final SaveOutfitViewModel viewModel;

    public SaveOutfitView(SaveOutfitController controller, SaveOutfitViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Outfit name:"));
        formPanel.add(outfitNameField);

        formPanel.add(new JLabel("Items (comma-separated):"));
        formPanel.add(itemsField);

        formPanel.add(new JLabel("Weather profile:"));
        formPanel.add(weatherProfileField);

        formPanel.add(new JLabel("Location (optional):"));
        formPanel.add(locationField);

        formPanel.add(new JLabel(""));
        formPanel.add(overwriteCheckBox);

        JButton saveButton = new JButton("Save Outfit");
        saveButton.addActionListener(e -> {
            controller.saveOutfit(
                    outfitNameField.getText(),
                    itemsField.getText(),
                    weatherProfileField.getText(),
                    locationField.getText(),
                    overwriteCheckBox.isSelected()
            );
            refreshFromViewModel();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(saveButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel(new GridLayout(2, 1));
        errorLabel.setForeground(Color.RED);
        messagePanel.add(messageLabel);
        messagePanel.add(errorLabel);
        add(messagePanel, BorderLayout.CENTER);

        savedOutfitsArea.setEditable(false);
        add(new JScrollPane(savedOutfitsArea), BorderLayout.SOUTH);
    }

    public void refreshFromViewModel() {
        messageLabel.setText(viewModel.getMessage());
        errorLabel.setText(viewModel.getErrorMessage());

        StringBuilder sb = new StringBuilder();
        for (Outfit o : viewModel.getSavedOutfits()) {
            sb.append(o.getName())
                    .append(" | Weather: ").append(o.getWeatherProfile())
                    .append(" | Location: ").append(o.getLocation())
                    .append(" | Items: ").append(o.getItems())
                    .append("\n");
        }
        savedOutfitsArea.setText(sb.toString());
    }
}
