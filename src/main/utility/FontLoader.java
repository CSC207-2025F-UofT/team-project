package main.utility;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    public static void registerFonts() {
        try {
            Font Helvetica = Font.createFont(Font.TRUETYPE_FONT, new File("font/Helvetica.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Helvetica);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}
