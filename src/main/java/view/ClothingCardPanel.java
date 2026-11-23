package view;

import use_case.outfit.OutfitOutputData.ClothingData;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public final class ClothingCardPanel extends JPanel {
    public ClothingCardPanel(ClothingData item) {
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JLabel image = new JLabel("No image", SwingConstants.CENTER);
        image.setPreferredSize(new Dimension(120, 95));
        // Only local thumbnail display; entities and use cases never load Swing icons.
        if (!item.imagePath.isBlank() && new File(item.imagePath).isFile()) {
            ImageIcon icon = new ImageIcon(item.imagePath);
            if (icon.getIconWidth() > 0) {
                double scale = Math.min(120.0 / icon.getIconWidth(), 95.0 / icon.getIconHeight());
                image.setIcon(new ImageIcon(icon.getImage().getScaledInstance(
                        Math.max(1, (int) (icon.getIconWidth() * scale)),
                        Math.max(1, (int) (icon.getIconHeight() * scale)), Image.SCALE_SMOOTH)));
                image.setText("");
            }
        }
        add(image, BorderLayout.CENTER);
        add(new JLabel(item.name, SwingConstants.CENTER), BorderLayout.SOUTH);
    }
}
