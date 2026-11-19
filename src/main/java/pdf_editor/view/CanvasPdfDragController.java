package pdf_editor.view;

import view.DrawingCanvas;
import pdf_editor.adapter.SelectedInkArtwork;
import interface_adapter.history.CanvasHistorySelection;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;

/** Swing adapter that copies a previously selected ink region through native drag and drop. */
public final class CanvasPdfDragController {
    private static final int DRAG_THRESHOLD = 5;
    /** Native Swing local-JVM flavor: keep formula/result and original graph pixels. */
    public static final DataFlavor HISTORY_SELECTION_FLAVOR = new DataFlavor(
            DataFlavor.javaJVMLocalObjectMimeType + ";class=" + CanvasHistorySelection.class.getName(),
            "Canvas History selection");

    /** Canvas movement has priority until the pointer crosses the PDF boundary. */
    static boolean shouldTransfer(Point pressed, Point current, int canvasWidth) {
        return pressed != null && current != null
                && pressed.distance(current) >= DRAG_THRESHOLD
                && current.x >= canvasWidth;
    }

    private CanvasPdfDragController() { }

    public static void install(DrawingCanvas canvas) {
        Objects.requireNonNull(canvas, "canvas");
        final class SelectionTransferHandler extends TransferHandler {
            private boolean history;
            @Override protected Transferable createTransferable(JComponent source) {
                try {
                    if (history) {
                        CanvasHistorySelection payload = canvas.selectedHistoryTransfer();
                        setDragImage(translucentPreview(payload.assets().get(0).image()));
                        setDragImageOffset(new Point(-12, -12));
                        return new HistoryTransferable(payload);
                    }
                    BufferedImage image = SelectedInkArtwork.render(canvas, false);
                    setDragImage(translucentPreview(image));
                    setDragImageOffset(new Point(-12, -12));
                    return new InkTransferable(image);
                } catch (IllegalStateException noSelection) {
                    return null;
                }
            }
            @Override public int getSourceActions(JComponent source) { return COPY; }
        }
        SelectionTransferHandler handler = new SelectionTransferHandler();
        canvas.setTransferHandler(handler);
        MouseAdapter gesture = new MouseAdapter() {
            private Point pressed;
            private boolean started;

            @Override public void mousePressed(MouseEvent event) {
                pressed = null;
                started = false;
                if (event.getButton() != MouseEvent.BUTTON1) return;
                // DrawingCanvas's existing listener already selected the clicked picture.
                // A History selection wins only if the pointer is within its selection box.
                handler.history = canvas.canCopySelectedHistoryAt(event.getPoint());
                if (handler.history || canvas.canCopySelectedInkAt(event.getPoint()))
                    pressed = event.getPoint();
            }
            @Override public void mouseReleased(MouseEvent event) {
                pressed = null;
                started = false;
            }
            @Override public void mouseDragged(MouseEvent event) {
                if (started || !shouldTransfer(pressed, event.getPoint(), canvas.getWidth())) return;
                started = true;
                canvas.cancelSelectionMoveForCopy();
                handler.exportAsDrag(canvas, event, TransferHandler.COPY);
                pressed = null;
            }
        };
        canvas.addMouseListener(gesture);
        canvas.addMouseMotionListener(gesture);
    }

    /** Local History payload; unlike an ink copy it never loses LaTeX/result metadata. */
    static final class HistoryTransferable implements Transferable {
        private final CanvasHistorySelection selection;
        HistoryTransferable(CanvasHistorySelection selection) {
            this.selection = Objects.requireNonNull(selection);
        }
        @Override public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { HISTORY_SELECTION_FLAVOR };
        }
        @Override public boolean isDataFlavorSupported(DataFlavor flavor) {
            return HISTORY_SELECTION_FLAVOR.equals(flavor);
        }
        @Override public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
            return selection;
        }
    }

    /** Compact translucent ghost; the source image stays full-resolution and untouched. */
    private static BufferedImage translucentPreview(BufferedImage source) {
        double scale = Math.min(1.0, 240.0 / Math.max(source.getWidth(), source.getHeight()));
        int width = Math.max(1, (int) Math.round(source.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(source.getHeight() * scale));
        BufferedImage ghost = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = ghost.createGraphics();
        try {
            g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                    java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, .55f));
            g.drawImage(source, 0, 0, width, height, null);
        } finally { g.dispose(); }
        return ghost;
    }

    private static final class InkTransferable implements Transferable {
        private final BufferedImage image;
        InkTransferable(BufferedImage image) { this.image = image; }
        @Override public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {DataFlavor.imageFlavor};
        }
        @Override public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }
        @Override public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
            return image;
        }
    }
}
