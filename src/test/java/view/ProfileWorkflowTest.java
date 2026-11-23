package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.ProfileApplication;
import data_access.DemoJobSource;
import data_access.FileProfileDataAccess;
import entity.User;
import java.awt.Component;
import java.awt.Container;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.JobSourceException;
import use_case.port.SourceFailure;
import use_case.profile.ProfileData;

class ProfileWorkflowTest {
  @TempDir Path directory;

  @Test
  void studentCanCreateReopenEditCancelAndGetRecommendationsWithTheLatestSkills() throws Exception {
    Path file = directory.resolve("profile.json");
    List<String> opened = new ArrayList<>();
    onEdt(
        () -> {
          JTabbedPane first = tabs(file, new DemoJobSource(), opened::add);
          Container profile = show(first, "Profile");
          field(profile, "Program of study").setText("Math");
          button(profile, "Cancel edits").doClick();
          assertEquals("", field(profile, "Program of study").getText());
          assertFalse(Files.exists(file));
          button(profile, "Save profile").doClick();
          assertTrue(shows(profile, "Enter your program of study."));
          field(profile, "Program of study").setText("Math");
          button(profile, "Save profile").doClick();
          assertTrue(shows(profile, "Enter at least one skill."));
          field(profile, "Skills").setText("Java, java, Python");
          field(profile, "Graduation year (optional)").setText("20xx");
          button(profile, "Save profile").doClick();
          assertTrue(shows(profile, "Enter a four-digit graduation year"));
          assertEquals("Java, java, Python", field(profile, "Skills").getText());
          assertFalse(Files.exists(file));

          field(profile, "Graduation year (optional)").setText("2028");
          button(profile, "Save profile").doClick();
          assertTrue(shows(profile, "Profile created"));
          assertEquals("Java, Python", field(profile, "Skills").getText());
          FileProfileDataAccess store = new FileProfileDataAccess(file);
          final String firstId = store.load().orElseThrow().getOwner().getUserId();
          button(profile, "View recommendations").doClick();
          Container results = (Container) first.getSelectedComponent();
          JTable table = table(results);
          assertEquals(2, table.getRowCount());
          assertEquals("Software Engineering Intern", table.getValueAt(0, 0));
          assertEquals("Java, Python", table.getValueAt(0, 4));
          assertEquals("UNKNOWN", table.getValueAt(0, 3));
          assertEquals("Not specified", table.getValueAt(1, 2));
          assertFalse(button(results, "Open application page").isEnabled());
          table.setRowSelectionInterval(0, 0);
          button(results, "Open application page").doClick();
          assertEquals(List.of("https://example.com/internships/software"), opened);

          JTabbedPane reopened = tabs(file, new DemoJobSource(), opened::add);
          Container edit = show(reopened, "Profile");
          assertEquals("Java, Python", field(edit, "Skills").getText());
          assertEquals("2028", field(edit, "Graduation year (optional)").getText());
          field(edit, "Skills").setText("");
          button(edit, "Save changes").doClick();
          assertTrue(shows(edit, "Enter at least one skill."));
          assertEquals(List.of("Java", "Python"), store.load().orElseThrow().getSkills());
          button(edit, "Cancel edits").doClick();
          assertEquals("Java, Python", field(edit, "Skills").getText());
          field(edit, "Skills").setText("SQL");
          button(edit, "Save changes").doClick();
          assertEquals(firstId, store.load().orElseThrow().getOwner().getUserId());
          button(edit, "View recommendations").doClick();
          Container newResults = (Container) reopened.getSelectedComponent();
          assertEquals(1, table(newResults).getRowCount());
          assertEquals("Data Science Co-op", table(newResults).getValueAt(0, 0));
          assertEquals("SQL", table(newResults).getValueAt(0, 4));
          button(newResults, "Edit profile").doClick();
          assertEquals(edit, reopened.getSelectedComponent());
          return null;
        });
  }

