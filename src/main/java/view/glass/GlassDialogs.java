package view.glass;

import view.board.BoardTheme;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

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
        int option = present(parent, title, prompt, Tone.QUESTION, field,
                new String[]{Objects.requireNonNull(affirmative), "Cancel"});
        return option == 0 ? field.getText() : null;
    }

    /** Opens caller-owned content (e.g. a saved-canvas list) without imposing a native JOptionPane. */
    public static boolean choose(Component parent, String title, JComponent content, String affirmative) {
        return present(parent, title, "", Tone.QUESTION, Objects.requireNonNull(content),
                new String[]{Objects.requireNonNull(affirmative), "Cancel"}) == 0;
    }

    private static int present(Component parent, String title, String message, Tone tone,
                               JComponent content, String[] labels) {
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
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-glass");
        dialog.getRootPane().getActionMap().put("close-glass", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { dialog.dispose(); }
        });
        if (!pane.buttons.isEmpty()) dialog.getRootPane().setDefaultButton(pane.buttons.get(0));
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
        private final Tone tone;
        final List<GlassButton> buttons = new ArrayList<>();

        DialogPane(String title, String message, Tone tone, JComponent extra,
                   String[] labels, IntConsumer select) {
            super(new BorderLayout(0, 12));
            this.tone = Objects.requireNonNull(tone);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(22, 24, 20, 24));
            setName("glass-dialog");

            JPanel heading = new JPanel(new BorderLayout(12, 0));
            heading.setOpaque(false);
            heading.add(new ToneIcon(tone), BorderLayout.WEST);
            JPanel words = new JPanel(new BorderLayout(0, 7));
            words.setOpaque(false);
            JLabel titleLabel = new JLabel(title == null ? "" : title);
            titleLabel.setFont(BoardTheme.font(15).deriveFont(Font.BOLD));
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
                description.setFont(BoardTheme.font(13));
                description.setForeground(new Color(56, 65, 80));
                description.setBorder(null);
                words.add(description, BorderLayout.CENTER);
            }
            heading.add(words, BorderLayout.CENTER);
            add(heading, BorderLayout.NORTH);
            if (extra != null) {
                JPanel inset = new JPanel(new BorderLayout());
                inset.setOpaque(false);
                inset.setBorder(BorderFactory.createEmptyBorder(2, 0, 3, 0));
                inset.add(extra);
                add(inset, BorderLayout.CENTER);
            }

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actions.setOpaque(false);
            for (int i = labels.length - 1; i >= 0; i--) {
                final int option = i;
                GlassButton button = new GlassButton(labels[i], 34);
                button.setName("glass-option-" + i);
                button.setForeground(i == 0 ? new Color(0, 97, 206) : new Color(58, 66, 80));
                button.addActionListener(e -> select.accept(option));
                actions.add(button);
                buttons.add(0, button);
            }
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
            g2.setColor(new Color(255, 255, 255, 235));
            g2.fill(shell);
            Glass.paint(g2, shell, null, 0, 0, 0, 1);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class ToneIcon extends JComponent {
        private final Tone tone;
        private ToneIcon(Tone tone) {
            this.tone = tone;
            setPreferredSize(new Dimension(35, 35));
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Glass.setUp(g2);
            g2.translate(getWidth() / 2.0, getHeight() / 2.0);
            Color c = switch (tone) {
                case ERROR -> new Color(229, 57, 53);
                case WARNING -> new Color(245, 158, 11);
                case INFO -> new Color(0, 122, 255);
                case QUESTION -> new Color(99, 102, 241);
            };
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 38));
            g2.fill(new Ellipse2D.Double(-15, -15, 30, 30));
            g2.setColor(c);
            g2.setFont(BoardTheme.font(16).deriveFont(Font.BOLD));
            String sign = tone == Tone.ERROR ? "!" : tone == Tone.QUESTION ? "?" : tone == Tone.WARNING ? "!" : "i";
            int width = g2.getFontMetrics().stringWidth(sign);
            g2.drawString(sign, -width / 2.0f, 6);
            g2.dispose();
        }
    }
}
