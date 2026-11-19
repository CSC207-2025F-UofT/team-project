package view.board;

import interface_adapter.history.HistoryItem;
import interface_adapter.history.HistoryViewModel;
import entity.EquationDisplay;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import view.answer.AnswerFont;
import rendering.TransparentArtwork;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/** Past calculations, newest first; drag a row onto the canvas to drop a copy there. */
public class HistoryPanel extends JPanel {
    static final int ROW_H = 56;
    private static final int TOP_ROOM = ToolbarSpot.TOP_Y;  // under the History capsule
    private static final float TEX_SIZE = 18f;
    private static final double DIGIT_H = 15;  // answer "0" this tall, like the formula
    private static final int CONTENT_X = 40;
    private static final int PICTURE_SCALE = 2;

    /** The board shows a ghost under the mouse and takes the drop. */
    public interface DragHost {
        void dragging(BufferedImage picture, Point screenTopLeft);

        // true if it landed on the canvas
        boolean dropped(BufferedImage picture, Point screenTopLeft);
        default boolean dropped(HistoryItem item, BufferedImage picture, Point screenTopLeft) {
            return dropped(picture, screenTopLeft);
        }
        default void importIntoPdf(HistoryItem item) { }
        default void importIntoLibrary(HistoryItem item) { }
        /** Drop target is determined by cursor, not by the artwork's offset top-left. */
        default boolean droppedAt(HistoryItem item, BufferedImage picture,
                                  Point previewTopLeft, Point cursorScreen) {
            return dropped(item, picture, previewTopLeft);
        }
        default void deleteHistory(HistoryItem item) { }
        /** Cancel a lost mouse-release gesture without creating a dropped item. */
        default void cancelDrag() { }
    }

    private final JPanel list = new JPanel();
    private final DragHost host;

    public HistoryPanel(HistoryViewModel viewModel, DragHost host) {
        this(viewModel, host, () -> { });
    }
    public HistoryPanel(HistoryViewModel viewModel, DragHost host, Runnable undoDelete) {
        super(new BorderLayout());
        this.host = host;
        setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(TOP_ROOM + 4, 16, 8, 10));
        JLabel title = new JLabel("Saved calculations");
        title.setFont(BoardTheme.font(13f));
        header.add(title, BorderLayout.WEST);
        JButton undo = new JButton("Undo delete");
        undo.setFont(BoardTheme.font(11f));
        undo.setFocusPainted(false);
        undo.setContentAreaFilled(false);
        undo.setBorder(BorderFactory.createEmptyBorder(4, 7, 4, 7));
        undo.addActionListener(e -> undoDelete.run());
        header.add(undo, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        viewModel.addPropertyChangeListener(e -> {
            if (HistoryViewModel.ITEMS_PROPERTY.equals(e.getPropertyName())) {
                SwingUtilities.invokeLater(() -> show(viewModel.getItems()));
            }
        });
        show(viewModel.getItems());
    }

    private void show(List<HistoryItem> items) {
        list.removeAll();
        if (items.isEmpty()) {
            JLabel empty = new JLabel("Calculations show up here.");
            empty.setFont(BoardTheme.font(14f));
            empty.setForeground(BoardTheme.LABEL_DISABLED);
            list.add(empty);
        }
        for (HistoryItem item : items) {
            list.add(new Row(item));
            list.add(Box.createVerticalStrut(10));
        }
        list.revalidate();
        list.repaint();
    }

    int rowCount() {
        int n = 0;
        for (java.awt.Component c : list.getComponents()) {
            if (c instanceof Row) {
                n++;
            }
        }
        return n;
    }