  @Test
  void unreadableProfileIsRetryableAndNeverTriggersTheExternalSource() throws Exception {
    Path file = directory.resolve("profile.json");
    Files.writeString(file, "broken");
    AtomicInteger fetches = new AtomicInteger();
    JobSource source =
        () -> {
          fetches.incrementAndGet();
          return new DemoJobSource().fetch();
        };
    onEdt(
        () -> {
          JTabbedPane tabs = tabs(file, source, url -> {});
          Container profile = show(tabs, "Profile");
          assertFalse(field(profile, "Skills").isEnabled());
          assertTrue(shows(profile, "Could not read the saved profile."));
          Container results = show(tabs, "Recommendations");
          assertTrue(shows(results, "Could not read the saved profile."));
          assertEquals(0, fetches.get());

          Files.delete(file);
          button(results, "Refresh recommendations").doClick();
          assertTrue(shows(results, "Create and save a profile"));
          assertEquals(0, fetches.get());
          button(results, "Edit profile").doClick();
          button(profile, "Reload profile").doClick();
          assertTrue(button(profile, "Save profile").isEnabled());
          assertTrue(field(profile, "Skills").isEnabled());
          return null;
        });
  }

  @Test
  void failedSaveKeepsDraftAndOriginalThenRetrySucceeds() throws Exception {
    Path file = directory.resolve("profile.json");
    FileProfileDataAccess store = new FileProfileDataAccess(file);
    store.create(new ProfileData("Math", "", "SQL", "", "", "ANY").toProfile(new User()));
    onEdt(
        () -> {
          Container profile = show(tabs(file, new DemoJobSource(), url -> {}), "Profile");
          field(profile, "Skills").setText("Java");
          try (FileChannel channel =
                  FileChannel.open(
                      directory.resolve("profile.json.lock"), StandardOpenOption.WRITE);
              FileLock held = channel.lock()) {
            assertTrue(held.isValid());
            button(profile, "Save changes").doClick();
            assertTrue(shows(profile, "Another save is in progress."));
            assertEquals("Java", field(profile, "Skills").getText());
            assertEquals(List.of("SQL"), store.load().orElseThrow().getSkills());
          }
          button(profile, "Save changes").doClick();
          assertTrue(shows(profile, "Changes saved."));
          assertEquals(List.of("Java"), store.load().orElseThrow().getSkills());
          return null;
        });
  }

  @Test
  void showsPartialWarningsEvenWithZeroMatchesAndDistinguishesTotalFailure() throws Exception {
    Path file = directory.resolve("profile.json");
    new FileProfileDataAccess(file)
        .create(new ProfileData("CS", "", "SQL", "", "", "ANY").toProfile(new User()));
    AtomicInteger call = new AtomicInteger();
    SourceFailure failure = new SourceFailure("second-board", SourceFailure.Reason.TIMEOUT);
    JobSource jobs =
        () -> {
          int attempt = call.getAndIncrement();
          if (attempt == 1) {
            throw new JobSourceException("All boards failed.");
          }
          return new JobFetchResult(
              attempt == 0 ? new DemoJobSource().fetch().getJobs() : List.of(),
              List.of("example"),
              List.of(failure));
        };
    onEdt(
        () -> {
          Container results =
              show(
                  tabs(
                      file,
                      jobs,
                      url -> {
                        throw new java.io.IOException();
                      }),
                  "Recommendations");
          assertEquals(1, table(results).getRowCount());
          assertTrue(shows(results, "Results are incomplete. Unavailable boards: second-board"));
          table(results).setRowSelectionInterval(0, 0);
          button(results, "Open application page").doClick();
          assertTrue(shows(results, "Could not open the browser."));
          assertEquals(
              "https://example.com/internships/data", field(results, "Application URL").getText());

          button(results, "Refresh recommendations").doClick();
          assertTrue(shows(results, "Could not retrieve job postings."));
          assertFalse(shows(results, "No matching internships."));
          assertEquals(0, table(results).getRowCount());
          assertFalse(button(results, "Open application page").isEnabled());

          button(results, "Refresh recommendations").doClick();
          assertTrue(shows(results, "No matching internships."));
          assertTrue(shows(results, "Results are incomplete. Unavailable boards: second-board"));
          return null;
        });
  }

