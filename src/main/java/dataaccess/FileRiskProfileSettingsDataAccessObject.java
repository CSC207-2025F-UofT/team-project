package dataaccess;

import entities.RiskProfile;
import entities.RiskProfileSettings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.json.JSONException;
import org.json.JSONObject;
import usecase.setriskprofile.SetRiskProfileSettingsDataAccessInterface;

/**
 * Implementation of the Data Access Object (DAO) for the user's risk profile settings that uses a
 * JSON file to persist the data.
 */
public class FileRiskProfileSettingsDataAccessObject
        implements SetRiskProfileSettingsDataAccessInterface {
  private static final String TARGET_GAIN_KEY = "targetGain";
  private static final String MAX_HOLDING_PERIOD_KEY = "maxHoldingPeriod";
  private static final String PRICE_WINDOW_KEY = "priceWindowMonths";

  private final Path settingsFile;

  /**
   * Creates a new data access object.
   *
   * @param fileName The name of the file to store the settings
   * @throws IOException If the directory to contain the settings file could not be created
   */
  public FileRiskProfileSettingsDataAccessObject(String fileName) throws IOException {
    settingsFile = AppConfigDirectory.resolve(fileName);
  }

  @Override
  public Optional<RiskProfileSettings> loadSettings() throws IOException {
    if (!Files.exists(settingsFile)) {
      return Optional.empty();
    }

    try {
      JSONObject json = new JSONObject(Files.readString(settingsFile));
      RiskProfile profile = new RiskProfile(json.getDouble(TARGET_GAIN_KEY),
              json.getInt(MAX_HOLDING_PERIOD_KEY));
      return Optional.of(new RiskProfileSettings(profile, json.getInt(PRICE_WINDOW_KEY)));
    } catch (JSONException | IllegalArgumentException e) {
      throw new IOException("Settings file " + settingsFile + " is invalid: " + e.getMessage(), e);
    }
  }

  @Override
  public void saveSettings(RiskProfileSettings settings) throws IOException {
    JSONObject json = new JSONObject();
    json.put(TARGET_GAIN_KEY, settings.getRiskProfile().getTargetGain());
    json.put(MAX_HOLDING_PERIOD_KEY, settings.getRiskProfile().getMaxHoldingPeriod());
    json.put(PRICE_WINDOW_KEY, settings.getPriceWindowMonths());
    Files.writeString(settingsFile, json.toString(2));
  }
}
