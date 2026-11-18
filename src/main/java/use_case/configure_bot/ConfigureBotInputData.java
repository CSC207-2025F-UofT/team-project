package use_case.configure_bot;

/**
 * The input data for the Configure Bot Use Case.
 */
public class ConfigureBotInputData {

    private final String styleName;
    private final double aggression;

    public ConfigureBotInputData(String styleName, double aggression) {
        this.styleName = styleName;
        this.aggression = aggression;
    }

    String getStyleName() {
        return styleName;
    }

    double getAggression() {
        return aggression;
    }

}
