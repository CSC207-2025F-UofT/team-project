package view;

import javax.swing.*;
import java.awt.*;

public class UIFactory {

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
//        btn.setForeground(Color.WHITE);
//        btn.setBackground(new Color(0x4A7CFF));
        btn.setForeground(new Color(0x2D3436));
        btn.setBackground(new Color(0xAECBFA));
        
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0x93B6F1));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0xAECBFA));
            }
        });

        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setForeground(new Color(50, 50, 50));
        btn.setBackground(new Color(240, 240, 240));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0xD5D7DA));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0xE5E7EB));
            }
        });

        return btn;
    }

    // Add a bit of padding + a thin border to the input field
    public static void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }
}