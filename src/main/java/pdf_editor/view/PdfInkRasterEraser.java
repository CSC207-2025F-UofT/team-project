package pdf_editor.view;

import pdf_editor.domain.PdfDocument;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/** Pixel erasure of rasterized PDF pen marks. Coordinates are in PDF page units.
 *  This rendering adapter never changes domain objects; commits are handled by the controller. */
final class PdfInkRasterEraser {
    private PdfInkRasterEraser() { }

    static boolean touches(PdfDocument.Placement p, List<Point2D.Float> path, float radius) {
        for (Point2D.Float point : path) {
            if (point.x + radius >= p.x() && point.x - radius <= p.x() + p.width()
                    && point.y + radius >= p.y() && point.y - radius <= p.y() + p.height()) return true;
        }
        return false;
    }

    static BufferedImage erase(PdfDocument.Placement p, List<Point2D.Float> path, float radius) {
        if (!touches(p, path, radius)) return null;
        BufferedImage original;
        try {
            original = ImageIO.read(new ByteArrayInputStream(p.material().imagePng()));
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read PDF ink image", ex);
        }
        if (original == null) throw new IllegalStateException("Invalid PDF ink image");
        BufferedImage out = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        try {
            g.drawImage(original, 0, 0, null);
            g.setComposite(AlphaComposite.Clear);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            double sx = original.getWidth() / p.width();
            double sy = original.getHeight() / p.height();
            // Page-to-image mapping (including placements that have been resized).
            g.scale(sx, sy);
            Path2D.Float line = new Path2D.Float();
            boolean first = true;
            for (Point2D.Float point : path) {
                if (first) { line.moveTo(point.x - p.x(), point.y - p.y()); first = false; }
                else line.lineTo(point.x - p.x(), point.y - p.y());
            }
            g.setStroke(new BasicStroke(Math.max(1, radius * 2), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(line);
            if (path.size() == 1) {
                Point2D.Float point = path.get(0);
                g.fillOval(Math.round(point.x - p.x() - radius), Math.round(point.y - p.y() - radius),
                        Math.max(1, Math.round(2 * radius)), Math.max(1, Math.round(2 * radius)));
            }
        } finally { g.dispose(); }
        return out;
    }
}
