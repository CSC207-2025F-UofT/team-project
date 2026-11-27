package app;

import data_access.JobBoard;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

/** Loads board tokens and company labels from a properties file at the application boundary. */
public final class BoardConfiguration {
  private BoardConfiguration() {}

  /**
   * Loads a non-empty configuration; the caller owns the input stream.
   *
   * @param input properties containing boardToken=Company Name entries
   * @return boards in deterministic token order
   * @throws IOException if the stream cannot be read
   */
  public static List<JobBoard> load(InputStream input) throws IOException {
    if (input == null) {
      throw new IOException("Board configuration was not found.");
    }
    Properties properties = new Properties();
    properties.load(input);
    List<JobBoard> boards = new ArrayList<>();
    for (String token : new TreeSet<>(properties.stringPropertyNames())) {
      boards.add(new JobBoard(token, properties.getProperty(token)));
    }
    if (boards.isEmpty()) {
      throw new IllegalArgumentException("Configure at least one company job board.");
    }
    return List.copyOf(boards);
  }
}
