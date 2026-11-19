package pdf_editor.view;

import entity.Stroke;
import pdf_editor.adapter.SelectedInkRegistry;
import pdf_editor.adapter.SelectedInkArtwork;
import pdf_editor.use_case.SelectionToLibrary;
import view.DrawingCanvas;
import view.glass.GlassDialogs;
import javax.swing.*;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

/** View-only context commands; the use case validates and publishes the result. */
public final class CanvasLibrarySelectionMenu {
    private CanvasLibrarySelectionMenu() { }
    public static void install(DrawingCanvas canvas, SelectionToLibrary action, SelectedInkRegistry registry) {
        Objects.requireNonNull(canvas); Objects.requireNonNull(action); Objects.requireNonNull(registry);
        canvas.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent event) { popup(event); }
            @Override public void mouseReleased(MouseEvent event) { popup(event); }
            private void popup(MouseEvent event) {
                if (!event.isPopupTrigger() || canvas.selectedInkSnapshot().isEmpty()) return;
                GlassSelectionMenu menu = new GlassSelectionMenu();
                JButton formula = menu.addAction("Recognize selection → Library");
                formula.addActionListener(e -> {
                    List<Stroke> strokes = canvas.selectedInkSnapshot();
                    BufferedImage image = canvas.selectedInkImage();
                    String selectedResult = canvas.selectedAnswer();
                    formula.setEnabled(false);
                    new SwingWorker<String, Void>() {
                        @Override protected String doInBackground() throws Exception { return action.recognize(image); }
                        @Override protected void done() {
                            try {
                                String latex = get();
                                if (canvas.containsAllInk(strokes)) {
                                    // The answer was captured with the original selection before the worker started.
                                    registry.track((selectedResult.isBlank() ? action.addFormula(latex)
                                            : action.addCalculation(latex, selectedResult)).id(), strokes);
                                }
                            } catch (Exception failure) {
                                GlassDialogs.error(canvas, "Recognition failed: " +
                                        (failure.getCause() == null ? failure.getMessage() : failure.getCause().getMessage()));
                            }
                        }
                    }.execute();
                });
                JButton ink = menu.addAction("Add selected ink image → Library");
                ink.addActionListener(e -> {
                    List<Stroke> strokes = canvas.selectedInkSnapshot();
                    if (!strokes.isEmpty()) {
                        registry.track(action.addInk(SelectedInkArtwork.render(canvas, true)).id(), strokes);
                    }
                });
                menu.show(canvas, event.getX(), event.getY());
            }
        });
    }

    /** View-only menu: identical on Windows and macOS, no native JMenuItem styling. */
    private static final class GlassSelectionMenu extends JPopupMenu {
        private static final int ROW_HEIGHT = 39;
        private static final int INSET = 6;
        private static final int WIDTH = 250;
        private final JPanel surface = new JPanel(null) {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth() - 1;
                int h = getHeight() - 1;
                g.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 252),
                        0, getHeight(), new Color(240, 244, 252, 249)));
                g.fillRoundRect(0, 0, w, h, 18, 18);
                g.setStroke(new BasicStroke(1f));
                g.setColor(new Color(187, 198, 214, 180));
                g.drawRoundRect(0, 0, w, h, 18, 18);
                g.dispose();
            }
        };

        GlassSelectionMenu() {
            setUI(new BasicPopupMenuUI());
            setOpaque(false);
            setBackground(new Color(0, 0, 0, 0));
            view.glass.GlassPopups.useTransparentPopup(this);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 8, 5));
            surface.setOpaque(false);
            surface.setPreferredSize(new Dimension(WIDTH, INSET * 2 + ROW_HEIGHT * 2));
            add(surface);
        }

        JButton addAction(String title) {
            int row = surface.getComponentCount();
            JButton button = new GlassAction(title);
            button.setBounds(INSET, INSET + row * ROW_HEIGHT,
                    WIDTH - 2 * INSET, ROW_HEIGHT);
            button.addActionListener(e -> setVisible(false));
            surface.add(button);
            return button;
        }

        private static final class GlassAction extends JButton {
            GlassAction(String title) {
                super(title);
                setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
                setFocusable(false);
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setBorder(BorderFactory.createEmptyBorder());
                setRolloverEnabled(true);
            }

            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g.setColor(getModel().isPressed() ? new Color(210, 224, 247)
                            : new Color(228, 238, 253));
                    g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
                }
                g.setColor(isEnabled() ? new Color(35, 43, 56) : new Color(145, 151, 161));
                FontMetrics fm = g.getFontMetrics(getFont());
                g.setFont(getFont());
                g.drawString(getText(), 12, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g.dispose();
            }
        }
    }

}
