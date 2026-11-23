package usecase.setriskprofile;

import entities.RiskProfileSettings;
import java.io.IOException;
import java.util.Optional;

/**
 * Data access interface for persisting the user's risk profile settings between runs.
 */
public interface SetRiskProfileSettingsDataAccessInterface {
  /**
   * Loads the user's previously saved settings.
   *
   * @return The saved settings, or an empty Optional if nothing has been saved yet
   * @throws IOException If saved settings exist but could not be read
   */
  Optional<RiskProfileSettings> loadSettings() throws IOException;

  /**
   * Saves the user's settings, replacing any previously saved settings.
   *
   * @param settings The settings to save
   * @throws IOException If the settings could not be written
   */
  void saveSettings(RiskProfileSettings settings) throws IOException;
}
