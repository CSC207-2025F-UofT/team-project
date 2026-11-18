package dataaccess;

import entities.Stock;
import entities.Watchlist;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.json.JSONArray;

/**
 * Implementation of the Data Access Object (DAO) for the watchlist that uses a JSON file to
 * persist the data.
 */
public class FileWatchlistDataAccessObject {
  // TODO: After we add data access interfaces to our use cases, make this class implement them.
  final Path watchlistFile;

  /**
   * Returns the directory used to store app-specific configuration/preference files, following
   * standard OS conventions on Windows, macOS, and Linux.
   *
   * @return The OS-specific configuration directory
   */
  private static Path getConfigDir() {
    final String appName = "JavaStockTracker";
    String os = System.getProperty("os.name").toLowerCase();
    String userHome = System.getProperty("user.home");

    if (os.contains("win")) {
      // Windows: %APPDATA%\AppName
      String appData = System.getenv("APPDATA");
      return Path.of(appData != null ? appData : userHome, appName);
    } else if (os.contains("mac")) {
      // macOS: ~/Library/Application Support/AppName
      return Path.of(userHome, "Library", "Application Support", appName);
    } else {
      // Linux/Unix (XDG Base Directory spec): ~/.config/appname
      String xdgConfig = System.getenv("XDG_CONFIG_HOME");
      return xdgConfig != null
              ? Path.of(xdgConfig, appName.toLowerCase())
              : Path.of(userHome, ".config", appName.toLowerCase());
    }
  }

  /**
   * Creates a new data access object.
   *
   * @param fileName The name of the file to store the watchlist
   * @throws IOException If the directory to contain the watchlist file could not be created
   */
  public FileWatchlistDataAccessObject(String fileName) throws IOException {
    if ("".equals(fileName)) {
      throw new IllegalArgumentException("File name cannot be empty");
    }

    Path configDir = getConfigDir();
    Files.createDirectories(configDir);
    watchlistFile = configDir.resolve(fileName);
  }

  /**
   * Constructs a watchlist entity by reading the JSON file.
   *
   * @return A Watchlist object corresponding to the contents of the JSON file
   * @throws IOException If the file could not be read
   */
  public Watchlist readWatchlist() throws IOException {
    String jsonString = Files.readString(watchlistFile);
    JSONArray array = new JSONArray(jsonString);

    ArrayList<Stock> stocksList = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      stocksList.add(new Stock(array.getString(i)));
    }

    return new Watchlist(stocksList);
  }

  /**
   * Writes a watchlist entity to the JSON file.
   *
   * @param watchlist The watchlist to save
   * @throws IOException If the file could not be written to
   */
  public void saveWatchlist(Watchlist watchlist) throws IOException {
    JSONArray array = new JSONArray(watchlist.getStocks().stream().map(Stock::getTicker).toArray());
    Files.writeString(watchlistFile, array.toString());
  }
}
