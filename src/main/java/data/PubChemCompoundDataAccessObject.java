package data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Compound;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import usecase.CompoundDataAccessInterface;

/**
 * Looks compounds up on PubChem. One search makes five requests: the basic properties, then
 * the melting point, boiling point, GHS safety data and a structure picture for that compound.
 */
public class PubChemCompoundDataAccessObject implements CompoundDataAccessInterface {
  private static final String PUBCHEM = "https://pubchem.ncbi.nlm.nih.gov/rest";
  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private final String baseUrl;
  private final HttpClient client;

  /** Creates a lookup that talks to the real PubChem. */
  public PubChemCompoundDataAccessObject() {
    this(PUBCHEM);
  }

  /**
   * Creates a lookup that talks to another server with the same paths. Tests use this.
   *
   * @param baseUrl stands in for "https://pubchem.ncbi.nlm.nih.gov/rest"
   */
  PubChemCompoundDataAccessObject(String baseUrl) {
    this.baseUrl = baseUrl;
    this.client = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();
  }

  @Override
  public Optional<Compound> findByName(String name) throws IOException {
    byte[] properties = get(baseUrl + "/pug/compound/name/" + encode(name)
        + "/property/Title,MolecularFormula,MolecularWeight/JSON");
    if (properties == null) {
      return Optional.empty();
    }
    try {
      JsonObject row = parse(properties).getAsJsonObject("PropertyTable")
          .getAsJsonArray("Properties").get(0).getAsJsonObject();
      long cid = row.get("CID").getAsLong();
      String section = baseUrl + "/pug_view/data/compound/" + cid + "/JSON?heading=";
      String ghs = text(get(section + "GHS+Classification"));
      return Optional.of(new Compound(
          row.has("Title") ? row.get("Title").getAsString() : name,
          row.get("MolecularFormula").getAsString(),
          // PubChem sends the weight as a string like "74.12"; Gson converts it for us.
          row.get("MolecularWeight").getAsDouble(),
          firstTemperature(text(get(section + "Melting+Point"))),
          firstTemperature(text(get(section + "Boiling+Point"))),
          signal(ghs), hazardStatements(ghs),
          get(baseUrl + "/pug/compound/cid/" + cid + "/PNG")));
    } catch (RuntimeException e) {
      // Gson throws unchecked exceptions when a reply isn't shaped the way we expect.
      throw new IOException("PubChem sent a reply we couldn't read", e);
    }
  }

  /**
   * Picks the temperature to show from a PUG View melting or boiling point reply. PubChem lists
   * many reports, some in °F, so the first one in °C wins. Otherwise the first report is used
   * as is.
   *
   * @param json the reply, or null if PubChem has no data under that heading
   * @return e.g. "-116.2 °C", or null if there's nothing usable
   */
  static String firstTemperature(String json) {
    if (json == null) {
      return null;
    }
    List<String> reports = new ArrayList<>();
    for (JsonObject information : information(json)) {
      reports.addAll(values(information));
    }
    for (String report : reports) {
      if (report.contains("°C")) {
        return report;
      }
    }
    return reports.isEmpty() ? null : reports.get(0);
  }

  /**
   * Finds the GHS signal word in a PUG View GHS Classification reply.
   *
   * @param json the reply, or null if PubChem has no GHS data
   * @return "Danger" or "Warning", or null if there isn't one (e.g. water isn't classified)
   */
  static String signal(String json) {
    if (json == null) {
      return null;
    }
    for (JsonObject information : information(json)) {
      if (named(information, "Signal") && !values(information).isEmpty()) {
        return values(information).get(0);
      }
    }
    return null;
  }

