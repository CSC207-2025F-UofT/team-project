package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontManager {
    private static Font winkle;
    private static Font appleGaramond;
    private static Font appleGaramondBold;
    private static Font appleGaramondItalic;

    public static void loadFonts(){
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            winkle = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/Winkle-Regular.ttf"));
            ge.registerFont(winkle);

            appleGaramond = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond.ttf"));
            ge.registerFont(appleGaramond);

            appleGaramondBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond-Bold.ttf"));
            ge.registerFont(appleGaramondBold);

            appleGaramondItalic = appleGaramondItalic = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond-LightItalic.ttf"));
            ge.registerFont(appleGaramondItalic);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Failed to load custom typography files.", e);
        }
    }

    public static Font getWinkle(float size) { return winkle.deriveFont(size); }
    public static Font getAppleGaramond(float size) { return appleGaramond.deriveFont(size); }
    public static Font getAppleGaramondBold(float size) { return appleGaramondBold.deriveFont(size); }
    public static Font getAppleGaramondItalic(float size) { return appleGaramondItalic.deriveFont(size); }

}
