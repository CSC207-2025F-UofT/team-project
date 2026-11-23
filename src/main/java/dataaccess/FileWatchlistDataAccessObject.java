package dataaccess;

import entities.Stock;
import entities.Watchlist;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import usecase.viewstockinfo.ViewStockInfoWatchlistDataAccessInterface;

/**
 * Implementation of the Data Access Object (DAO) for the watchlist that uses a JSON file to
 * persist the data.
 */
public class FileWatchlistDataAccessObject implements ViewStockInfoWatchlistDataAccessInterface {
  // TODO: After we add data access interfaces to our use cases, make this class implement them.
  private final Path watchlistFile;

  /**
   * Creates a new data access object.
   *
   * @param fileName The name of the file to store the watchlist
   * @throws IOException If the directory to contain the watchlist file could not be created
   */
  public FileWatchlistDataAccessObject(String fileName) throws IOException {
    watchlistFile = AppConfigDirectory.resolve(fileName);
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

  @Override
  public List<Stock> getWatchlistStocks() throws IOException {
    try {
      return readWatchlist().getStocks();
    } catch (NoSuchFileException e) {
      return List.of();
    } catch (JSONException | IllegalArgumentException e) {
      throw new IOException("Invalid watchlist data", e);
    }
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
