package view;

import interface_adapter.pet_management.PetManagementController;
import interface_adapter.pet_management.PetManagementState;
import interface_adapter.pet_management.PetManagementViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AdminPetManagementView extends JPanel implements ActionListener, PropertyChangeListener {

    // Which “page” this instance behaves as
    public enum Mode { ADD, MODIFY, DELETE }

    private final String viewName = "petManagement";

    private final PetManagementViewModel viewModel;
    private PetManagementController controller;
    private final Mode mode;

    private final JTextField idField = new JTextField(8);
    private final JTextField nameField = new JTextField(12);
    private final JTextField ageField = new JTextField(8);
    private final JTextField genderField = new JTextField(8);
    private final JTextField speciesField = new JTextField(12);
    private final JTextField breedField = new JTextField(12);
    private final JTextField colourField = new JTextField(10);
    private final JTextField sizeField = new JTextField(10);
    private final JTextField imageUrlField = new JTextField(15);

    private final JTextArea descriptionArea = new JTextArea(3, 20);

    private final JLabel statusLabel = new JLabel(" ");

    private final JButton addButton = new JButton("Add");
    private final JButton updateButton = new JButton("Update");
    private final JButton deleteButton = new JButton("Delete");

    // Default to ADD if no mode is given
    public AdminPetManagementView(PetManagementViewModel viewModel) {
        this(viewModel, Mode.ADD);
    }

    public AdminPetManagementView(PetManagementViewModel viewModel, Mode mode) {
        this.viewModel = viewModel;
        this.mode = mode;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        form.add(new JLabel("ID:"));
        form.add(idField);

        form.add(new JLabel("Name:"));
        form.add(nameField);

        form.add(new JLabel("Age (BABY/YOUNG/ADULT/SENIOR):"));
        form.add(ageField);

        form.add(new JLabel("Gender (MALE/FEMALE):"));
        form.add(genderField);

        form.add(new JLabel("Species:"));
        form.add(speciesField);

        form.add(new JLabel("Breed:"));
        form.add(breedField);

        form.add(new JLabel("Colour:"));
        form.add(colourField);

        form.add(new JLabel("Size (SMALL/MEDIUM/LARGE/EXTRA_LARGE):"));
        form.add(sizeField);

        form.add(new JLabel("Image URL:"));
        form.add(imageUrlField);

        form.add(new JLabel("Description:"));
        form.add(new JScrollPane(descriptionArea));

        JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);

        add(statusLabel, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);

        configureMode();
    }

    // Turn buttons/fields on/off based on what this page is for
    private void configureMode() {
        switch (mode) {
            case ADD:
                addButton.setVisible(true);
                updateButton.setVisible(false);
                deleteButton.setVisible(false);
                // everything editable
                break;

            case MODIFY:
                addButton.setVisible(false);
                updateButton.setVisible(true);
                deleteButton.setVisible(false);
                // all fields editable; user types ID + new values
                // (auto-fill from ID would require a small extra use case)
                break;

            case DELETE:
                addButton.setVisible(false);
                updateButton.setVisible(false);
                deleteButton.setVisible(true);

                // Make it feel like “just ID”
                nameField.setEnabled(false);
                ageField.setEnabled(false);
                genderField.setEnabled(false);
                speciesField.setEnabled(false);
                breedField.setEnabled(false);
                colourField.setEnabled(false);
                sizeField.setEnabled(false);
                imageUrlField.setEnabled(false);
                descriptionArea.setEnabled(false);
                break;
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setController(PetManagementController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (controller == null) return;

        Object src = e.getSource();

        if (src == addButton) {
            controller.addPet(
                    idField.getText(),
                    nameField.getText(),
                    ageField.getText(),
                    genderField.getText(),
                    speciesField.getText(),
                    breedField.getText(),
                    colourField.getText(),
                    sizeField.getText(),
                    descriptionArea.getText(),
                    imageUrlField.getText()
            );
        } else if (src == updateButton) {
            controller.updatePet(
                    idField.getText(),
                    nameField.getText(),
                    ageField.getText(),
                    genderField.getText(),
                    speciesField.getText(),
                    breedField.getText(),
                    colourField.getText(),
                    sizeField.getText(),
                    descriptionArea.getText(),
                    imageUrlField.getText()
            );
        } else if (src == deleteButton) {
            controller.deletePet(idField.getText());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PetManagementState state = (PetManagementState) evt.getNewValue();
        statusLabel.setText(state.getStatusMessage());
    }
}
