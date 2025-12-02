package view.profile;

import entity.User;
import interface_adapter.update_profile.UpdateUserProfileController;
import service.Backend;
import view.common.RightContentTemplate;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditProfileView extends RightContentTemplate implements PropertyChangeListener {

    private final String viewName = "editProfile";

    private JTextField nameField;
    private JTextField emailField;
    private JTextArea addressField;
    private JTextField phoneField;
    private JTextField idTypeField;
    private JTextField usernameField;
    private JTextField userTypeField;

    public EditProfileView(Backend backend, UpdateUserProfileController controller) {
        super("Edit Profile");

        // Load Current User
        User user = backend.getCurrentUser();
        if (user == null) {
            user = new User(0, "", "", User.idType.PHOTO_CARD, 0, "",
                    "", "", "user");
        }

        // Main card
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(850, 600));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Form fields
        nameField = createField(card, "Name:", user.getName());
        emailField = createField(card, "Email:", user.getEmail());
        phoneField = createField(card, "Phone:", String.valueOf(user.getPhoneNumber()));
        addressField = createTextArea(card, "Address:", user.getAddress());

        // Non-editable fields
        usernameField = createField(card, "Username:", user.getUsername());
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(240, 240, 240));

        userTypeField = createField(card, "User Type:", user.getUserType());
        userTypeField.setEditable(false);
        userTypeField.setBackground(new Color(240, 240, 240));

        idTypeField = createField(card, "ID Type:", user.getIdType().toString());
        idTypeField.setEditable(false);
        idTypeField.setBackground(new Color(240, 240, 240));

        card.add(Box.createVerticalStrut(20));

        // Save Button
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(90, 140, 255));
        saveBtn.setForeground(Color.BLACK);
        saveBtn.setFocusPainted(false);
        saveBtn.setPreferredSize(new Dimension(120, 40));
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        saveBtn.addActionListener(e -> controller.save(
                nameField.getText(),
                emailField.getText(),
                addressField.getText(),
                phoneField.getText()
        ));

        card.add(saveBtn);

        // Put card into RightContentTemplate bodyPanel 
        bodyPanel.setLayout(new GridBagLayout());
        bodyPanel.add(card);
    }


    /** Create label + single-line text field */
    private JTextField createField(JPanel card, String label, String value) {

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(700, 50));

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 40));
        lbl.setFont(new Font("Arial", Font.PLAIN, 15));

        JTextField field = new JTextField(value);
        field.setPreferredSize(new Dimension(450, 30));

        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);

        card.add(row);
        card.add(Box.createVerticalStrut(15));
        return field;
    }

    /** Create multi-line text field */
    private JTextArea createTextArea(JPanel card, String label, String value) {

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(700, 120));

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 40));
        lbl.setFont(new Font("Arial", Font.PLAIN, 15));

        JTextArea area = new JTextArea(value);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 100));

        row.add(lbl, BorderLayout.WEST);
        row.add(scroll, BorderLayout.CENTER);

        card.add(row);
        card.add(Box.createVerticalStrut(15));

        return area;
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
