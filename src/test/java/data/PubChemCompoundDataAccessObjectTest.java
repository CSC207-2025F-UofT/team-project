package data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import entity.Compound;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Checks the PubChem lookup against replies saved from the real API, so no test goes online.
 */
class PubChemCompoundDataAccessObjectTest {
  private static final byte[] PNG = {(byte) 0x89, 'P', 'N', 'G'};

  // A tiny local server that plays PubChem. Each page is matched by a piece of its URL;
  // anything not listed gets a 404, the same as PubChem does for missing data.
  private final Map<String, byte[]> pages = new LinkedHashMap<>();
  private final List<String> requested = new ArrayList<>();
  private int forcedStatus;
  private HttpServer server;
  private PubChemCompoundDataAccessObject pubChem;

  @BeforeEach
  void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext("/", exchange -> {
      String uri = exchange.getRequestURI().getRawPath() + "?"
          + exchange.getRequestURI().getRawQuery();
      requested.add(uri);
      byte[] body = pages.entrySet().stream().filter(page -> uri.contains(page.getKey()))
          .map(Map.Entry::getValue).findFirst().orElse(null);
      int status = forcedStatus != 0 ? forcedStatus : body == null ? 404 : 200;
      byte[] reply = status == 200 ? body : new byte[0];
      exchange.sendResponseHeaders(status, reply.length == 0 ? -1 : reply.length);
      try (OutputStream out = exchange.getResponseBody()) {
        out.write(reply);
      }
    });
    server.start();
    pubChem = new PubChemCompoundDataAccessObject(
        "http://127.0.0.1:" + server.getAddress().getPort());
  }

  @AfterEach
  void stopServer() {
    server.stop(0);
  }

  @Test
  void findsDiethylEther() throws IOException {
    pages.put("/property/", fixture("diethyl_ether_properties.json").getBytes(
        StandardCharsets.UTF_8));
    pages.put("Melting", fixture("diethyl_ether_mp.json").getBytes(StandardCharsets.UTF_8));
    pages.put("Boiling", fixture("diethyl_ether_bp.json").getBytes(StandardCharsets.UTF_8));
    pages.put("GHS", fixture("diethyl_ether_ghs.json").getBytes(StandardCharsets.UTF_8));
    pages.put("/PNG", PNG);

    Compound ether = pubChem.findByName("diethyl ether").orElseThrow();

    assertEquals("Diethyl Ether", ether.getName());
    assertEquals("C4H10O", ether.getFormula());
    assertEquals(74.12, ether.getMolecularWeight());
    assertTrue(ether.getMeltingPoint().startsWith("-116.3 °C"));
    assertTrue(ether.getBoilingPoint().startsWith("34.6 °C"));
    assertEquals("Danger", ether.getSignal());
    assertEquals("H224: Extremely flammable liquid and vapor",
        ether.getHazardStatements().get(0));
    assertArrayEquals(PNG, ether.getStructureImage());
    assertTrue(requested.get(0).contains("/name/diethyl%20ether/"));
    assertTrue(requested.stream().anyMatch(uri -> uri.contains("/compound/3283/")));
  }

  @Test
  void unknownNameIsEmpty() throws IOException {
    assertEquals(Optional.empty(), pubChem.findByName("unobtainium"));
  }

  @Test
  void busyServerThrows() {
    forcedStatus = 503;
    assertThrows(IOException.class, () -> pubChem.findByName("diethyl ether"));
  }

  @Test
  void garbledReplyThrows() {
    pages.put("/property/", "<html>not json</html>".getBytes(StandardCharsets.UTF_8));
    assertThrows(IOException.class, () -> pubChem.findByName("diethyl ether"));
  }

  @Test
  void missingSectionsAreLeftOut() throws IOException {
    pages.put("/property/", fixture("diethyl_ether_properties.json").getBytes(
        StandardCharsets.UTF_8));

    Compound ether = pubChem.findByName("diethyl ether").orElseThrow();

    assertEquals("Diethyl Ether", ether.getName());
    assertNull(ether.getMeltingPoint());
    assertNull(ether.getBoilingPoint());
    assertNull(ether.getSignal());
    assertTrue(ether.getHazardStatements().isEmpty());
    assertNull(ether.getStructureImage());
  }

  @Test
  void urlEncodesSpaces() {
    assertEquals("diethyl%20ether", PubChemCompoundDataAccessObject.encode("diethyl ether"));
  }

  @Test
  void meltingPointPrefersCelsius() throws IOException {
    // The first report is in °F, so the first °C one should win.
    assertEquals("-116.3 °C (stable crystals); -123.3 °C (metastable crystals)",
        PubChemCompoundDataAccessObject.firstTemperature(fixture("diethyl_ether_mp.json")));
  }

  @Test
  void boilingPointFromNumberAndUnit() throws IOException {
    assertEquals("140 °C",
        PubChemCompoundDataAccessObject.firstTemperature(fixture("aspirin_bp.json")));
  }

  @Test
  void parsesGhs() throws IOException {
    String ghs = fixture("diethyl_ether_ghs.json");
    assertEquals("Danger", PubChemCompoundDataAccessObject.signal(ghs));
    assertEquals(List.of("H224: Extremely flammable liquid and vapor",
        "H302: Harmful if swallowed", "H336: May cause drowsiness or dizziness"),
        PubChemCompoundDataAccessObject.hazardStatements(ghs));
  }

  @Test
  void waterIsNotClassified() throws IOException {
    String ghs = fixture("water_ghs.json");
    assertNull(PubChemCompoundDataAccessObject.signal(ghs));
    assertTrue(PubChemCompoundDataAccessObject.hazardStatements(ghs).isEmpty());
  }

  @Test
  void missingSectionIsNull() {
    assertNull(PubChemCompoundDataAccessObject.firstTemperature(null));
    assertNull(PubChemCompoundDataAccessObject.signal(null));
    assertTrue(PubChemCompoundDataAccessObject.hazardStatements(null).isEmpty());
  }

  private static String fixture(String name) throws IOException {
    try (InputStream in = PubChemCompoundDataAccessObjectTest.class
        .getResourceAsStream("/pubchem/" + name)) {
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
