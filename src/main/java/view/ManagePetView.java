package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import interface_adapter.manage_pet.ManagePetController;
import interface_adapter.manage_pet.ManagePetViewModel;
import interface_adapter.manage_pet.PetViewData;

/**
 * Shelter pet management and a read-only preview of pets available for adoption.
 */
public final class ManagePetView extends JPanel {
    private static final Color ERROR_COLOR = new Color(150, 28, 28);

    private final ManagePetController controller;
    private final ManagePetViewModel viewModel;
    private final JList<PetViewData> managedPets = createPetList(true);
    private final JList<PetViewData> availablePets = createPetList(false);
    private final JButton editButton = new JButton("Edit");
    private final JButton removeButton = new JButton("Remove");
    private final JLabel message = new JLabel(" ");
    private final JLabel photo = new JLabel();
    private final JTextArea details = new JTextArea("No pets are currently available.");

    /**
     * Creates the screen and observes results presented by the use case.
     * @param controller the controller for pet management actions
     * @param viewModel the state to display
     */
    public ManagePetView(ManagePetController controller, ManagePetViewModel viewModel) {
        super(new BorderLayout(0, 8));
        this.controller = controller;
        this.viewModel = viewModel;

        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(850, 470));
        final JSplitPane panels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createManagementPanel(), createGalleryPanel());
        panels.setResizeWeight(0.36);
        add(panels, BorderLayout.CENTER);
        add(message, BorderLayout.SOUTH);

        managedPets.addListSelectionListener(event -> updateEditButtons());
        availablePets.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                showGalleryDetails();
            }
        });
        viewModel.addPropertyChangeListener(event -> update());
        update();
    }

    private JPanel createManagementPanel() {
        final JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Pets"));
        final JButton addButton = new JButton("Add");
        final JPanel actions = new JPanel();
        actions.add(addButton);
        actions.add(editButton);
        actions.add(removeButton);
        panel.add(new JScrollPane(managedPets), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);

        addButton.addActionListener(event -> openForm(null));
        editButton.addActionListener(event -> openForm(managedPets.getSelectedValue()));
        removeButton.addActionListener(event -> removeSelectedPet());
        return panel;
    }

    private JPanel createGalleryPanel() {
        final JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Available Pets"));
        final JScrollPane galleryList = new JScrollPane(availablePets);
        galleryList.setPreferredSize(new Dimension(160, 0));
        panel.add(galleryList, BorderLayout.WEST);

        final JPanel preview = new JPanel(new BorderLayout(0, 8));
        photo.setHorizontalAlignment(JLabel.CENTER);
        photo.setVisible(false);
        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        preview.add(photo, BorderLayout.NORTH);
        preview.add(new JScrollPane(details), BorderLayout.CENTER);
        panel.add(preview, BorderLayout.CENTER);
        return panel;
    }

    private void openForm(PetViewData pet) {
        final PetForm form = new PetForm(controller, viewModel, pet);
        form.showDialog(this);
    }

    private void removeSelectedPet() {
        final PetViewData pet = managedPets.getSelectedValue();
        if (pet != null) {
            final int answer = JOptionPane.showConfirmDialog(this,
                    "Remove " + pet.getName() + " from the shelter's listings?", "Remove Pet",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (answer == JOptionPane.YES_OPTION) {
                controller.remove(pet.getId());
            }
        }
    }

    private void update() {
        updateList(managedPets, viewModel.getPets());
        updateList(availablePets, viewModel.getAvailablePets());
        updateEditButtons();
        message.setForeground(viewModel.hasError() ? ERROR_COLOR : getForeground());
        final String text = viewModel.getMessage();
        message.setText(text.isEmpty() ? " " : text);
    }

    private void updateEditButtons() {
        final boolean petSelected = !managedPets.isSelectionEmpty();
        editButton.setEnabled(petSelected);
        removeButton.setEnabled(petSelected);
    }

    private static JList<PetViewData> createPetList(boolean showStatus) {
        final JList<PetViewData> list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        final ListCellRenderer<Object> petRenderer = (display, value, index, selected, hasFocus) -> {
            Object label = value;
            if (value instanceof PetViewData) {
                final PetViewData pet = (PetViewData) value;
                final String suffix = showStatus ? pet.getStatus() : pet.getSpecies();
                label = pet.getName() + " (" + readable(suffix) + ")";
            }
            return renderer.getListCellRendererComponent(display, label, index, selected, hasFocus);
        };
        list.setCellRenderer(petRenderer);
        return list;
    }

    private static void updateList(JList<PetViewData> list, List<PetViewData> pets) {
        final PetViewData selected = list.getSelectedValue();
        final DefaultListModel<PetViewData> model = new DefaultListModel<>();
        int selection = 0;
        for (PetViewData pet : pets) {
            if (selected != null && selected.getId().equals(pet.getId())) {
                selection = model.size();
            }
            model.addElement(pet);
        }
        list.setModel(model);
        if (!model.isEmpty()) {
            list.setSelectedIndex(selection);
        }
    }

    private void showGalleryDetails() {
        final PetViewData pet = availablePets.getSelectedValue();
        photo.setIcon(null);
        photo.setText("");
        photo.setVisible(false);
        if (pet == null) {
            details.setText("No pets are currently available.");
        }
        else {
            details.setText(describe(pet));
            details.setCaretPosition(0);
            showPhoto(pet.getPhotoPath());
        }
    }

    private static String describe(PetViewData pet) {
        return String.join("\n",
                pet.getName(),
                readable(pet.getSpecies()) + " · " + pet.getBreed(),
                pet.getAgeYears() + " years old · " + readable(pet.getSex()),
                "Temperament  " + readable(String.join(", ", pet.getTemperamentTags())),
                "Health notes  " + pet.getHealthNotes(),
                "Needs medication  " + yesOrNo(pet.needsMedication()),
                "Vaccinated  " + yesOrNo(pet.isVaccinated()),
                "Spayed/neutered  " + yesOrNo(pet.isSpayedNeutered()),
                "Adoption fee  $" + pet.getAdoptionFee());
    }

    private void showPhoto(String photoPath) {
        if (!photoPath.isBlank()) {
            photo.setVisible(true);
            try {
                final BufferedImage image = ImageIO.read(new File(photoPath));
                if (image != null) {
                    final double scale = Math.min(1.0, Math.min(245.0 / image.getWidth(), 150.0 / image.getHeight()));
                    final int width = Math.max(1, (int) (image.getWidth() * scale));
                    final int height = Math.max(1, (int) (image.getHeight() * scale));
                    photo.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                }
                else {
                    photo.setText("Photo unavailable");
                }
            }
            catch (IOException exception) {
                photo.setText("Photo unavailable");
            }
        }
    }

    private static String readable(String value) {
        return value.toLowerCase(Locale.ROOT).replace('_', ' ');
    }

    private static String yesOrNo(boolean value) {
        return value ? "Yes" : "No";
    }
}