    // formula (typeset) and "= answer" in Kalam, no background, PICTURE_SCALE x
    static BufferedImage picture(HistoryItem item) {
        if (item.isGraph()) {
            try {
                // Keep the original full-resolution PNG for drag and Canvas placement.
                // The History row scales it only at paint time; never rasterize a
                // tiny thumbnail and later enlarge that thumbnail into pixels.
                BufferedImage original = javax.imageio.ImageIO.read(
                        new java.io.ByteArrayInputStream(item.imagePng()));
                if (original != null) return TransparentArtwork.removeWhiteMatte(original);
            } catch (java.io.IOException badImage) { /* fall through to formula */ }
        }
        TeXIcon icon = typeset(HistoryAnswerArtwork.formulaLatex(item));
        Rectangle2D formula = formulaBox(item, icon);
        double answerWidth = HistoryAnswerArtwork.width(item, answerSize());
        double answerHeight = HistoryAnswerArtwork.height(item, answerSize());
        int w = (int) Math.ceil(formula.getWidth() + (answerWidth > 0 ? GAP + answerWidth : 0)) + 8;
        int h = (int) Math.ceil(Math.max(formula.getHeight(), answerHeight)) + 8;
        BufferedImage img = new BufferedImage(w * PICTURE_SCALE, h * PICTURE_SCALE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.scale(PICTURE_SCALE, PICTURE_SCALE);
        paintContent(g, item, icon, 4, h);
        g.dispose();
        return img;
    }

    private static BufferedImage graphPreview(BufferedImage original) {
        double factor = Math.min(440.0 / original.getWidth(), 84.0 / original.getHeight());
        int w = Math.max(1, (int) Math.round(original.getWidth() * factor));
        int h = Math.max(1, (int) Math.round(original.getHeight() * factor));
        BufferedImage preview = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = preview.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(original, 0, 0, w, h, null);
        g.dispose();
        return preview;
    }

    private static final int GAP = 10;

    private static float answerSize() {
        return AnswerFont.sizeFor(DIGIT_H);
    }

    private static Rectangle2D formulaBox(HistoryItem item, TeXIcon icon) {
        if (icon != null) {
            return new Rectangle2D.Double(0, 0, icon.getIconWidth(), icon.getIconHeight());
        }
        return AnswerFont.visualBounds(HistoryAnswerArtwork.formulaLatex(item).replace("\\Rightarrow", " =>"), answerSize());
    }

    // formula then answer, vertically centred in height h
    private static void paintContent(Graphics2D g, HistoryItem item, TeXIcon icon, int x, int h) {
        g.setColor(Color.BLACK);
        Rectangle2D formula = formulaBox(item, icon);
        if (icon != null) {
            icon.setForeground(Color.BLACK);
            icon.paintIcon(null, g, x, (int) Math.round((h - formula.getHeight()) / 2));
        } else {
            AnswerFont.draw(g, HistoryAnswerArtwork.formulaLatex(item).replace("\\Rightarrow", " =>"), answerSize(), x, (h - formula.getHeight()) / 2);
        }
        if (HistoryAnswerArtwork.width(item, answerSize()) > 0) {
            HistoryAnswerArtwork.paint(g, item, answerSize(), x + formula.getWidth() + GAP, h / 2.0);
        }
    }

    private static TeXIcon typeset(String latex) {
        try {
            return new TeXFormula(EquationDisplay.typeset(latex)).createTeXIcon(TeXConstants.STYLE_DISPLAY, TEX_SIZE);
        } catch (RuntimeException | Error bad) {
            return null;
        }
    }

    /** The same translucent context surface used by PDF materials on all platforms. */
    GlassContextMenu contextMenu(HistoryItem item) {
        GlassContextMenu menu = new GlassContextMenu();
        menu.addAction("Add to Library", () -> host.importIntoLibrary(item));
        menu.addAction("Add to PDF", () -> host.importIntoPdf(item));
        menu.addAction("Delete from History", () -> host.deleteHistory(item));
        return menu;
    }

    // Transparent material row; drag the artwork from anywhere in the row
    private final class Row extends JComponent {
        private final HistoryItem item;
        private final TeXIcon icon;
        private final BufferedImage graphImage;
        private BufferedImage picture;  // made on press
        private BufferedImage dragged;  // full-resolution payload while dragging
        private BufferedImage dragPreview;  // only the screen ghost uses this low-cost preview
        private Point grab;
        private java.awt.event.AWTEventListener releaseWatcher;

        private void watchExternalRelease() {
            if (releaseWatcher != null) return;
            releaseWatcher = event -> {
                if (event instanceof MouseEvent mouse && mouse.getID() == MouseEvent.MOUSE_DRAGGED
                        && mouse.getSource() != Row.this && dragged != null && dragPreview != null) {
                    Point screen = mouse.getLocationOnScreen();
                    host.dragging(dragPreview, new Point(screen.x - grab.x, screen.y - grab.y));
                } else if (event instanceof MouseEvent mouse && mouse.getID() == MouseEvent.MOUSE_RELEASED
                        && SwingUtilities.isLeftMouseButton(mouse)) {
                    // AWT can deliver the release to the Canvas, a popup or the window
                    // instead of the original History row (notably on macOS).
                    Point screen = mouse.getLocationOnScreen();
                    SwingUtilities.invokeLater(() -> finishDrag(screen));
                } else if (event instanceof java.awt.event.WindowEvent window
                        && (window.getID() == java.awt.event.WindowEvent.WINDOW_DEACTIVATED
                            || window.getID() == java.awt.event.WindowEvent.WINDOW_LOST_FOCUS)) {
                    SwingUtilities.invokeLater(this::cancelDrag);
                }
            };
            java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(releaseWatcher,
                    java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.WINDOW_EVENT_MASK
                            | java.awt.AWTEvent.WINDOW_FOCUS_EVENT_MASK);
        }

        private void stopWatchingRelease() {
            if (releaseWatcher != null) {
                java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener(releaseWatcher);
                releaseWatcher = null;
            }
        }

        private void finishDrag(Point releaseScreen) {
            BufferedImage payload = dragged;
            Point startingGrab = grab;
            // Clear the view even if the host rejects the drop or throws.
            dragged = null;
            grab = null;
            dragPreview = null;
            picture = null;
            stopWatchingRelease();
            try {
                if (payload != null && startingGrab != null) {
                    host.droppedAt(item, payload,
                            new Point(releaseScreen.x - startingGrab.x,
                                    releaseScreen.y - startingGrab.y), releaseScreen);
                }
            } finally {
                host.cancelDrag();
                repaint();
            }
        }

        private void cancelDrag() {
            dragged = null;
            grab = null;
            picture = null;
            dragPreview = null;
            stopWatchingRelease();
            host.cancelDrag();
            repaint();
        }

        Row(HistoryItem item) {
            this.item = item;
            this.icon = item.isGraph() ? null : typeset(HistoryAnswerArtwork.formulaLatex(item));
            this.graphImage = item.isGraph() ? picture(item) : null;
            setToolTipText("Drag to Canvas / PDF. Right-click for Library, PDF or Delete actions.");
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentX(LEFT_ALIGNMENT);
            MouseAdapter m = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e) && e.getX() <= getWidth() - 30) {
                        picture = graphImage != null ? graphImage : picture(item);
                        dragPreview = graphImage == null ? picture : graphPreview(graphImage);
                        int previewHeight = dragPreview.getHeight() / PICTURE_SCALE;
                        grab = new Point(e.getX() - CONTENT_X, e.getY() - (ROW_H - previewHeight) / 2);
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (grab == null) {
                        return;
                    }
                    if (dragged == null) {
                        dragged = picture;
                        watchExternalRelease();
                        repaint();
                    }
                    host.dragging(dragPreview, topLeft(e));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (grab == null) return;
                    finishDrag(e.getLocationOnScreen());
                }
            };
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e) && e.getX() > getWidth() - 30) {
                        host.deleteHistory(item);
                        return;
                    }
                    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) host.importIntoPdf(item);
                }
                @Override public void mousePressed(MouseEvent e) { popup(e); }
                @Override public void mouseReleased(MouseEvent e) { popup(e); }
                private void popup(MouseEvent e) {
                    if (!e.isPopupTrigger()) return;
                    contextMenu(item).show(e.getComponent(), e.getX(), e.getY());
                }
            });
            addMouseListener(m);
            addMouseMotionListener(m);
            addHierarchyListener(event -> {
                if (!isShowing() && releaseWatcher != null) cancelDrag();
            });
        }

        private Point topLeft(MouseEvent e) {
            Point p = e.getLocationOnScreen();
            return new Point(p.x - grab.x, p.y - grab.y);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, ROW_H);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, ROW_H);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            RoundRectangle2D cap = new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, ROW_H - 1, ROW_H, ROW_H);
            if (dragged != null) {  // dashed spot left behind
                g2.setColor(BoardTheme.LABEL_DISABLED);
                g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f,
                        new float[]{5f, 4f}, 0f));
                g2.draw(cap);
                g2.dispose();
                return;
            }
            // A History item is transparent: no white capsule or its drop shadow.
            g2.setColor(BoardTheme.LABEL_DISABLED);
            Graphics2D grip = (Graphics2D) g2.create();
            grip.translate(20, ROW_H / 2.0);
            Icons.grip(grip);
            grip.dispose();
            g2.clipRect(CONTENT_X, 0, getWidth() - CONTENT_X - ROW_H / 2, ROW_H);
            if (item.isGraph()) {
                BufferedImage graph = graphImage;
                double ratio = Math.min(220.0 / graph.getWidth(), 42.0 / graph.getHeight());
                int dw = Math.max(1, (int) Math.round(graph.getWidth() * ratio));
                int dh = Math.max(1, (int) Math.round(graph.getHeight() * ratio));
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.drawImage(graph, CONTENT_X, (ROW_H - dh) / 2, dw, dh, null);
            } else {
                paintContent(g2, item, icon, CONTENT_X, ROW_H);
            }
            // Subtle delete affordance at the trailing edge, never part of the drag payload.
            g2.setClip(null);
            g2.setColor(new Color(130, 139, 153));
            g2.setFont(BoardTheme.font(16f));
            g2.drawString("×", getWidth() - 23, ROW_H / 2 + 5);
            g2.dispose();
        }
    }
}
