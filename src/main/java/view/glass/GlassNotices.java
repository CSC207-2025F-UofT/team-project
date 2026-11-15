package view.glass;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

/**
 * Compact floating feedback for the one-board workspace. Presenter/UI callers supply all copy:
 * <pre>
 * GlassNotices.showSuccess(window, "Saved \"123\"");
 * GlassNotices.NoticeHandle error = GlassNotices.showError(window, "Can't calculate", "a has no value");
 * error.dismiss();
 * </pre>
 * Only the view layer handles layout and expiration; no use case or persistence code here.
 */
public final class GlassNotices {
    public static final int SUCCESS_DURATION_MS = 2_000;
    private static final String KEY = "mathlab.activeNotices";

    private GlassNotices() { }

    public interface NoticeHandle { void dismiss(); }
    public enum Kind { SUCCESS, ERROR }

    public static NoticeHandle showSuccess(Component parent, String message) {
        return show(parent, Kind.SUCCESS, "", message);
    }

    public static NoticeHandle showError(Component parent, String title, String message) {
        return show(parent, Kind.ERROR, title, message);
    }

    private static NoticeHandle show(Component parent, Kind kind, String title, String message) {
        Objects.requireNonNull(kind);
        Objects.requireNonNull(message);
        if (!SwingUtilities.isEventDispatchThread()) {
            java.util.concurrent.atomic.AtomicReference<NoticeHandle> reference = new java.util.concurrent.atomic.AtomicReference<>();
            java.util.concurrent.atomic.AtomicBoolean closed = new java.util.concurrent.atomic.AtomicBoolean();
            SwingUtilities.invokeLater(() -> {
                NoticeHandle actual = show(parent, kind, title, message);
                reference.set(actual);
                if (closed.get()) actual.dismiss();
            });
            return () -> {
                closed.set(true);
                NoticeHandle actual = reference.get();
                if (actual != null) actual.dismiss();
            };
        }
        JRootPane root = parent == null ? null : SwingUtilities.getRootPane(parent);
        if (root == null) return () -> { };
        @SuppressWarnings("unchecked")
        EnumMap<Kind, Displayed> perRoot = (EnumMap<Kind, Displayed>) root.getClientProperty(KEY);
        if (perRoot == null) {
            perRoot = new EnumMap<>(Kind.class);
            root.putClientProperty(KEY, perRoot);
        }
        Displayed previous = perRoot.get(kind);
        if (previous != null) previous.dismiss();
        NoticeView view = new NoticeView(kind, title, message);
        Displayed current = new Displayed(root, kind, view);
        perRoot.put(kind, current);
        view.onClose(current::dismiss);
        root.getLayeredPane().add(view, JLayeredPane.POPUP_LAYER);
        current.position();
        current.installResizeListener();
        if (kind == Kind.SUCCESS) current.startExpiry();
        root.getLayeredPane().revalidate();
        root.getLayeredPane().repaint();
        return current::dismiss;
    }

    private static final class Displayed {
        final JRootPane root;
        final Kind kind;
        final NoticeView view;
        Timer expiry;
        ComponentAdapter resize;
        boolean dismissed;

        Displayed(JRootPane root, Kind kind, NoticeView view) {
            this.root = root;
            this.kind = kind;
            this.view = view;
        }

        void position() {
            Dimension preferred = view.getPreferredSize();
            JLayeredPane layer = root.getLayeredPane();
            int available = layer.getWidth();
            int width = Math.min(preferred.width, Math.max(0, available - 16));
            view.setBounds(Math.max(8, (available - width) / 2),
                    kind == Kind.ERROR ? 72 : 180, width, preferred.height);
            view.revalidate();
            view.repaint();
        }

