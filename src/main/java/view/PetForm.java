package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import interface_adapter.manage_pet.BreedReferenceViewData;
import interface_adapter.manage_pet.ManagePetController;
import interface_adapter.manage_pet.ManagePetViewModel;
import interface_adapter.manage_pet.PetViewData;

/**
 * Shared form for adding a new pet and updating an existing pet.
 */
final class PetForm extends JPanel {
    private static final String BREED_REFERENCE_TITLE = "Breed reference";

    private final ManagePetController controller;
    private final ManagePetViewModel viewModel;
    private final PetViewData pet;
    private final JTextField name = new JTextField(24);
    private final JComboBox<String> species;
    private final JTextField breed = new JTextField(24);
    private final JTextField age = new JTextField(8);
    private final JComboBox<String> sex;
    private final List<JCheckBox> temperament = new ArrayList<>();
    private final JTextArea healthNotes = new JTextArea(3, 24);
    private final JCheckBox needsMedication = new JCheckBox("Needs medication");
    private final JCheckBox vaccinated = new JCheckBox("Vaccinated");
    private final JCheckBox spayedNeutered = new JCheckBox("Spayed/neutered");
    private final JTextField fee = new JTextField(8);
    private final JTextField photoPath = new JTextField(20);
    private final JTextArea error = new JTextArea(2, 1);

    PetForm(ManagePetController controller, ManagePetViewModel viewModel, PetViewData pet) {
        super(new GridBagLayout());
        this.controller = controller;
        this.viewModel = viewModel;
        this.pet = pet;
        species = new JComboBox<>(viewModel.getSpeciesOptions());
        sex = new JComboBox<>(viewModel.getSexOptions());
        species.setSelectedIndex(-1);
        sex.setSelectedIndex(-1);
        healthNotes.setLineWrap(true);
        healthNotes.setWrapStyleWord(true);
        photoPath.setEditable(false);
        error.setForeground(new Color(150, 28, 28));
        error.setEditable(false);
        error.setLineWrap(true);
        error.setWrapStyleWord(true);
        error.setOpaque(false);

        addRow(0, "Name", name);
        addRow(1, "Species", species);
        addRow(2, "Breed", createBreedInput());
        addRow(3, "Age (whole years)", age);
        addRow(4, "Sex", sex);
        addRow(5, "Temperament tags", createTemperamentOptions());
        addRow(6, "Health notes", new JScrollPane(healthNotes));
        addRow(7, "Care and procedures", createHealthOptions());
        addRow(8, "Adoption fee ($)", fee);
        addRow(9, "Photo (optional)", createPhotoPicker());
        if (pet != null) {
            populateForm(pet);
        }
    }

