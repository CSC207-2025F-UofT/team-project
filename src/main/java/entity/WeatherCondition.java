package entity;

/**
 * The four weather conditions used by the game, each with the share of maximum energy restored after resting.
 */
public enum WeatherCondition {
    CLEAR(100),
    CLOUDY(95),
    RAIN(85),
    SNOW(80);

    private final int energyRestorePercent;

    WeatherCondition(int energyRestorePercent) {
        this.energyRestorePercent = energyRestorePercent;
    }

    public int getEnergyRestorePercent() {
        return energyRestorePercent;
    }

    /**
     * Converts a WMO weather code (as returned by Open-Meteo) into a game condition.
     * @param wmoCode the WMO weather interpretation code
     * @return the matching game condition
     */
    public static WeatherCondition fromWmoCode(int wmoCode) {
        if (wmoCode <= 1) {
            return CLEAR;
        }
        if (wmoCode <= 48) {
            return CLOUDY;
        }
        if (wmoCode >= 71 && wmoCode <= 77 || wmoCode == 85 || wmoCode == 86) {
            return SNOW;
        }
        return RAIN;
    }
}
