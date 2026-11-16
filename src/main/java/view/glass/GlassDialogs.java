package view.glass;

import view.board.BoardTheme;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.function.Consumer;

/**
 * A shared, Swing-only modal presentation for message, confirmation and text-entry dialogs.
 * Callers still own the decisions: this class never modifies a document, workspace or file.
 * The native JFileChooser remains separate so file selection works on Windows and macOS.
 */
public final class GlassDialogs {
    private GlassDialogs() { }

    public enum Tone { INFO, WARNING, ERROR, QUESTION }

    public static void info(Component parent, String message) {
        present(parent, "Message", message, Tone.INFO, null, new String[]{"OK"});
    }

    public static void warning(Component parent, String message) {
        present(parent, "Notice", message, Tone.WARNING, null, new String[]{"OK"});
    }

    public static void error(Component parent, String message) {
        present(parent, "Error", message, Tone.ERROR, null, new String[]{"OK"});
    }

    /** Yes / No confirmation; closing or Esc is always treated as No. */
    public static boolean confirm(Component parent, String title, String message, String affirmative) {
        return present(parent, title, message, Tone.QUESTION, null,
                new String[]{Objects.requireNonNull(affirmative), "Cancel"}) == 0;
    }

    /** Returns the clicked option index, or -1 for Esc / dismissed. */
    public static int options(Component parent, String title, String message, String... options) {
        if (options == null || options.length == 0) throw new IllegalArgumentException("Options required");
        return present(parent, title, message, Tone.QUESTION, null, options);
    }

    /** Returns null for Cancel or Esc; an empty string is returned unchanged for validation by caller. */
    public static String input(Component parent, String title, String prompt, String initialValue) {
        return input(parent, title, prompt, initialValue, "OK");
    }

    public static String input(Component parent, String title, String prompt,
                               String initialValue, String affirmative) {
        GlassTextField field = new GlassTextField(24, "");
        field.setText(initialValue == null ? "" : initialValue);
        field.setFont(new Font(Font.DIALOG, Font.PLAIN, 15)); // logical font fallback for Unicode names
        int option = present(parent, title, prompt, Tone.QUESTION, field,
                new String[]{Objects.requireNonNull(affirmative), "Cancel"});
        return option == 0 ? field.getText() : null;
    }

    /** Opens caller-owned content (e.g. a saved-canvas list) without imposing a native JOptionPane. */
    public static boolean choose(Component parent, String title, JComponent content, String affirmative) {
        return choose(parent, title, content, affirmative, null);
    }

    /** Register an accept callback for double-click or Enter on caller-owned content. */
    public static boolean choose(Component parent, String title, JComponent content, String affirmative,
                                 Consumer<Runnable> registerAccept) {
        return present(parent, title, "", Tone.QUESTION, Objects.requireNonNull(content),
                new String[]{Objects.requireNonNull(affirmative), "Cancel"}, registerAccept) == 0;
    }

    public enum UnsavedDecision { DONT_SAVE, CANCEL, SAVE }

    /** Order is stable: red destructive choice, neutral Cancel, blue Save. */
    public static UnsavedDecision unsavedChanges(Component parent, String canvasName) {
        int choice = present(parent, "Save changes to “" + canvasName + "” before opening another canvas?",
                "If you don’t save, your latest drawing will be lost.", Tone.WARNING, null,
                new String[]{"Don’t Save", "Cancel", "Save"});
        return choice == 0 ? UnsavedDecision.DONT_SAVE :
                choice == 2 ? UnsavedDecision.SAVE : UnsavedDecision.CANCEL;
    }

    private static int present(Component parent, String title, String message, Tone tone,
                               JComponent content, String[] labels) {
        return present(parent, title, message, tone, content, labels, null);
    }

