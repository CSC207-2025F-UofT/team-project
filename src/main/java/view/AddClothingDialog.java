package view;

import interface_adapter.wardrobe.WardrobeController;
import interface_adapter.wardrobe.WardrobeViewModel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.File;

/** Clothing registration form (UC1 steps 2-5). */
public final class AddClothingDialog extends JDialog {
    private static final String[] TYPES = {"HAT", "TOP", "BOTTOM", "SHOES"};

    private final JTextField name = new JTextField(20);
    private final JComboBox<String> type = new JComboBox<>(TYPES);
    private final JLabel preview = new JLabel("No image selected", SwingConstants.CENTER);
    private final JLabel imageName = new JLabel(" ");
    private final JButton choose = new JButton("Choose image...");
    private final JButton save = new JButton("Save");
    private final JButton cancel = new JButton("Cancel");
    private final WardrobeViewModel model;
    private final PropertyChangeListener listener;
    private String imagePath = "";

    public AddClothingDialog(Window owner, WardrobeController controller, WardrobeViewModel model) {
        super(owner, "Add clothing", ModalityType.APPLICATION_MODAL);
        this.model = model;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        preview.setPreferredSize(new Dimension(220, 220));
        preview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JPanel left = new JPanel(new BorderLayout(4, 4));
        left.add(preview, BorderLayout.CENTER);
        JPanel imageControls = new JPanel(new BorderLayout(4, 4));
        imageControls.add(choose, BorderLayout.NORTH);
        imageControls.add(imageName, BorderLayout.SOUTH);
        left.add(imageControls, BorderLayout.SOUTH);
        content.add(left, BorderLayout.WEST);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Name"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1; form.add(name, c);
        c.gridx = 0; c.gridy = 1; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        form.add(new JLabel("Type"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; form.add(type, c);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        form.add(Box.createVerticalGlue(), c);
        content.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel); buttons.add(save);
        content.add(buttons, BorderLayout.SOUTH);

        choose.addActionListener(e -> chooseImage());
        // Validation lives in the use case; the dialog stays open on error so the user can retry.
        save.addActionListener(e -> controller.addClothing(
                name.getText(), (String) type.getSelectedItem(), imagePath));
        cancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(save);

        listener = event -> {
            if (event.getPropertyName().equals("busy")) {
                boolean enabled = !((Boolean) event.getNewValue());
                save.setEnabled(enabled); choose.setEnabled(enabled); cancel.setEnabled(enabled);
                setDefaultCloseOperation(enabled ? DISPOSE_ON_CLOSE : DO_NOTHING_ON_CLOSE);
            } else if (event.getPropertyName().equals("state")) {
                if (model.getState().itemAdded) dispose();
            }
        };
        model.addListener(listener);
        pack();
        setLocationRelativeTo(owner);
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Images (png, jpg, jpeg, gif, bmp)", "png", "jpg", "jpeg", "gif", "bmp"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        imagePath = file.getAbsolutePath();
        imageName.setText(file.getName());
        ImageIcon icon = new ImageIcon(imagePath);
        if (icon.getIconWidth() > 0) {
            double scale = Math.min(210.0 / icon.getIconWidth(), 210.0 / icon.getIconHeight());
            preview.setIcon(new ImageIcon(icon.getImage().getScaledInstance(
                    Math.max(1, (int) (icon.getIconWidth() * scale)),
                    Math.max(1, (int) (icon.getIconHeight() * scale)), Image.SCALE_SMOOTH)));
            preview.setText("");
        } else {
            preview.setIcon(null);
            preview.setText("Preview unavailable");
        }
    }

    @Override public void dispose() {
        if (listener != null) model.removeListener(listener);
        super.dispose();
    }
}
