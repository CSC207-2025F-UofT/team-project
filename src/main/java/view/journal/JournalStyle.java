package view.journal;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

final class JournalStyle {
    static final int WIDTH = 414;
    static final int HEIGHT = 896;
    static final Color PAPER = new Color(0xfcfcfc);
    static final Color GREEN = new Color(0xdceedf);
    static final Color CARD_GREEN = new Color(0xe2efe3);
    static final Color PHOTO_BUTTON = new Color(0xdff0e2);
    static final Color INK = new Color(0x202624);
    static final Font UI_FONT = loadFont("DM-Sans");
    static final Font HANDWRITING = loadFont("Gochi-Hand");

    private JournalStyle() {
    }

    private static Font loadFont(String name) {
        try (InputStream stream = Objects.requireNonNull(
                JournalStyle.class.getResourceAsStream("/journal/fonts/" + name + ".ttf"), name)) {
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (IOException | FontFormatException exception) {
            throw new IllegalStateException("Could not load " + name, exception);
        }
    }

    static JLabel label(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.putClientProperty("html.disable", true);
        label.setFont(font);
        label.setForeground(INK);
        return label;
    }

    static ImageIcon loadScaledIcon(String assetName, int iconWidth, int iconHeight) {
        ImageIcon sourceIcon = new ImageIcon(Objects.requireNonNull(
                JournalStyle.class.getResource("/journal/assets/" + assetName + ".png"), assetName));
        return new ImageIcon(sourceIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));
    }

    static JButton iconButton(String label, ImageIcon icon, Runnable onClick) {
        JButton button = new JButton(icon);
        button.getAccessibleContext().setAccessibleName(label);
        button.setToolTipText(label);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(event -> onClick.run());
        return button;
    }

    static JButton roundedButton(String label, Color fill, Runnable onClick) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics graphics) {
                paintRounded(graphics, fill, getWidth(), getHeight(), getHeight());
                super.paintComponent(graphics);
            }
        };
        button.setUI(new BasicButtonUI());
        button.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        button.setFont(UI_FONT.deriveFont(12f));
        button.setForeground(INK);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(event -> onClick.run());
        return button;
    }

    static void paintRounded(Graphics graphics, Color fill, int width, int height, int cornerDiameter) {
        Graphics2D roundedShapeGraphics = (Graphics2D) graphics.create();
        roundedShapeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        roundedShapeGraphics.setColor(fill);
        roundedShapeGraphics.fillRoundRect(0, 0, width, height, cornerDiameter, cornerDiameter);
        roundedShapeGraphics.dispose();
    }

    static void addPageHeader(JPanel panel, String title, Runnable onBack) {
        JButton backButton = iconButton("Back", loadScaledIcon("back", 32, 32), onBack);
        backButton.setBounds(17, 22, 36, 36);
        panel.add(backButton);
        JLabel heading = label(title, UI_FONT.deriveFont(Font.BOLD, 26f));
        heading.setBounds(65, 22, WIDTH - 130, 36);
        panel.add(heading);
    }
}
