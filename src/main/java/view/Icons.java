package view;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

public final class Icons {
    private Icons() { }

    public static ImageIcon acorn(int height) {
        URL url = Icons.class.getResource("/acorn.png");
        if (url == null) {
            return null;
        }
        ImageIcon raw = new ImageIcon(url);
        int width = raw.getIconWidth() * height / raw.getIconHeight();
        return new ImageIcon(raw.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}