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
    private final String hText;   // pills on the axes
    private final String vText;
    private final List<double[][]> path;

    public PhaseReadout(double h, double v, double dh, double dv, String caption, String text, String detail,
                        String hText, String vText, List<double[][]> path) {
        this.h = h;
        this.v = v;
        this.dh = dh;
        this.dv = dv;
        this.caption = caption;
        this.text = text;
        this.detail = detail;
        this.hText = hText;
        this.vText = vText;
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

    public String getHText() {
        return hText;
    }

    public String getVText() {
        return vText;
    }

    public List<double[][]> getPath() {
        return path;
    }
}
