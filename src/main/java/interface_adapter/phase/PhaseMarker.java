package interface_adapter.phase;

// an equilibrium on the card: dot at (h, v), or a dashed line v = c when line is set
public final class PhaseMarker {
    // how the card colours it: green pulls in, red pushes out, orange saddle, blue goes round
    public enum Role { ATTRACTOR, REPELLER, SADDLE, CENTER, NEUTRAL }

    private final double h;
    private final double v;
    private final boolean line;
    private final Role role;
    private final String caption;

    public PhaseMarker(double h, double v, boolean line, Role role, String caption) {
        this.h = h;
        this.v = v;
        this.line = line;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    // things settle there
    public boolean isStable() {
        return role == Role.ATTRACTOR;
    }

    public String getCaption() {
        return caption;
    }
}