        void installResizeListener() {
            resize = new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent event) { position(); }
            };
            root.getLayeredPane().addComponentListener(resize);
        }

        void startExpiry() {
            expiry = new Timer(SUCCESS_DURATION_MS, event -> dismiss());
            expiry.setRepeats(false);
            expiry.start();
        }

        void dismiss() {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(this::dismiss);
                return;
            }
            if (dismissed) return;
            dismissed = true;
            if (expiry != null) expiry.stop();
            if (resize != null) root.getLayeredPane().removeComponentListener(resize);
            root.getLayeredPane().remove(view);
            @SuppressWarnings("unchecked")
            EnumMap<Kind, Displayed> registered = (EnumMap<Kind, Displayed>) root.getClientProperty(KEY);
            if (registered != null && registered.get(kind) == this) {
                registered.remove(kind);
                if (registered.isEmpty()) root.putClientProperty(KEY, null);
            }
            root.getLayeredPane().repaint();
        }
    }

    /** Offscreen-testable custom-drawn capsule, without JTextArea layout artefacts. */
    static final class NoticeView extends JPanel {
        private static final Font HEADING = new Font(Font.DIALOG, Font.BOLD, 17);
        private static final Font BODY = new Font(Font.DIALOG, Font.PLAIN, 14);
        private static final Font SUCCESS_TEXT = new Font(Font.DIALOG, Font.PLAIN, 16);
        final Kind kind;
        private final String title;
        private final String message;
        private final JButton closeButton;
        private Runnable close = () -> { };

        NoticeView(Kind kind, String title, String message) {
            super(null);
            this.kind = Objects.requireNonNull(kind);
            this.title = title == null ? "" : title;
            this.message = Objects.requireNonNull(message);
            setName("glass-notice-" + kind.name().toLowerCase());
            setOpaque(false);
            if (kind == Kind.ERROR) {
                closeButton = new JButton("×");
                closeButton.setName("dismiss-error-notice");
                closeButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));
                closeButton.setForeground(new Color(110, 110, 115));
                closeButton.setBorder(null);
                closeButton.setOpaque(false);
                closeButton.setFocusable(false);
                closeButton.setContentAreaFilled(false);
                closeButton.setBorderPainted(false);
                closeButton.addActionListener(e -> close.run());
                add(closeButton);
            } else {
                closeButton = null;
            }
        }

        void onClose(Runnable callback) { close = callback; }
        void layoutAll() { doLayout(); }

        @Override public Dimension getPreferredSize() {
            if (kind == Kind.SUCCESS) {
                FontMetrics metrics = getFontMetrics(SUCCESS_TEXT);
                return new Dimension(Math.max(205, Math.min(480, metrics.stringWidth(message) + 98)), 64);
            }
            FontMetrics headingMetrics = getFontMetrics(HEADING);
            int width = Math.max(430, Math.min(580, Math.max(headingMetrics.stringWidth(title) + 148,
                    getFontMetrics(BODY).stringWidth(message) + 150)));
            int count = wrap(message, getFontMetrics(BODY), width - 148).size();
            return new Dimension(width, 80 + Math.max(0, count - 1) * 19 + 12);
        }

        @Override public void doLayout() {
            if (closeButton != null) closeButton.setBounds(getWidth() - 53, (getHeight() - 47) / 2, 38, 38);
        }

        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            Glass.setUp(g);
            int w = getWidth(), h = getHeight();
            if (w < 28 || h < 28) { g.dispose(); return; }

            // Opaque floating white glass: Canvas strokes must not shine through notification text.
            double x = 5, y = 3, sw = w - 10, sh = h - 11;
            double rounding = kind == Kind.SUCCESS ? sh : 32;
            RoundRectangle2D shadow = new RoundRectangle2D.Double(x + 1, y + 5, sw, sh, rounding, rounding);
            g.setColor(new Color(15, 23, 42, 22));
            g.fill(shadow);
            RoundRectangle2D shell = new RoundRectangle2D.Double(x, y, sw, sh, rounding, rounding);
            g.setColor(new Color(255, 255, 255, 254));
            g.fill(shell);
            g.setColor(new Color(202, 207, 216, 155));
            g.setStroke(new BasicStroke(1f));
            g.draw(shell);

            int markerX = 37;
            int markerY = (int)(y + sh / 2);
            boolean success = kind == Kind.SUCCESS;
            g.setColor(success ? new Color(226, 242, 231) : new Color(255, 231, 230));
            g.fill(new Ellipse2D.Double(markerX - 18, markerY - 18, 36, 36));
            g.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(success ? new Color(25, 139, 64) : new Color(215, 30, 38));
            if (success) {
                g.drawLine(markerX - 9, markerY, markerX - 2, markerY + 7);
                g.drawLine(markerX - 2, markerY + 7, markerX + 10, markerY - 8);
            } else {
                g.drawLine(markerX, markerY - 9, markerX, markerY + 4);
                g.fillOval(markerX - 2, markerY + 9, 4, 4);
            }

            int textX = 69;
            if (success) {
                g.setFont(SUCCESS_TEXT);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(new Color(28, 28, 30));
                g.drawString(ellipsize(message, fm, Math.max(20, w - textX - 20)), textX,
                        markerY + (fm.getAscent() - fm.getDescent()) / 2);
            } else {
                g.setFont(HEADING);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(new Color(28, 28, 30));
                g.drawString(ellipsize(title, fm, Math.max(20, w - textX - 60)), textX, markerY - 3);
                g.setFont(BODY);
                FontMetrics bodyFm = g.getFontMetrics();
                g.setColor(new Color(103, 103, 109));
                int baseline = markerY + 18;
                for (String line : wrap(message, bodyFm, Math.max(30, w - textX - 58))) {
                    if (baseline > h - 13) break;
                    g.drawString(line, textX, baseline);
                    baseline += 19;
                }
            }
            g.dispose();
            super.paintComponent(graphics); // paints actual close JButton on top of the shell
        }

        private static String ellipsize(String message, FontMetrics fm, int width) {
            if (fm.stringWidth(message) <= width) return message;
            String suffix = "…";
            int end = message.length();
            while (end > 0 && fm.stringWidth(message.substring(0, end) + suffix) > width) end--;
            return message.substring(0, end).stripTrailing() + suffix;
        }

        private static List<String> wrap(String message, FontMetrics fm, int width) {
            if (message.isBlank()) return List.of("");
            ArrayList<String> lines = new ArrayList<>();
            StringBuilder current = new StringBuilder();
            for (String word : message.split("\\s+")) {
                String candidate = current.isEmpty() ? word : current + " " + word;
                if (!current.isEmpty() && fm.stringWidth(candidate) > width) {
                    lines.add(current.toString());
                    current = new StringBuilder(word);
                } else current = new StringBuilder(candidate);
            }
            if (!current.isEmpty()) lines.add(current.toString());
            return lines;
        }
    }
}
