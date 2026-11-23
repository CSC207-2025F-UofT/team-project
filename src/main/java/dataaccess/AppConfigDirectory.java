package dataaccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Locates the directory used to store the app's configuration/preference files.
 */
final class AppConfigDirectory {
  private static final String APP_NAME = "JavaStockTracker";

  private AppConfigDirectory() {}

  /**
   * Returns the directory used to store app-specific configuration/preference files, following
   * standard OS conventions on Windows, macOS, and Linux.
   *
   * @return The OS-specific configuration directory
   */
  static Path getPath() {
    String os = System.getProperty("os.name").toLowerCase();
    String userHome = System.getProperty("user.home");

    if (os.contains("win")) {
      // Windows: %APPDATA%\AppName
      String appData = System.getenv("APPDATA");
      return Path.of(appData != null ? appData : userHome, APP_NAME);
    } else if (os.contains("mac")) {
      // macOS: ~/Library/Application Support/AppName
      return Path.of(userHome, "Library", "Application Support", APP_NAME);
    } else {
      // Linux/Unix (XDG Base Directory spec): ~/.config/appname
      String xdgConfig = System.getenv("XDG_CONFIG_HOME");
      return xdgConfig != null
              ? Path.of(xdgConfig, APP_NAME.toLowerCase())
              : Path.of(userHome, ".config", APP_NAME.toLowerCase());
    }
  }

  /**
   * Resolves a file inside the configuration directory, creating the directory if needed.
   *
   * @param fileName Name of the file
   * @return Path to the file inside the configuration directory
   * @throws IOException If the configuration directory could not be created
   */
  static Path resolve(String fileName) throws IOException {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("File name cannot be empty");
    }

    Path configDir = getPath();
    Files.createDirectories(configDir);
    return configDir.resolve(fileName);
  }
}