  /**
   * Lists the hazard statements from a PUG View GHS Classification reply. PubChem repeats them
   * once per data source, so only the first list is used. The percent and the category in
   * brackets are dropped: "H224 (98%): Extremely flammable liquid and vapor [Danger Flammable
   * liquids]" becomes "H224: Extremely flammable liquid and vapor".
   *
   * @param json the reply, or null if PubChem has no GHS data
   * @return the statements, or empty if there are none
   */
  static List<String> hazardStatements(String json) {
    if (json == null) {
      return List.of();
    }
    for (JsonObject information : information(json)) {
      if (!named(information, "GHS Hazard Statements")) {
        continue;
      }
      List<String> statements = new ArrayList<>();
      for (String value : values(information)) {
        // Skips notes like "Not Classified" that aren't real H-statements.
        if (value.matches("H\\d{3}.*")) {
          statements.add(value.replaceAll(" \\([\\d.]+%\\)", "").replaceAll(" \\[.*]$", ""));
        }
      }
      if (!statements.isEmpty()) {
        return statements;
      }
    }
    return List.of();
  }

  /** Every Information entry in a PUG View reply, in the order PubChem lists them. */
  private static List<JsonObject> information(String json) {
    List<JsonObject> found = new ArrayList<>();
    collectInformation(JsonParser.parseString(json), found);
    return found;
  }

  private static void collectInformation(JsonElement node, List<JsonObject> found) {
    if (node.isJsonArray()) {
      for (JsonElement child : node.getAsJsonArray()) {
        collectInformation(child, found);
      }
    } else if (node.isJsonObject()) {
      JsonObject object = node.getAsJsonObject();
      if (object.has("Information")) {
        for (JsonElement information : object.getAsJsonArray("Information")) {
          found.add(information.getAsJsonObject());
        }
      }
      for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
        collectInformation(entry.getValue(), found);
      }
    }
  }

  /**
   * The text of one Information entry. It's either a list of strings, or a number with a unit
   * like {"Number": [140], "Unit": "°C"}. A number without a unit means nothing on its own, so
   * it's skipped.
   */
  private static List<String> values(JsonObject information) {
    List<String> values = new ArrayList<>();
    JsonObject value = information.getAsJsonObject("Value");
    if (value == null) {
      return values;
    }
    if (value.has("StringWithMarkup")) {
      for (JsonElement markup : value.getAsJsonArray("StringWithMarkup")) {
        JsonElement string = markup.getAsJsonObject().get("String");
        if (string != null && !string.getAsString().isBlank()) {
          values.add(string.getAsString().trim());
        }
      }
    } else if (value.has("Number") && value.has("Unit")) {
      JsonArray numbers = value.getAsJsonArray("Number");
      if (numbers.size() > 0) {
        values.add(numbers.get(0).getAsString() + " " + value.get("Unit").getAsString());
      }
    }
    return values;
  }

  private static boolean named(JsonObject information, String name) {
    return information.has("Name") && name.equals(information.get("Name").getAsString());
  }

  /**
   * Downloads a page.
   *
   * @return the body, or null if PubChem says there's no such compound or no data
   * @throws IOException if PubChem can't be reached, is too slow, or is having trouble
   */
  private byte[] get(String url) throws IOException {
    HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(TIMEOUT).build();
    HttpResponse<byte[]> response;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Interrupted while waiting for PubChem", e);
    }
    int status = response.statusCode();
    // 404 is "no such compound" or "no data under that heading"; 400 is a name PubChem
    // can't make sense of, which for the student is the same thing.
    if (status == 404 || status == 400) {
      return null;
    }
    if (status != 200) {
      throw new IOException("PubChem answered with HTTP " + status);
    }
    return response.body();
  }

  private static JsonObject parse(byte[] json) {
    return JsonParser.parseString(text(json)).getAsJsonObject();
  }

  private static String text(byte[] body) {
    return body == null ? null : new String(body, StandardCharsets.UTF_8);
  }

  /**
   * Puts a name into a URL path. PubChem reads "+" literally, so spaces have to be sent as %20.
   *
   * @param name e.g. "diethyl ether"
   * @return e.g. "diethyl%20ether"
   */
  static String encode(String name) {
    return URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
  }
}