  @Test
  void revisitingRecommendationsWhileLoadingUsesTheNewlySavedProfile() throws Exception {
    Path file = directory.resolve("profile.json");
    FileProfileDataAccess store = new FileProfileDataAccess(file);
    store.create(new ProfileData("CS", "", "Java", "", "", "ANY").toProfile(new User()));
    onEdt(
        () -> {
          Deque<Runnable> work = new ArrayDeque<>();
          JTabbedPane tabs =
              ProfileApplication.createTabs(
                  new DemoJobSource(),
                  store,
                  work::addLast,
                  Runnable::run,
                  url -> {},
                  "Test boards");
          work.removeFirst().run();
          final Container results = show(tabs, "Recommendations");
          Container profile = show(tabs, "Profile");
          field(profile, "Skills").setText("SQL");
          button(profile, "Save changes").doClick();
          show(tabs, "Recommendations");
          assertFalse(button(results, "Refresh recommendations").isEnabled());
          work.removeFirst().run();
          work.removeFirst().run();
          work.removeFirst().run();
          assertTrue(work.isEmpty());
          assertEquals(1, table(results).getRowCount());
          assertEquals("Data Science Co-op", table(results).getValueAt(0, 0));
          return null;
        });
  }

  @Test
  void productionStyleSchedulersRetrieveOffTheEdtAndUpdateTheScreenOnIt() throws Exception {
    Path file = directory.resolve("profile.json");
    FileProfileDataAccess store = new FileProfileDataAccess(file);
    store.create(new ProfileData("Math", "", "SQL", "", "", "ANY").toProfile(new User()));
    ExecutorService background = Executors.newSingleThreadExecutor();
    try {
      JobSource jobs =
          () -> {
            assertFalse(SwingUtilities.isEventDispatchThread());
            return new DemoJobSource().fetch();
          };
      Executor ui =
          action ->
              SwingUtilities.invokeLater(
                  () -> {
                    assertTrue(SwingUtilities.isEventDispatchThread());
                    action.run();
                  });
      Container results =
          onEdt(
              () ->
                  show(
                      ProfileApplication.createTabs(
                          jobs, store, background, ui, url -> {}, "Test boards"),
                      "Recommendations"));
      background.submit(() -> {}).get(5, TimeUnit.SECONDS);
      onEdt(
          () -> {
            assertEquals(1, table(results).getRowCount());
            assertTrue(button(results, "Refresh recommendations").isEnabled());
            return null;
          });
    } finally {
      background.shutdownNow();
    }
  }

  private static JTabbedPane tabs(Path file, JobSource jobs, LinkOpener opener) {
    return ProfileApplication.createTabs(
        jobs,
        new FileProfileDataAccess(file),
        Runnable::run,
        Runnable::run,
        opener,
        "Offline test listings");
  }

  private static Container show(JTabbedPane tabs, String name) {
    tabs.setSelectedIndex(tabs.indexOfTab(name));
    return (Container) tabs.getSelectedComponent();
  }

  private static boolean shows(Container view, String text) {
    return children(view, JLabel.class)
        .anyMatch(label -> label.isVisible() && label.getText().contains(text));
  }

  private static JTable table(Container view) {
    return children(view, JTable.class).findFirst().orElseThrow();
  }

  private static JButton button(Container view, String name) {
    return children(view, JButton.class)
        .filter(button -> button.getText().equals(name))
        .findFirst()
        .orElseThrow();
  }

  private static JTextField field(Container view, String name) {
    JLabel label =
        children(view, JLabel.class)
            .filter(item -> name.equals(item.getText()))
            .findFirst()
            .orElseThrow();
    return (JTextField) label.getLabelFor();
  }

  private static <T extends Component> Stream<T> children(Container view, Class<T> type) {
    return java.util.Arrays.stream(view.getComponents())
        .flatMap(
            component -> {
              Stream<T> direct =
                  type.isInstance(component) ? Stream.of(type.cast(component)) : Stream.empty();
              return component instanceof Container
                  ? Stream.concat(direct, children((Container) component, type))
                  : direct;
            });
  }

  private static <T> T onEdt(Callable<T> action) throws Exception {
    FutureTask<T> task = new FutureTask<>(action);
    SwingUtilities.invokeAndWait(task);
    return task.get(5, TimeUnit.SECONDS);
  }
}
