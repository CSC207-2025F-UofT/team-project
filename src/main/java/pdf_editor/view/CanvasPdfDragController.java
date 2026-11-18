package pdf_editor.view;

import view.DrawingCanvas;
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

    private CanvasPdfDragController() { }

    public static void install(DrawingCanvas canvas) {
        Objects.requireNonNull(canvas, "canvas");
        TransferHandler handler = new TransferHandler() {
            @Override protected Transferable createTransferable(JComponent source) {
                BufferedImage image;
                try { image = canvas.selectedInkTransparentImage(); }
                catch (IllegalStateException noSelection) { return null; }
                // The same translucent preview follows the pointer during a native drag.
                setDragImage(translucentPreview(image));
                setDragImageOffset(new Point(-12, -12));
                return new InkTransferable(image);
            }
            @Override public int getSourceActions(JComponent source) { return COPY; }
        };
        canvas.setTransferHandler(handler);
        MouseAdapter gesture = new MouseAdapter() {
            private Point pressed;
            private boolean started;

            @Override public void mousePressed(MouseEvent event) {
                pressed = event.getButton() == MouseEvent.BUTTON1
                        && canvas.canCopySelectedInkAt(event.getPoint()) ? event.getPoint() : null;
                started = false;
            }
            @Override public void mouseReleased(MouseEvent event) {
                pressed = null;
                started = false;
            }
            @Override public void mouseDragged(MouseEvent event) {
                if (pressed == null || started || pressed.distance(event.getPoint()) < DRAG_THRESHOLD) return;
                started = true;
                canvas.cancelSelectionMoveForCopy();
                handler.exportAsDrag(canvas, event, TransferHandler.COPY);
                pressed = null;
            }
        };
        canvas.addMouseListener(gesture);
        canvas.addMouseMotionListener(gesture);
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
