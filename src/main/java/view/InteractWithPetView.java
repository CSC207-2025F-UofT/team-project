package view;

import interface_adapter.interact_with_pet.InteractWithPetState;
import interface_adapter.interact_with_pet.InteractWithPetViewModel;
import interface_adapter.interact_with_pet.InteractWithPetViewModelListener;

import javax.swing.*;
import java.awt.*;

public class InteractWithPetView extends JPanel
        implements InteractWithPetViewModelListener {

    private final InteractWithPetViewModel viewModel;

    private final JLabel nameLabel = new JLabel();
    private final JLabel speciesLabel = new JLabel();
    private final JLabel imageLabel =
            new JLabel("Pet picture will appear here.");

    public InteractWithPetView(InteractWithPetViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Interact with Your Pet");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        speciesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(350, 250));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        add(title);
        add(nameLabel);
        add(speciesLabel);
        add(imagePanel);

        stateChanged();
    }

    @Override
    public void stateChanged() {
        InteractWithPetState state = viewModel.getState();

        nameLabel.setText("Name: " + state.getPetName());
        speciesLabel.setText("Species: " + state.getSpecies());
    }
}