    void showDialog(Component owner) {
        final String title = pet == null ? "Add Pet" : "Edit Pet";
        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(owner), title,
                Dialog.ModalityType.APPLICATION_MODAL);
        final JButton save = new JButton("Save");
        final JButton cancel = new JButton("Cancel");
        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.TRAILING, 6, 0));
        buttons.add(cancel);
        buttons.add(save);
        final JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.add(error, BorderLayout.CENTER);
        footer.add(buttons, BorderLayout.EAST);
        final JPanel content = new JPanel(new BorderLayout(0, 8));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(this, BorderLayout.CENTER);
        content.add(footer, BorderLayout.SOUTH);

        save.addActionListener(event -> {
            submit();
            if (viewModel.hasError()) {
                error.setText(viewModel.getMessage());
            }
            else {
                dialog.dispose();
            }
        });
        cancel.addActionListener(event -> dialog.dispose());
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(content);
        dialog.getRootPane().setDefaultButton(save);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private void addRow(int row, String label, Component field) {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.insets = new Insets(3, 3, 3, 3);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        final JLabel caption = new JLabel(label);
        caption.setLabelFor(field);
        add(caption, constraints);
        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(field, constraints);
    }

    private JPanel createBreedInput() {
        final JPanel panel = new JPanel(new BorderLayout(6, 0));
        final JButton lookup = new JButton("Look up breed");
        breed.getAccessibleContext().setAccessibleName("Breed");
        panel.add(breed, BorderLayout.CENTER);
        panel.add(lookup, BorderLayout.EAST);
        lookup.addActionListener(event -> lookUpBreed(lookup));
        return panel;
    }

    private void lookUpBreed(JButton lookup) {
        final String query = breed.getText().strip();
        lookup.setEnabled(false);
        lookup.setText("Looking up…");
        final SwingWorker<Optional<BreedReferenceViewData>, Void> worker = new SwingWorker<>() {
            @Override
            protected Optional<BreedReferenceViewData> doInBackground() {
                return controller.lookupBreed(query);
            }

            @Override
            protected void done() {
                lookup.setEnabled(true);
                lookup.setText("Look up breed");
                if (isShowing() && query.equalsIgnoreCase(breed.getText().strip())) {
                    showLookupResult(this);
                }
            }
        };
        worker.execute();
    }

    private void showLookupResult(SwingWorker<Optional<BreedReferenceViewData>, Void> worker) {
        try {
            final Optional<BreedReferenceViewData> reference = worker.get();
            if (reference.isPresent()) {
                showBreedReference(reference.get());
            } else {
                JOptionPane.showMessageDialog(this, "No exact match. Try the full breed name or continue manually.",
                        BREED_REFERENCE_TITLE, JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException exception) {
            final Throwable cause = exception.getCause();
            final String message = cause instanceof IllegalArgumentException || cause instanceof IllegalStateException
                    ? cause.getMessage() : "Breed lookup is unavailable. You can still fill in the form.";
            JOptionPane.showMessageDialog(this, message, BREED_REFERENCE_TITLE, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showBreedReference(BreedReferenceViewData reference) {
        final JPanel result = new JPanel(new BorderLayout(0, 8));
        final String traits = reference.getTemperament().isBlank() ? "Unavailable" : reference.getTemperament();
        final JTextArea summary = new JTextArea(reference.getName() + "\nTypical temperament: " + traits
                + "\nSource: TheCatAPI", 4, 32);
        summary.setEditable(false);
        summary.setLineWrap(true);
        summary.setWrapStyleWord(true);
        summary.setOpaque(false);
        result.add(summary, BorderLayout.NORTH);
        final JLabel photo = new JLabel("Reference photo unavailable", JLabel.CENTER);
        final byte[] referencePhoto = reference.getPhoto();
        if (referencePhoto.length > 0) {
            final ImageIcon icon = new ImageIcon(referencePhoto);
            if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                final double scale = Math.min(1.0, Math.min(250.0 / icon.getIconWidth(), 170.0 / icon.getIconHeight()));
                final int width = Math.max(1, (int) (icon.getIconWidth() * scale));
                final int height = Math.max(1, (int) (icon.getIconHeight() * scale));
                photo.setText("");
                photo.setIcon(new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                photo.getAccessibleContext().setAccessibleName("Breed reference photo");
            }
        }
        result.add(photo, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(this, result, BREED_REFERENCE_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel createTemperamentOptions() {
        final JPanel panel = new JPanel(new GridLayout(0, 2, 8, 0));
        for (String option : viewModel.getTemperamentOptions()) {
            final JCheckBox tag = new JCheckBox(option.toLowerCase(Locale.ROOT).replace('_', ' '));
            tag.setActionCommand(option);
            temperament.add(tag);
            panel.add(tag);
        }
        return panel;
    }

    private JPanel createHealthOptions() {
        final JPanel panel = new JPanel(new GridLayout(0, 2, 8, 0));
        panel.add(needsMedication);
        panel.add(vaccinated);
        panel.add(spayedNeutered);
        return panel;
    }

    private JPanel createPhotoPicker() {
        final JPanel panel = new JPanel(new BorderLayout(6, 0));
        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        final JButton choose = new JButton("Browse…");
        final JButton clear = new JButton("Clear");
        buttons.add(choose);
        buttons.add(clear);
        panel.add(photoPath, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.EAST);
        choose.addActionListener(event -> choosePhoto());
        clear.addActionListener(event -> {
            showPhotoPath("");
            error.setText("");
        });
        return panel;
    }

    private void choosePhoto() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a pet photo");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            try {
                if (ImageIO.read(file) == null) {
                    error.setText("Choose a readable image file.");
                }
                else {
                    showPhotoPath(file.getAbsolutePath());
                    error.setText("");
                }
            }
            catch (IOException exception) {
                error.setText("Choose a readable image file.");
            }
        }
    }

    private void populateForm(PetViewData selected) {
        name.setText(selected.getName());
        species.setSelectedItem(selected.getSpecies());
        breed.setText(selected.getBreed());
        age.setText(Integer.toString(selected.getAgeYears()));
        sex.setSelectedItem(selected.getSex());
        for (JCheckBox tag : temperament) {
            tag.setSelected(selected.getTemperamentTags().contains(tag.getActionCommand()));
        }
        healthNotes.setText(selected.getHealthNotes());
        needsMedication.setSelected(selected.needsMedication());
        vaccinated.setSelected(selected.isVaccinated());
        spayedNeutered.setSelected(selected.isSpayedNeutered());
        fee.setText(selected.getAdoptionFee());
        showPhotoPath(selected.getPhotoPath());
    }

    private void showPhotoPath(String path) {
        photoPath.setText(path);
        photoPath.setToolTipText(path.isEmpty() ? null : path);
        photoPath.setCaretPosition(0);
    }

    private void submit() {
        final List<String> tags = new ArrayList<>();
        for (JCheckBox tag : temperament) {
            if (tag.isSelected()) {
                tags.add(tag.getActionCommand());
            }
        }
        final String selectedSpecies = (String) species.getSelectedItem();
        final String selectedSex = (String) sex.getSelectedItem();
        if (pet == null) {
            controller.add(name.getText(), selectedSpecies, breed.getText(), age.getText(), selectedSex,
                    tags, healthNotes.getText(), needsMedication.isSelected(), vaccinated.isSelected(),
                    spayedNeutered.isSelected(), fee.getText(), photoPath.getText());
        }
        else {
            controller.edit(pet.getId(), name.getText(), selectedSpecies, breed.getText(), age.getText(), selectedSex,
                    tags, healthNotes.getText(), needsMedication.isSelected(), vaccinated.isSelected(),
                    spayedNeutered.isSelected(), fee.getText(), photoPath.getText());
        }
    }
}
