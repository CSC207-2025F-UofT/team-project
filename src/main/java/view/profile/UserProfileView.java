package view.profile;

import entity.User;
import service.Backend;
import view.common.RightContentTemplate;

import javax.swing.*;
import java.awt.*;

public class UserProfileView extends RightContentTemplate {

    public UserProfileView(Backend backend) {
        super("My Profile");

        User user = backend.getCurrentUser();
        if (user == null) {
            add(new JLabel("No user logged in."), BorderLayout.CENTER);
            return;
        }

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        card.setMaximumSize(new Dimension(850, 600));

        addField(card, "Name:", user.getName());
        addField(card, "Email:", user.getEmail());
        addField(card, "Address:", user.getAddress());
        addField(card, "User Type:", user.getUserType());
        addField(card, "Phone:", String.valueOf(user.getPhoneNumber()));
        addField(card, "ID Type:", user.getIdType().toString());

        bodyPanel.setLayout(new GridBagLayout());
        bodyPanel.add(card);
    }

    private void addField(JPanel card, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(700, 50));

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(150, 30));
        lbl.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 14));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);

        card.add(row);
        card.add(Box.createVerticalStrut(15));
    }
}