    private static int present(Component parent, String title, String message, Tone tone,
                               JComponent content, String[] labels, Consumer<Runnable> registerAccept) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        try {
            dialog.setBackground(new Color(0, 0, 0, 0));
        } catch (UnsupportedOperationException | IllegalArgumentException unsupported) {
            dialog.setBackground(new Color(242, 242, 247));
        }
        AtomicInteger selected = new AtomicInteger(-1);
        DialogPane pane = new DialogPane(title, message, tone, content, labels, i -> {
            selected.set(i);
            dialog.dispose();
        });
        dialog.setContentPane(pane);
        if (registerAccept != null) registerAccept.accept(() -> {
            selected.set(0);
            dialog.dispose();
        });
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-glass");
        dialog.getRootPane().getActionMap().put("close-glass", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { dialog.dispose(); }
        });
        if (!pane.buttons.isEmpty()) {
            int defaultOption = labels.length == 3 && labels[0].contains("Save") && labels[2].equals("Save")
                    ? 2 : 0;
            dialog.getRootPane().setDefaultButton(pane.buttons.get(defaultOption));
        }
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        if (content instanceof JTextField field) {
            SwingUtilities.invokeLater(() -> { field.requestFocusInWindow(); field.selectAll(); });
        }
        dialog.setVisible(true);
        return selected.get();
    }

    /** Package-visible, window-free presentation: easily tested in headless mode. */
    static final class DialogPane extends JPanel {
        private static final int SHADOW = 9;
        final List<GlassButton> buttons = new ArrayList<>();

        DialogPane(String title, String message, Tone tone, JComponent extra,
                   String[] labels, IntConsumer select) {
            super(new BorderLayout(0, 12));
            Objects.requireNonNull(tone);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(22, 24, 20, 24));
            setName("glass-dialog");

            JPanel heading = new JPanel(new BorderLayout());
            heading.setOpaque(false);
            JPanel words = new JPanel(new BorderLayout(0, 7));
            words.setOpaque(false);
            JLabel titleLabel = new JLabel(title == null ? "" : title);
            titleLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            titleLabel.setForeground(new Color(30, 41, 59));
            words.add(titleLabel, BorderLayout.NORTH);
            if (message != null && !message.isBlank()) {
                JTextArea description = new JTextArea(message);
                description.setLineWrap(true);
                description.setWrapStyleWord(true);
                description.setRows(Math.min(5, Math.max(1, message.length() / 43 + 1)));
                description.setColumns(30);
                description.setEditable(false);
                description.setFocusable(false);
                description.setOpaque(false);
                description.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
                description.setForeground(new Color(56, 65, 80));
                description.setBorder(null);
                words.add(description, BorderLayout.CENTER);
            }
            heading.add(words, BorderLayout.CENTER);
            if ((title != null && !title.isBlank()) || (message != null && !message.isBlank())) {
                add(heading, BorderLayout.NORTH);
            }
            if (extra != null) {
                JPanel inset = new JPanel(new BorderLayout());
                inset.setOpaque(false);
                inset.setBorder(BorderFactory.createEmptyBorder(2, 0, 3, 0));
                inset.add(extra);
                add(inset, BorderLayout.CENTER);
            }

            JPanel actions = new JPanel(new BorderLayout());
            actions.setOpaque(false);
            JPanel trailing = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            trailing.setOpaque(false);
            java.util.List<GlassButton> trailingButtons = new java.util.ArrayList<>();
            boolean unsaved = labels.length == 3 && labels[0].toLowerCase(java.util.Locale.ROOT)
                    .contains("save") && labels[1].equals("Cancel") && labels[2].equals("Save");
            for (int i = 0; i < labels.length; i++) {
                final int index = i;
                Style style = labels[i].equals("Cancel") ? Style.SECONDARY :
                        unsaved && i == 0 ? Style.DESTRUCTIVE :
                                (unsaved ? i == 2 : i == 0) ? Style.PRIMARY : Style.SECONDARY;
                GlassButton button = new ActionButton(labels[i], style);
                button.setName("glass-option-" + i);
                button.addActionListener(e -> select.accept(index));
                buttons.add(button);
                if (unsaved && i == 0) actions.add(button, BorderLayout.WEST);
                else trailingButtons.add(button);
            }
            if (unsaved) trailingButtons.forEach(trailing::add);
            else for (int i = trailingButtons.size()-1; i >= 0; i--) trailing.add(trailingButtons.get(i));
            actions.add(trailing, BorderLayout.EAST);
            add(actions, BorderLayout.SOUTH);
        }

        @Override public Dimension getPreferredSize() {
            Dimension natural = super.getPreferredSize();
            return new Dimension(Math.max(400, natural.width + 16), natural.height + 17);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Glass.setUp(g2);
            RoundRectangle2D shell = new RoundRectangle2D.Double(SHADOW, SHADOW / 2.0,
                    getWidth() - SHADOW * 2.0, getHeight() - SHADOW * 1.5, 27, 27);
            // Nearly opaque white body; only the outer shadow and rim stay translucent.
            // This prevents canvas strokes showing through Save As fields and text.
            g2.setColor(new Color(255, 255, 255, 253));
            g2.fill(shell);
            Glass.paint(g2, shell, null, 0, 0, 0, 1);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private enum Style { PRIMARY, SECONDARY, DESTRUCTIVE }

    /** Buttons remain GlassButton-compatible for existing callers and tests. */
    private static final class ActionButton extends GlassButton {
        private final Style style;
        ActionButton(String label, Style style) {
            super(label, 35);
            this.style = style;
            setFont(BoardTheme.font(13.5f));
        }
        @Override public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(Math.max(89, d.width), d.height);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Glass.setUp(g2);
            int inset = 3;
            int w = getWidth() - 2 * inset, h = getHeight() - 2 * inset;
            RoundRectangle2D shape = new RoundRectangle2D.Double(inset, inset, w, h, h, h);
            if (style != Style.DESTRUCTIVE) {
                g2.setColor(style == Style.PRIMARY ? new Color(0, 122, 255) : new Color(238, 238, 240));
                g2.fill(shape);
            }
            g2.setColor(style == Style.PRIMARY ? Color.WHITE :
                    style == Style.DESTRUCTIVE ? new Color(220, 38, 38) : new Color(27, 29, 33));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }
}
