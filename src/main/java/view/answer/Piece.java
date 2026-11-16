package view.answer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

// bit of answer text at (x, y) off the baseline start; a √ may get stretched + a bar
final class Piece {
    final TextLayout layout;
    final int start;  // chars of the plain text
    final int end;
    final float size;
    private final double x;
    private final double y;
    private AffineTransform stretch;
    private Shape bar;

    Piece(TextLayout layout, int start, int end, float size, double x, double y) {
        this.layout = layout;
        this.start = start;
        this.end = end;
        this.size = size;
        this.x = x;
        this.y = y;
    }

    // √ taller than the font made it, bottom stays put
    void stretchUpTo(double top) {
        Rectangle2D glyph = ink();
        double bottom = glyph.getMaxY();
        stretch = new AffineTransform();
        stretch.translate(0, bottom);
        stretch.scale(1, (bottom - top) / (bottom - glyph.getY()));
        stretch.translate(0, -bottom);
    }

    void setBar(Shape bar) {
        this.bar = bar;
    }

    Rectangle2D ink() {
        Rectangle2D ink = layout.getBounds();
        ink.setRect(ink.getX() + x, ink.getY() + y, ink.getWidth(), ink.getHeight());
        if (stretch != null) {
            ink = stretch.createTransformedShape(ink).getBounds2D();
        }
        return bar == null ? ink : ink.createUnion(bar.getBounds2D());
    }

    void appendTo(Path2D all) {
        if (bar == null) {
            all.append(glyphs(), false);
            return;
        }
        Area joined = new Area(glyphs());  // bar winds the other way, a plain append punches a hole at the tip
        joined.add(new Area(bar));
        all.append(joined, false);
    }

    void draw(Graphics2D g, double dx, double dy) {
        if (stretch == null) {
            layout.draw(g, (float) (dx + x), (float) (dy + y));
        } else {
            g.fill(AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(glyphs()));
        }
        if (bar != null) {
            g.fill(AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(bar));
        }
    }

    private Shape glyphs() {
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        if (stretch != null) {
            at.preConcatenate(stretch);
        }
        return layout.getOutline(at);
    }
}
