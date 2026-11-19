package interface_adapter.phase;

import java.util.List;

// cursor readout, already worded: caption (what it is), text (the point), detail (the arrow / slope)
public final class PhaseReadout {
    private final double h;
    private final double v;
    private final double dh;
    private final double dv;
    private final String caption;
    private final String text;
    private final String detail;
    private final List<double[][]> path;

    public PhaseReadout(double h, double v, double dh, double dv, String caption, String text, String detail,
                        List<double[][]> path) {
        this.h = h;
        this.v = v;
        this.dh = dh;
        this.dv = dv;
        this.caption = caption;
        this.text = text;
        this.detail = detail;
        this.path = List.copyOf(path);
    }

    public double getH() {
        return h;
    }

    public double getV() {
        return v;
    }

    public double getDh() {
        return dh;
    }

    public double getDv() {
        return dv;
    }

    public String getCaption() {
        return caption;
    }

    public String getText() {
        return text;
    }

    public String getDetail() {
        return detail;
    }

    public List<double[][]> getPath() {
        return path;
    }
}
