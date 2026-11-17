package view;

import interface_adapter.adopt_pet.AdoptPetState;
import interface_adapter.adopt_pet.AdoptPetViewModel;
import interface_adapter.adopt_pet.AdoptPetViewModelListener;

import javax.swing.*;
import java.awt.*;

public class AdoptPetView extends JPanel
        implements AdoptPetViewModelListener {

    private final AdoptPetViewModel viewModel;

    private final JComboBox<String> speciesInput =
            new JComboBox<>(new String[]{"DOG", "CAT"});
    private final JTextField nameInput = new JTextField(15);
    private final JLabel imageLabel =
            new JLabel("Choose a species, then request a picture.");
    private final JLabel errorLabel = new JLabel();

    private final JButton anotherPictureButton =
            new JButton("Another Picture");
    private final JButton adoptButton = new JButton("Adopt");

    public AdoptPetView(AdoptPetViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Adopt a Pet");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel speciesPanel = new JPanel();
        speciesPanel.add(new JLabel("Species:"));
        speciesPanel.add(speciesInput);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(350, 250));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Pet name:"));
        namePanel.add(nameInput);

        JPanel buttons = new JPanel();
        buttons.add(anotherPictureButton);
        buttons.add(adoptButton);

        add(title);
        add(speciesPanel);
        add(imagePanel);
        add(namePanel);
        add(errorLabel);
        add(buttons);

        stateChanged();
    }

    @Override
    public void stateChanged() {
        AdoptPetState state = viewModel.getState();

        speciesInput.setSelectedItem(state.getSpecies());
        nameInput.setText(state.getPetName());
        errorLabel.setText(state.getErrorMessage());

        speciesInput.setEnabled(!state.isLoading());
        anotherPictureButton.setEnabled(!state.isLoading());

        adoptButton.setEnabled(
                !state.isLoading() && !state.getImageUrl().isEmpty()
        );

        imageLabel.setText(
                state.isLoading()
                        ? "Loading picture..."
                        : "Choose a species, then request a picture."
        );
    }
}