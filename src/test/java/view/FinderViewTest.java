package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.FinderApplication;
import data_access.DemoJobSource;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;

class FinderViewTest {
  @Test
  void realAssemblyValidatesSearchRendersResultsAndOpensOnlySelectedPosting() throws Exception {
    onEdt(
        () -> {
          List<String> opened = new ArrayList<>();
          FinderView view =
              FinderApplication.createView(
                  new DemoJobSource(), Runnable::run, Runnable::run, opened::add, "OFFLINE DEMO");
          JTextField input =
              components(view, JTextField.class).stream()
                  .filter(JTextField::isEditable)
                  .findFirst()
                  .orElseThrow();
          JButton search = button(view, "Search");
          final JTable table = components(view, JTable.class).get(0);
          search.doClick();
          assertTrue(text(view).contains("Enter at least one keyword."));
          input.setText("PYTHON");
          search.doClick();
          assertEquals(2, table.getRowCount());
          assertTrue(text(view).contains("2 matching internships"));
          assertEquals("Not specified", table.getValueAt(1, 2));
          assertFalse(button(view, "Open application page").isEnabled());
          // Sorting changes view positions: link selection must use the table's model index.
          table.getRowSorter().toggleSortOrder(0);
          table.setRowSelectionInterval(0, 0);
          assertEquals("Data Science Co-op", table.getValueAt(0, 0));
          button(view, "Open application page").doClick();
          assertEquals(List.of("https://example.com/internships/data"), opened);
          assertEquals(2, table.getRowCount());
          input.setText("does-not-match");
          search.doClick();
          assertEquals(0, table.getRowCount());
          assertFalse(button(view, "Open application page").isEnabled());
          assertTrue(text(view).contains("0 matches"));
          return null;
        });
  }

  @Test
  void failedBrowserOpenKeepsCopyableUrlAndResults() throws Exception {
    onEdt(
        () -> {
          FinderView view =
              FinderApplication.createView(
                  new DemoJobSource(),
                  Runnable::run,
                  Runnable::run,
                  url -> {
                    throw new java.io.IOException("No browser");
                  },
                  "Sample data");
          components(view, JTextField.class).stream()
              .filter(JTextField::isEditable)
              .findFirst()
              .orElseThrow()
              .setText("java");
          button(view, "Search").doClick();
          components(view, JTable.class).get(0).setRowSelectionInterval(0, 0);
          button(view, "Open application page").doClick();
          assertTrue(text(view).contains("Copy the application URL"));
          assertTrue(
              components(view, JTextField.class).stream()
                  .anyMatch(field -> field.getText().startsWith("https://example.com/")));
          return null;
        });
  }

  @Test
  void slowRetrievalRunsOffEdtAndUiRemainsResponsive() throws Exception {
    ExecutorService worker = Executors.newSingleThreadExecutor();
    CountDownLatch started = new CountDownLatch(1);
    CountDownLatch release = new CountDownLatch(1);
    CountDownLatch published = new CountDownLatch(1);
    AtomicBoolean retrievalOnEdt = new AtomicBoolean(true);
    AtomicInteger calls = new AtomicInteger();
    JobSource source =
        () -> {
          retrievalOnEdt.set(SwingUtilities.isEventDispatchThread());
          calls.incrementAndGet();
          started.countDown();
          try {
            if (!release.await(3, TimeUnit.SECONDS)) {
              throw new AssertionError("Test did not release retrieval.");
            }
          } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError(exception);
          }
          return new JobFetchResult(List.of(), List.of("test"), List.of());
        };
    try {
      FinderView view =
          onEdt(
              () ->
                  FinderApplication.createView(
                      source,
                      worker,
                      task ->
                          SwingUtilities.invokeLater(
                              () -> {
                                task.run();
                                published.countDown();
                              }),
                      url -> {},
                      "Test board"));
      onEdt(
          () -> {
            components(view, JTextField.class).stream()
                .filter(JTextField::isEditable)
                .findFirst()
                .orElseThrow()
                .setText("java");
            button(view, "Search").doClick();
            return null;
          });
      assertTrue(started.await(2, TimeUnit.SECONDS));
      onEdt(
          () -> {
            assertFalse(button(view, "Search").isEnabled());
            assertTrue(text(view).contains("Searching configured boards"));
            button(view, "Search").doClick();
            return null;
          });
      assertFalse(retrievalOnEdt.get());
      assertEquals(1, calls.get());
      release.countDown();
      assertTrue(published.await(2, TimeUnit.SECONDS));
      onEdt(
          () -> {
            assertTrue(button(view, "Search").isEnabled());
            assertTrue(text(view).contains("0 matches"));
            return null;
          });
    } finally {
      release.countDown();
      worker.shutdownNow();
    }
  }

  @Test
  void demoPreviewUsesRealComponentsWithoutPixelAssertions() throws Exception {
    onEdt(
        () -> {
          FinderView view =
              FinderApplication.createView(
                  new DemoJobSource(),
                  Runnable::run,
                  Runnable::run,
                  url -> {},
                  "OFFLINE DEMO - fictional sample listings");
          components(view, JTextField.class).stream()
              .filter(JTextField::isEditable)
              .findFirst()
              .orElseThrow()
              .setText("python");
          button(view, "Search").doClick();
          components(view, JTable.class).get(0).setRowSelectionInterval(0, 0);
          assertEquals(2, components(view, JTable.class).get(0).getRowCount());
          String preview = System.getProperty("finder.preview.path");
          if (preview != null) {
            view.setSize(view.getPreferredSize());
            layout(view);
            BufferedImage image =
                new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            view.printAll(graphics);
            graphics.dispose();
            Path path = Path.of(preview);
            Files.createDirectories(path.toAbsolutePath().getParent());
            ImageIO.write(image, "png", path.toFile());
          }
          return null;
        });
  }

  @Test
  void desktopAdapterRejectsNonWebSchemesBeforeOpeningAnything() {
    org.junit.jupiter.api.Assertions.assertThrows(
        java.io.IOException.class, () -> new DesktopLinkOpener().open("file:///etc/passwd"));
    org.junit.jupiter.api.Assertions.assertThrows(
        java.io.IOException.class, () -> new DesktopLinkOpener().open("javascript:alert(1)"));
  }

  private static JButton button(Container root, String label) {
    return components(root, JButton.class).stream()
        .filter(button -> label.equals(button.getText()))
        .findFirst()
        .orElseThrow();
  }

  private static String text(Container root) {
    return components(root, JTextArea.class).stream()
        .map(JTextArea::getText)
        .collect(java.util.stream.Collectors.joining(" "));
  }

  private static <T extends Component> List<T> components(Container root, Class<T> type) {
    List<T> result = new ArrayList<>();
    for (Component component : root.getComponents()) {
      if (type.isInstance(component)) {
        result.add(type.cast(component));
      }
      if (component instanceof Container) {
        result.addAll(components((Container) component, type));
      }
    }
    return result;
  }

  private static <T> T onEdt(Callable<T> operation) throws Exception {
    FutureTask<T> task = new FutureTask<>(operation);
    SwingUtilities.invokeAndWait(task);
    return task.get();
  }

  private static void layout(Container container) {
    container.doLayout();
    for (Component component : container.getComponents()) {
      if (component instanceof Container) {
        layout((Container) component);
      }
    }
  }
}
