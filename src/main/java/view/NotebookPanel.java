package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * The student's lab notebook: a rich-text page beside the lab that stays open for the whole
 * module. Supports bold, italic, underline and strikethrough, with Google Docs shortcuts.
 */
class NotebookPanel extends JPanel {
  private static final Color BACKGROUND = new Color(0xF5F5F5);
  private static final Color PLACEHOLDER = new Color(0xBDBDBD);
  /** Cmd on macOS, Ctrl elsewhere. Not from Toolkit, which throws in headless tests. */
  private static final int SHORTCUT = System.getProperty("os.name", "").startsWith("Mac")
      ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;

  // TODO: save the notes when the student exits the module, load them when it opens again and
  //  clear them on Reset (through RecordResultController, as Notebook.record("notes", html)).
  final JTextPane text = new JTextPane() {
    @Override
    protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      if (getDocument().getLength() == 0) {
        Insets insets = getInsets();
        graphics.setColor(PLACEHOLDER);
        graphics.setFont(getFont());
        graphics.drawString("Start writing here.", insets.left,
            insets.top + graphics.getFontMetrics().getAscent());
      }
    }
  };
  /** Style attribute -> its toolbar button, so the buttons can show the style at the caret. */
  private final Map<Object, JToggleButton> buttons = new LinkedHashMap<>();

  NotebookPanel() {
    super(new BorderLayout(0, 12));
    setBackground(BACKGROUND);
    setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

    JLabel heading = new JLabel("Your Lab Notebook");
    heading.setFont(Theme.bold(Theme.HEADER_SIZE));
    add(heading, BorderLayout.NORTH);

    JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    toolbar.setOpaque(false);
    Font font = Theme.SANS_REGULAR.deriveFont(Theme.BODY_SIZE);
    addStyle(toolbar, StyleConstants.Bold, "B", "Bold", KeyEvent.VK_B, 0,
        Theme.bold(Theme.BODY_SIZE));
    addStyle(toolbar, StyleConstants.Italic, "I", "Italic", KeyEvent.VK_I, 0,
        font.deriveFont(Font.ITALIC));
    addStyle(toolbar, StyleConstants.Underline, "U", "Underline", KeyEvent.VK_U, 0,
        font.deriveFont(Map.of(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
    addStyle(toolbar, StyleConstants.StrikeThrough, "S", "Strikethrough", KeyEvent.VK_X,
        InputEvent.SHIFT_DOWN_MASK,
        font.deriveFont(Map.of(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON)));
    toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, PLACEHOLDER));

    text.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
    // The editor updates its typing style on caret moves too, so read it after that.
    text.addCaretListener(event -> SwingUtilities.invokeLater(this::refreshButtons));
    JScrollPane scroll = new JScrollPane(text);
    scroll.setBorder(null);

    JPanel page = new JPanel(new BorderLayout());
    page.setBackground(Color.WHITE);
    page.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
    page.add(toolbar, BorderLayout.NORTH);
    page.add(scroll, BorderLayout.CENTER);
    add(page, BorderLayout.CENTER);
  }

  /** Adds a toolbar button and a keyboard shortcut that both toggle one text style. */
  private void addStyle(JPanel toolbar, Object style, String label, String name, int key,
      int extraModifiers, Font font) {
    AbstractAction toggle = new AbstractAction(label) {
      @Override
      public void actionPerformed(ActionEvent event) {
        toggle(style);
      }
    };
    JToggleButton button = new JToggleButton(toggle);
    button.setFont(font);
    // Not focusable, so clicking it leaves the caret and selection in the text.
    button.setFocusable(false);
    button.setToolTipText(name);
    button.getAccessibleContext().setAccessibleName(name);
    toolbar.add(button);
    buttons.put(style, button);

    text.getInputMap().put(KeyStroke.getKeyStroke(key, SHORTCUT | extraModifiers), name);
    text.getActionMap().put(name, toggle);
  }

  /**
   * Turns a style on or off for the selection, or for what is typed next if nothing is
   * selected. Like Google Docs, it turns off only if the selection already starts with it.
   */
  void toggle(Object style) {
    SimpleAttributeSet change = new SimpleAttributeSet();
    change.addAttribute(style, !Boolean.TRUE.equals(currentStyle().getAttribute(style)));
    int start = text.getSelectionStart();
    int end = text.getSelectionEnd();
    if (start != end) {
      text.getStyledDocument().setCharacterAttributes(start, end - start, change, false);
    }
    text.getInputAttributes().addAttributes(change);
    refreshButtons();
  }

  private AttributeSet currentStyle() {
    int start = text.getSelectionStart();
    return start == text.getSelectionEnd() ? text.getInputAttributes()
        : text.getStyledDocument().getCharacterElement(start).getAttributes();
  }

  private void refreshButtons() {
    AttributeSet current = currentStyle();
    buttons.forEach((style, button) ->
        button.setSelected(Boolean.TRUE.equals(current.getAttribute(style))));
  }
}
