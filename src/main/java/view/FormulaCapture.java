package view;

import java.awt.image.BufferedImage;

// one formula's picture, tagged so a late result can be spotted
public final class FormulaCapture {
    private final long id;
    private final long revision;
    private final BufferedImage image;
    private final BufferedImage withoutEquals;  // ink minus a trailing "=", null if none

    public FormulaCapture(long id, long revision, BufferedImage image) {
        this(id, revision, image, null);
    }

    public FormulaCapture(long id, long revision, BufferedImage image, BufferedImage withoutEquals) {
        this.id = id;
        this.revision = revision;
        this.image = image;
        this.withoutEquals = withoutEquals;
    }

    public long getId() {
        return id;
    }

    public long getRevision() {
        return revision;
    }

    public BufferedImage getImage() {
        return image;
    }

    // null = ink doesn't end in an "="
    public BufferedImage getWithoutEquals() {
        return withoutEquals;
    }
}
