package adapter;

import entity.CharacterClass;
import entity.Race;
import entity.Stats;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import usecase.CatalogGateway;

/** Retrieves races and character classes from the D&D 5e API. */
public final class Dnd5eCatalogGateway implements CatalogGateway {
  private static final URI DEFAULT_API_BASE_URI = URI.create("https://www.dnd5eapi.co/api/2014/");
  // might implement ability to set base stats in future
  private static final Stats CLASS_BASE_STATS = new Stats(10, 10, 10, 10, 10, 10);

  private final OkHttpClient httpClient;
  private final URI apiBaseUri;
  private List<Race> racesCache;
  private List<CharacterClass> classesCache;

  /** Creates a gateway using the public D&D 5e 2014 API. */
  public Dnd5eCatalogGateway() {
    this(new OkHttpClient(), DEFAULT_API_BASE_URI);
  }

  /** Creates a gateway using the supplied HTTP client and API base URI. */
  public Dnd5eCatalogGateway(OkHttpClient httpClient, URI apiBaseUri) {
    if (httpClient == null || apiBaseUri == null) {
      throw new IllegalArgumentException("HTTP client and API base URI required");
    }
    this.httpClient = httpClient;
    this.apiBaseUri =
        apiBaseUri.toString().endsWith("/") ? apiBaseUri : URI.create(apiBaseUri + "/");
  }

  /** Loads race names and ability bonuses, reusing the in-memory cache when available. */
  @Override
  public synchronized List<Race> getRaces() {
    if (racesCache != null) {
      return new ArrayList<>(racesCache);
    }
    JSONArray references = requiredArray(fetchJson("races"), "results", "race catalog");
    List<Race> races = new ArrayList<>();
    for (Object reference : references) {
      JSONObject raceReference = requireObject(reference, "race reference");
      String index = requiredString(raceReference, "index", "race reference");
      String name = requiredString(raceReference, "name", "race reference");
      JSONObject details = fetchJson("races/" + index);
      races.add(new Race(name, parseRaceBonuses(details)));
    }
    racesCache = Collections.unmodifiableList(races);
    return new ArrayList<>(racesCache);
  }

  /** Loads class names with the standard base stats, reusing the cache when available. */
  @Override
  public synchronized List<CharacterClass> getClasses() {
    if (classesCache != null) {
      return new ArrayList<>(classesCache);
    }
    JSONArray references = requiredArray(fetchJson("classes"), "results", "class catalog");
    List<CharacterClass> classes = new ArrayList<>();
    for (Object reference : references) {
      JSONObject classReference = requireObject(reference, "class reference");
      String name = requiredString(classReference, "name", "class reference");
      classes.add(new CharacterClass(name, CLASS_BASE_STATS));
    }
    classesCache = Collections.unmodifiableList(classes);
    return new ArrayList<>(classesCache);
  }

  /** Fetches and validates one JSON object from the D&D API. */
  private JSONObject fetchJson(String path) {
    URI uri = apiBaseUri.resolve(path);
    Request request =
        new Request.Builder().url(uri.toString()).header("Accept", "application/json").build();
    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IllegalStateException(
            "D&D 5e API returned HTTP " + response.code() + " for " + uri);
      }
      ResponseBody responseBody = response.body();
      if (responseBody == null) {
        throw new IllegalStateException("Empty response from " + uri);
      }
      try {
        return requireObject(new JSONObject(responseBody.string()), "response from " + uri);
      } catch (JSONException e) {
        throw new IllegalStateException("Invalid JSON returned by D&D 5e API resource " + uri, e);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Could not retrieve D&D 5e API resource " + uri, e);
    }
  }

  /** Converts the API's ability-bonus entries into the domain's six-stat value object. */
  private Stats parseRaceBonuses(JSONObject race) {
    JSONArray bonuses = requiredArray(race, "ability_bonuses", "race details");
    int strength = 0;
    int dexterity = 0;
    int constitution = 0;
    int intelligence = 0;
    int wisdom = 0;
    int charisma = 0;

    for (Object element : bonuses) {
      JSONObject bonus = requireObject(element, "race ability bonus");
      JSONObject ability = requireObject(bonus.get("ability_score"), "race ability score");
      String abilityIndex = requiredString(ability, "index", "ability score");
      int value = requiredInt(bonus);
      switch (abilityIndex) {
        case "str":
          strength = value;
          break;
        case "dex":
          dexterity = value;
          break;
        case "con":
          constitution = value;
          break;
        case "int":
          intelligence = value;
          break;
        case "wis":
          wisdom = value;
          break;
        case "cha":
          charisma = value;
          break;
        default:
          throw new IllegalStateException("Unknown ability score from D&D 5e API: " + abilityIndex);
      }
    }
    return new Stats(strength, dexterity, constitution, intelligence, wisdom, charisma);
  }

  /** Returns the named JSON array or reports a missing or malformed API field. */
  private static JSONArray requiredArray(JSONObject object, String field, String context) {
    if (!object.has(field) || object.isNull(field) || !(object.get(field) instanceof JSONArray)) {
      throw new IllegalStateException("Missing or invalid " + field + " in " + context);
    }
    return object.getJSONArray(field);
  }

  /** Returns a JSON object or reports a missing or malformed API value. */
  private static JSONObject requireObject(Object element, String context) {
    if (!(element instanceof JSONObject)) {
      throw new IllegalStateException("Expected an object for " + context);
    }
    return (JSONObject) element;
  }

  /** Returns a required string field from an API object. */
  private static String requiredString(JSONObject object, String field, String context) {
    if (!object.has(field) || object.isNull(field) || !(object.get(field) instanceof String)) {
      throw new IllegalStateException("Missing or invalid " + field + " in " + context);
    }
    return object.getString(field);
  }

  /** Returns a numeric ability bonus from the API response. */
  private static int requiredInt(JSONObject bonus) {
    if (!bonus.has("bonus") || bonus.isNull("bonus")) {
      throw new IllegalStateException("Missing or invalid bonus in race ability bonus");
    }
    try {
      return bonus.getInt("bonus");
    } catch (JSONException e) {
      throw new IllegalStateException("Missing or invalid bonus in race ability bonus", e);
    }
  }
}
