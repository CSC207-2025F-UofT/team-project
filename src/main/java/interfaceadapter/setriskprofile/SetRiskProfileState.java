package interfaceadapter.setriskprofile;

import java.util.ArrayList;
import java.util.List;

/**
 * The state shown by the Set Risk Profile view. All values are already formatted for display.
 */
public class SetRiskProfileState {
  private List<PresetOption> presets = new ArrayList<>();
  private int selectedPresetIndex = -1;
  private double targetGainPercent;
  private int maxHoldingPeriod;
  private int windowMonths;

  private String resultTicker = "";
  private String buyPrice = "";
  private String sellDate = "";
  private String sellPrice = "";
  private String priceRange = "";
  private String returns = "";
  private String targetMessage = "";
  private boolean targetReached;

  private String errorMessage;

  public List<PresetOption> getPresets() {
    return presets;
  }

  public void setPresets(List<PresetOption> presets) {
    this.presets = List.copyOf(presets);
  }

  /**
   * Returns the index of the selected preset in {@link #getPresets()}, or -1 for a custom profile.
   *
   * @return The selected preset index
   */
  public int getSelectedPresetIndex() {
    return selectedPresetIndex;
  }

  public void setSelectedPresetIndex(int selectedPresetIndex) {
    this.selectedPresetIndex = selectedPresetIndex;
  }

  public double getTargetGainPercent() {
    return targetGainPercent;
  }

  public void setTargetGainPercent(double targetGainPercent) {
    this.targetGainPercent = targetGainPercent;
  }

  public int getMaxHoldingPeriod() {
    return maxHoldingPeriod;
  }

  public void setMaxHoldingPeriod(int maxHoldingPeriod) {
    this.maxHoldingPeriod = maxHoldingPeriod;
  }

  public int getWindowMonths() {
    return windowMonths;
  }

  public void setWindowMonths(int windowMonths) {
    this.windowMonths = windowMonths;
  }

  public String getResultTicker() {
    return resultTicker;
  }

  public void setResultTicker(String resultTicker) {
    this.resultTicker = resultTicker;
  }

  public String getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(String buyPrice) {
    this.buyPrice = buyPrice;
  }

  public String getSellDate() {
    return sellDate;
  }

  public void setSellDate(String sellDate) {
    this.sellDate = sellDate;
  }

  public String getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(String sellPrice) {
    this.sellPrice = sellPrice;
  }

  public String getPriceRange() {
    return priceRange;
  }

  public void setPriceRange(String priceRange) {
    this.priceRange = priceRange;
  }

  public String getReturns() {
    return returns;
  }

  public void setReturns(String returns) {
    this.returns = returns;
  }

  public String getTargetMessage() {
    return targetMessage;
  }

  public void setTargetMessage(String targetMessage) {
    this.targetMessage = targetMessage;
  }

  public boolean isTargetReached() {
    return targetReached;
  }

  public void setTargetReached(boolean targetReached) {
    this.targetReached = targetReached;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * A risk profile preset as shown in the view.
   */
  public static class PresetOption {
    private final String label;
    private final double targetGainPercent;
    private final int maxHoldingPeriod;

    /**
     * Creates a new PresetOption.
     *
     * @param label Display label of the preset
     * @param targetGainPercent Target gain in percent (e.g., 20% would be 20.0)
     * @param maxHoldingPeriod Maximum holding period in trading days
     */
    public PresetOption(String label, double targetGainPercent, int maxHoldingPeriod) {
      this.label = label;
      this.targetGainPercent = targetGainPercent;
      this.maxHoldingPeriod = maxHoldingPeriod;
    }

    public String getLabel() {
      return label;
    }

    public double getTargetGainPercent() {
      return targetGainPercent;
    }

    public int getMaxHoldingPeriod() {
      return maxHoldingPeriod;
    }
  }
}
