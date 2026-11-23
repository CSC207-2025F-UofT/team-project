package app;

import data_access.DemoJobSource;
import data_access.FileProfileDataAccess;
import data_access.GreenhouseJobSource;
import data_access.JobBoard;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import use_case.common.InternshipClassifier;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.JobSourceException;
import view.DesktopLinkOpener;

/** Entry point for the Java 11 Swing internship Finder. */
public final class Main {
  private Main() {}

  /** Starts the Finder, offline demo, or explicit command-line API smoke check. */
  public static void main(String[] args) {
    try {
      start(args);
    } catch (IOException | IllegalArgumentException | JobSourceException exception) {
      System.err.println("Finder could not start: " + exception.getMessage());
      System.exit(1);
    }
  }

  private static void start(String[] args) throws IOException, JobSourceException {
    boolean demo = false;
    boolean checkApi = false;
    Path config = null;
    for (int index = 0; index < args.length; index++) {
      if ("--demo".equals(args[index])) {
        demo = true;
      } else if ("--check-api".equals(args[index])) {
        checkApi = true;
      } else if ("--boards".equals(args[index]) && index + 1 < args.length) {
        config = Path.of(args[++index]);
      } else {
        throw new IllegalArgumentException(
            "Usage: java -jar TeamProject-1.0-SNAPSHOT.jar [--demo | --boards FILE] [--check-api]");
      }
    }
    if (demo && config != null) {
      throw new IllegalArgumentException("Choose either sample data or a board configuration.");
    }
    JobSource source;
    String sourceLabel;
    if (demo) {
      source = new DemoJobSource();
      sourceLabel = "OFFLINE DEMO - fictional sample listings";
    } else {
      List<JobBoard> boards;
      try (InputStream input =
          config == null
              ? Main.class.getResourceAsStream("/boards.properties")
              : Files.newInputStream(config)) {
        boards = BoardConfiguration.load(input);
      }
      Duration timeout = Duration.ofSeconds(8);
      source =
          new GreenhouseJobSource(
              HttpClient.newBuilder().connectTimeout(timeout).build(),
              URI.create("https://boards-api.greenhouse.io/v1/boards/"),
              boards,
              timeout);
      sourceLabel =
          "Searching company boards: "
              + boards.stream().map(JobBoard::getCompany).collect(Collectors.joining(", "));
    }
    if (checkApi) {
      JobFetchResult result = source.fetch();
      System.out.println(sourceLabel);
      System.out.println("Successful boards: " + result.getSuccessfulSources());
      System.out.println("Unavailable boards: " + result.getFailedBoards());
      System.out.println("Postings: " + result.getJobs().size());
      System.out.println(
          "Internship postings: "
              + result.getJobs().stream()
                  .filter(job -> InternshipClassifier.isInternship(job.getTitle()))
                  .count());
      return;
    }
    if (GraphicsEnvironment.isHeadless()) {
      throw new IllegalArgumentException("A graphical desktop is required to open the Finder.");
    }
    SwingUtilities.invokeLater(() -> showWindow(source, sourceLabel));
  }

  private static void showWindow(JobSource source, String sourceLabel) {
    ExecutorService worker =
        Executors.newSingleThreadExecutor(
            task -> {
              Thread thread = new Thread(task, "finder-search");
              thread.setDaemon(true);
              return thread;
            });
    JFrame window = new JFrame("Internship Finder and Tracker");
    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    Path defaultProfile =
        Path.of(System.getProperty("user.home"), ".intern-finder", "profile.json");
    Path profilePath =
        Path.of(System.getProperty("internfinder.profile", defaultProfile.toString()));
    window.add(
        ProfileApplication.createTabs(
            source,
            new FileProfileDataAccess(profilePath),
            worker,
            SwingUtilities::invokeLater,
            new DesktopLinkOpener(),
            sourceLabel));
    window.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent event) {
            worker.shutdownNow();
          }
        });
    window.pack();
    window.setLocationRelativeTo(null);
    window.setVisible(true);
  }
}
