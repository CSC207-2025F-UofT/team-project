package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Formats the home button
 */
public class HomeButton extends JButton {

    public HomeButton() {
        this.setText("Home");
        this.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, 13));
        this.setForeground(AppTheme.PRIMARY_COLOR);
        this.setBackground(AppTheme.BG_SECONDARY);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setOpaque(true);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setBorder(new EmptyBorder(10, 20, 10, 20));
    }



}
