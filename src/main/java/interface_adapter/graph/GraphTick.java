package interface_adapter.graph;

// one axis tick: where it goes + what it says
public final class GraphTick {
    private final double value;
    private final String label;

    public GraphTick(double value, String label) {
        this.value = value;
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
