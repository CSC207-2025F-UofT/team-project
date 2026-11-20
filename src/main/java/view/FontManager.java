package view;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * The Font manager for all custom fonts within the application.
 */
public class FontManager {
    private static Font winkle;
    private static Font appleGaramond;
    private static Font appleGaramondBold;
    private static Font appleGaramondItalic;
    private static Font ginny;

    /**
     * Loads the font files and registers them with the graphics environment.
     */
    public static void loadFonts(){
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            winkle = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/Winkle-Regular.ttf"));
            ge.registerFont(winkle);

            appleGaramond = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond.ttf"));
            ge.registerFont(appleGaramond);

            appleGaramondBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond-Bold.ttf"));
            ge.registerFont(appleGaramondBold);

            appleGaramondItalic = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond-LightItalic.ttf"));
            ge.registerFont(appleGaramondItalic);

            ginny = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/Ginny-Regular.ttf"));
            ge.registerFont(ginny);

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Failed to load custom font.", e);
        }
    }

    /**
     * Returns the Winkle font at the given size.
     *
     * @param size the font size
     */
    public static Font getWinkle(float size) { return winkle.deriveFont(size); }

    /**
     * Returns the Apple Garamond font at the given size.
     *
     * @param size the font size
     */
    public static Font getAppleGaramond(float size) { return appleGaramond.deriveFont(size); }

    /**
     * Returns the Bold Apple Garamond font at the given size.
     *
     * @param size the font size
     */
    public static Font getAppleGaramondBold(float size) { return appleGaramondBold.deriveFont(size); }

    /**
     * Returns the Italic Apple Garamond font at the given size.
     *
     * @param size the font size
     */
    public static Font getAppleGaramondItalic(float size) { return appleGaramondItalic.deriveFont(size); }

    /**
     * Returns the Ginny font at the given size.
     *
     * @param size the font size
     */
    public static Font getGinny(float size) {
        return ginny.deriveFont(size);
    }

}
