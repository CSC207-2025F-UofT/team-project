package usecase.setriskprofile;

import java.util.List;

/**
 * Output data describing the available risk profile presets and the user's current settings.
 */
public class SetRiskProfileSettingsOutputData {
  private final List<Preset> presets;
  private final String selectedPreset;
  private final double targetGain;
  private final int maxHoldingPeriod;
  private final int window;

  /**
   * Creates a new SetRiskProfileSettingsOutputData object.
   *
   * @param presets The available risk profile presets
   * @param selectedPreset Name of the preset matching the current settings, or null if custom
   * @param targetGain Current target gain as a fraction (e.g., 20% would be 0.2)
   * @param maxHoldingPeriod Current maximum holding period in trading days
   * @param window Current past price window in months
   */
  public SetRiskProfileSettingsOutputData(List<Preset> presets, String selectedPreset,
                                          double targetGain, int maxHoldingPeriod, int window) {
    this.presets = List.copyOf(presets);
    this.selectedPreset = selectedPreset;
    this.targetGain = targetGain;
    this.maxHoldingPeriod = maxHoldingPeriod;
    this.window = window;
  }

  public List<Preset> getPresets() {
    return presets;
  }

  public String getSelectedPreset() {
    return selectedPreset;
  }

  public double getTargetGain() {
    return targetGain;
  }

  public int getMaxHoldingPeriod() {
    return maxHoldingPeriod;
  }

  public int getWindow() {
    return window;
  }

  /**
   * A risk profile preset available to the user. Instances of these objects are returned in the
   * output data to isolate business logic inside {@link entities.RiskProfilePreset} from outer
   * layers.
   */
  public static class Preset {
    private final String name;
    private final double targetGain;
    private final int maxHoldingPeriod;

    /**
     * Creates a new Preset object.
     *
     * @param name Name of the preset
     * @param targetGain Target gain as a fraction
     * @param maxHoldingPeriod Maximum holding period in trading days
     */
    public Preset(String name, double targetGain, int maxHoldingPeriod) {
      this.name = name;
      this.targetGain = targetGain;
      this.maxHoldingPeriod = maxHoldingPeriod;
    }

    public String getName() {
      return name;
    }

    public double getTargetGain() {
      return targetGain;
    }

    public int getMaxHoldingPeriod() {
      return maxHoldingPeriod;
    }
  }
}
