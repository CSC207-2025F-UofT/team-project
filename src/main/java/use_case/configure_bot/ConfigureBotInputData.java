package use_case.configure_bot;

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
