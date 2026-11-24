package interface_adapter.configure_bot;

public class ConfigureState {
    private String styleName = "";
    private double aggression = 0.0;

    public ConfigureState(ConfigureState copy) {
        styleName = copy.styleName;
        aggression = copy.aggression;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public ConfigureState() {}

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setAggression(double aggression) {this.aggression = aggression; }

    public double getAggression() {
        return aggression;
    }
}
