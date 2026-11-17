package data_access;

import entity.ActivityIdea;
import entity.Contribution;
import entity.Expense;
import entity.Trip;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Stores trips, expenses, contributions and activity ideas in one JSON file, so data is still there
 * the next time the app starts.
 *
 * <p>The whole file is rewritten after every change. That is simple and fast enough for the amount
 * of data one group's trip produces.
 */
public class JsonFileTripDataAccessObject extends InMemoryTripDataAccessObject {

  private static final String TRIPS = "trips";
  private static final String EXPENSES = "expenses";
  private static final String CONTRIBUTIONS = "contributions";
  private static final String ACTIVITY_IDEAS = "activityIdeas";
  private static final int INDENT = 2;

  private final Path file;

  /**
   * Opens the data file, creating it if it does not exist yet.
   *
   * @param filePath where the JSON file lives, for example "trips.json"
   * @throws DataAccessException if the file exists but cannot be read or parsed
   */
  public JsonFileTripDataAccessObject(String filePath) {
    this.file = Path.of(filePath);
    if (Files.exists(file)) {
      load();
    } else {
      onDataChanged();
    }
  }

  @Override
  protected void onDataChanged() {
    final JSONObject root = new JSONObject();
    root.put(TRIPS, toArray(allTrips(), TripJsonMapper::tripToJson));
    root.put(EXPENSES, toArray(allExpenses(), TripJsonMapper::expenseToJson));
    root.put(CONTRIBUTIONS, toArray(allContributions(), TripJsonMapper::contributionToJson));
    root.put(ACTIVITY_IDEAS, toArray(allActivityIdeas(), TripJsonMapper::activityIdeaToJson));
    try {
      Files.writeString(file, root.toString(INDENT), StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new DataAccessException("Could not save data to " + file, ex);
    }
  }

  private void load() {
    final JSONObject root;
    try {
      final String content = Files.readString(file, StandardCharsets.UTF_8);
      root = content.isBlank() ? new JSONObject() : new JSONObject(content);
    } catch (IOException | org.json.JSONException ex) {
      throw new DataAccessException("Could not read data from " + file, ex);
    }

    final List<Trip> trips = new ArrayList<>();
    for (JSONObject json : objects(root, TRIPS)) {
      trips.add(TripJsonMapper.tripFromJson(json));
    }
    final List<Expense> expenses = new ArrayList<>();
    for (JSONObject json : objects(root, EXPENSES)) {
      expenses.add(TripJsonMapper.expenseFromJson(json));
    }
    final List<Contribution> contributions = new ArrayList<>();
    for (JSONObject json : objects(root, CONTRIBUTIONS)) {
      contributions.add(TripJsonMapper.contributionFromJson(json));
    }
    final List<ActivityIdea> ideas = new ArrayList<>();
    for (JSONObject json : objects(root, ACTIVITY_IDEAS)) {
      ideas.add(TripJsonMapper.activityIdeaFromJson(json));
    }
    loadAll(trips, expenses, contributions, ideas);
  }

  private static List<JSONObject> objects(JSONObject root, String key) {
    final List<JSONObject> result = new ArrayList<>();
    final JSONArray array = root.optJSONArray(key);
    if (array != null) {
      for (int i = 0; i < array.length(); i++) {
        result.add(array.getJSONObject(i));
      }
    }
    return result;
  }

  private static <T> JSONArray toArray(List<T> entities, Function<T, JSONObject> toJson) {
    final JSONArray array = new JSONArray();
    for (T entity : entities) {
      array.put(toJson.apply(entity));
    }
    return array;
  }
}
