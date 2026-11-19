package interface_adapter.phase;

// an equilibrium on the card: dot at (h, v), or a dashed line v = c when line is set
public final class PhaseMarker {
    private final double h;
    private final double v;
    private final boolean line;
    private final boolean stable;
    private final String caption;

    public PhaseMarker(double h, double v, boolean line, boolean stable, String caption) {
        this.h = h;
        this.v = v;
        this.line = line;
        this.stable = stable;
        this.caption = caption;
    }

    public double getH() {
        return h;
    }

    public double getV() {
        return v;
    }

    public boolean isLine() {
        return line;
    }

    // filled dot = things settle there, hollow = they leave
    public boolean isStable() {
        return stable;
    }

    public String getCaption() {
        return caption;
    }
}
