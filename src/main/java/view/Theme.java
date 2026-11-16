package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * The app's shared colors and button styles, so every page looks the same.
 */
public final class Theme {
  static final Color GREEN = new Color(0xC7C70);
  static final Color ON_GREEN = Color.WHITE;
  static final Color RED = new Color(0xD7261E);
  static final Color YELLOW = new Color(0xFFD54A);
  static final Color DISABLED = new Color(0xE0E0E0);
  static final Color DIM = new Color(0, 0, 0, 120);

  /** Default size for body text, in points. */
  static final float BODY_SIZE_SMALL = 12f;
  static final float BODY_SIZE = 14f;
  static final float HEADER_SIZE = 18f;
  static final float TITLE_SIZE = 32f;
  static final Font SANS_REGULAR = loadFont("IBMPlexSans-Regular.ttf");
  static final Font SANS_BOLD = loadFont("IBMPlexSans-Bold.ttf");

  private Theme() {
  }

  /**
   * Reads our chosen font file from src/main/resources/fonts/ and 
   * registers it with Java. If the file is missing or broken, a system
   * font is returned instead so the app still runs.
   *
   * @param file the font file name 
   * @return the loaded font, or the fallback font
   */
  public static Font loadFont(String file) {
    try (InputStream in = Theme.class.getResourceAsStream("/fonts/" + file)) {
      Font font = Font.createFont(Font.TRUETYPE_FONT, in);
      GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
      return font;
    } catch (IOException | FontFormatException | NullPointerException e) {
      return new Font(Font.SANS_SERIF, Font.PLAIN, 12);   
    }
  }

  /** Makes IBM Plex Sans the default font for every Swing component. Call once in main. */
  public static void installDefaultFonts() {
    Font body = SANS_REGULAR.deriveFont(BODY_SIZE);
    UIManager.put("defaultFont", body);   // used by FlatLaf, if you add it
    for (String key : new String[] {"Label.font", "Button.font", "TextField.font",
        "TextArea.font", "TextPane.font", "CheckBox.font", "RadioButton.font",
        "ComboBox.font", "List.font", "Table.font", "TabbedPane.font", "ToolTip.font",
        "OptionPane.messageFont", "OptionPane.buttonFont"}) {
      UIManager.put(key, body);
    }
  }

  /**
   * A flat button filled with one color. Opaque and borderless, so the color also shows on
   * macOS.
   *
   * @param text the button text
   * @param background the fill
   * @param foreground the text color
   * @return the button
   */
  static JButton filledButton(String text, Color background, Color foreground) {
    JButton button = new JButton(text);
    button.setBackground(background);
    button.setForeground(foreground);
    button.setOpaque(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    return button;
  }

  /**
   * Shows a modal dialog with its owner window darkened behind it, until the dialog closes.
   *
   * @param dialog the modal dialog to show
   */
  static void showDimmed(JDialog dialog) {
    JRootPane root = dialog.getOwner() instanceof RootPaneContainer
        ? ((RootPaneContainer) dialog.getOwner()).getRootPane() : null;
    if (root == null) {
      dialog.setVisible(true);
      return;
    }
    final Component oldGlass = root.getGlassPane();
    JComponent dim = new JComponent() {
      @Override
      protected void paintComponent(Graphics graphics) {
        graphics.setColor(DIM);
        graphics.fillRect(0, 0, getWidth(), getHeight());
      }
    };
    root.setGlassPane(dim);
    dim.setVisible(true);
    // A modal dialog blocks here until it closes.
    dialog.setVisible(true);
    root.setGlassPane(oldGlass);
  }

  /**
   * A green circle with white text in the middle, e.g. a step number or a check mark.
   *
   * @param text what goes in the circle
   * @param size the circle's diameter in pixels
   * @param fontSize the text size in points
   * @return the circle
   */
  static JComponent circle(String text, int size, float fontSize) {
    JLabel circle = new JLabel(text, SwingConstants.CENTER) {
      @Override
      protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(GREEN);
        g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
        g.dispose();
        super.paintComponent(graphics);
      }
    };
    circle.setForeground(ON_GREEN);
    circle.setFont(bold(fontSize));
    Dimension fixed = new Dimension(size, size);
    circle.setPreferredSize(fixed);
    circle.setMaximumSize(fixed);
    return circle;
  }

  /**
   * A button that looks like an underlined link.
   *
   * @param text the link text
   * @param color the text color
   * @return the button
   */
  static JButton linkButton(String text, Color color) {
    JButton link = new JButton(text);
    link.setForeground(color);
    link.setFont(link.getFont().deriveFont(
        Map.of(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
    link.setBorderPainted(false);
    link.setContentAreaFilled(false);
    link.setFocusPainted(false);
    link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    return link;
  }

  //** IBM Plex Sans Bold at the given size. */
  static Font bold(float size) {
    return SANS_BOLD.deriveFont(size);
  }
}
