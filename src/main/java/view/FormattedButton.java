package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.Normalizer;

/**
 * Formats a button
 */
public class FormattedButton extends JButton {

    /**
     * By default, will give home button
     */
    public FormattedButton() {
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

    /**
     * Gives formatted button
     * @param text text for button to display
     */
    public FormattedButton(String text) {
        this.setText(text);
        this.setFont(new Font(AppTheme.FONT_NAME, Font.BOLD, AppTheme.FONT_SIZE_BUTTON));
        this.setForeground(Color.WHITE);
        this.setBackground(AppTheme.PRIMARY_COLOR);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setOpaque(true);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setBorder(new EmptyBorder(10, 20, 10, 20));
    }





}
