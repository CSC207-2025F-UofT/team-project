package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.FinderApplication;
import data_access.DemoJobSource;
import entity.JobPosting;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import use_case.port.CompanyBoardSource;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.PagedJobSearchSource;

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
  void companyPickerScopesEachSearchAndKeepsTheKeywordsWhenSwitching() throws Exception {
    onEdt(
        () -> {
          List<String> requests = new ArrayList<>();
          CompanyBoardSource source =
              new CompanyBoardSource() {
                @Override
                public java.util.Map<String, String> getCompanies() {
                  return java.util.Map.of("alpha", "Alpha");
                }

                @Override
                public JobFetchResult fetch() {
                  requests.add("all");
                  return new JobFetchResult(List.of(), List.of("alpha"), List.of());
                }

                @Override
                public JobFetchResult fetchCompany(String board) {
                  requests.add(board);
                  return new JobFetchResult(List.of(), List.of(board), List.of());
                }
              };
          FinderView view =
              FinderApplication.createView(
                  source, Runnable::run, Runnable::run, url -> {}, "Company boards");
          JComboBox<?> picker = components(view, JComboBox.class).get(0);
          JTextField keywords = fieldWithLabel(view, "Keywords");
          keywords.setText("software");
          button(view, "Search").doClick();
          picker.setSelectedItem("Alpha (alpha)");
          keywords.postActionEvent();
          picker.setSelectedItem("Other Greenhouse board");
          JTextField custom = fieldWithLabel(view, "Board token or URL");
          custom.setText("https://job-boards.greenhouse.io/startup");
          custom.postActionEvent();
          picker.setSelectedIndex(0);
          assertFalse(custom.isVisible());
          keywords.postActionEvent();
          assertEquals(
              List.of("all", "alpha", "https://job-boards.greenhouse.io/startup", "all"), requests);
          return null;
        });
  }

  private static JTextField fieldWithLabel(Container root, String title) {
    return (JTextField)
        components(root, JLabel.class).stream()
            .filter(label -> title.equals(label.getText()))
            .findFirst()
            .orElseThrow()
            .getLabelFor();
  }

  @Test
  void joobleTabSearchesKeywordsAndOpensTheCorrectLinks() throws Exception {
    String provider = "Jooble";
    String homepage = "https://ca.jooble.org/";
    onEdt(
        () -> {
          List<String> requests = new ArrayList<>();
          List<String> opened = new ArrayList<>();
          JobPosting intern =
              new JobPosting.Builder(provider.toLowerCase(java.util.Locale.ROOT), "ca", "42")
                  .title("Research Co-op")
                  .company("An unconfigured startup")
                  .applicationUrl("https://example.com/listing/42")
                  .build();
          JobPosting other =
              new JobPosting.Builder(provider.toLowerCase(java.util.Locale.ROOT), "ca", "43")
                  .title("Internal Tools Engineer")
                  .company("Another employer")
                  .applicationUrl("https://example.com/listing/43")
                  .build();
          PagedJobSearchSource index =
              (criteria, page) -> {
                assertTrue(criteria.getCompany().isEmpty());
                requests.add(criteria.getQuery() + "/" + criteria.getLocation());
                return new PagedJobSearchSource.Page(
                    new JobFetchResult(List.of(intern, other), List.of(provider), List.of()), 2);
              };
          JTabbedPane tabs =
              FinderApplication.createLiveView(
                  () -> {
                    throw new AssertionError(provider + " searched a company board");
                  },
                  Optional.of(index),
                  Runnable::run,
                  Runnable::run,
                  opened::add,
                  "Company boards");
          tabs.setSelectedIndex(tabs.indexOfTab(provider + " Canada"));
          Container panel = (Container) tabs.getSelectedComponent();
          JTextField location = fieldWithLabel(panel, "Location (optional)");
          location.setText("  Toronto ");
          button(panel, "Search").doClick();
          assertTrue(requests.isEmpty());
          fieldWithLabel(panel, "Keywords (required)").setText("software   intern");
          location.postActionEvent();
          assertEquals(List.of("software intern/Toronto"), requests);
          JTable table = components(panel, JTable.class).get(0);
          assertEquals(1, table.getRowCount());
          assertEquals("An unconfigured startup", table.getValueAt(0, 1));
          assertEquals(provider, table.getValueAt(0, 3));
          table.changeSelection(0, 3, false, false);
          table.getActionMap().get("openSelectedLink").actionPerformed(null);
          button(panel, "Open application page").doClick();
          assertEquals(List.of(homepage, intern.getApplicationUrl()), opened);
          return null;
        });
  }

  @Test
  void keywordSearchFiltersByCompanyAcrossBatchesAndResetsWhenEdited() throws Exception {
    onEdt(
        () -> {
          List<Integer> pages = new ArrayList<>();
          PagedJobSearchSource index =
              (criteria, page) -> {
                assertEquals("software", criteria.getQuery());
                assertEquals("aMaZoN", criteria.getCompany());
                assertEquals("Toronto", criteria.getLocation());
                pages.add(page);
                List<JobPosting> postings = new ArrayList<>();
                int first = page == 1 ? 1 : 24;
                int last = page == 1 ? 25 : 48;
                for (int id = first; id <= last; id++) {
                  postings.add(
                      new JobPosting.Builder("jooble", "ca", String.valueOf(id))
                          .title("Software Intern " + id)
                          .company(id % 10 == 0 ? "Other employer" : "Amazon Canada")
                          .applicationUrl("https://example.com/jobs/" + id)
                          .build());
                }
                return new PagedJobSearchSource.Page(
                    new JobFetchResult(postings, List.of("jooble"), List.of()), 50);
              };
          JTabbedPane tabs =
              FinderApplication.createLiveView(
                  new DemoJobSource(),
                  Optional.of(index),
                  Runnable::run,
                  Runnable::run,
                  link -> {},
                  "Company boards");
          Container panel = (Container) tabs.getComponentAt(tabs.indexOfTab("Jooble Canada"));
          JTextField keywords = fieldWithLabel(panel, "Keywords (required)");
          JTextField company = fieldWithLabel(panel, "Company (optional)");
          JTextField location = fieldWithLabel(panel, "Location (optional)");
          company.setText("  aMaZoN  ");
          location.setText("Toronto");
          keywords.setText("   ");
          JButton more = button(panel, "Load more");
          assertFalse(more.isEnabled());
          company.postActionEvent();
          assertTrue(pages.isEmpty());
          assertTrue(text(panel).contains("Enter at least one keyword."));
          keywords.setText("software");
          company.postActionEvent();
          JTable table = components(panel, JTable.class).get(0);
          assertEquals(10, table.getRowCount());
          assertTrue(more.isEnabled());
          assertEquals(List.of(1), pages);
          table.setRowSelectionInterval(0, 0);
          more.doClick();
          assertEquals(20, table.getRowCount());
          assertEquals(List.of(1), pages);
          more.doClick();
          assertEquals(30, table.getRowCount());
          assertEquals(List.of(1, 2), pages);
          more.doClick();
          assertEquals(40, table.getRowCount());
          assertEquals(0, table.getSelectedRow());
          assertEquals(List.of(1, 2), pages);
          more.doClick();
          assertEquals(44, table.getRowCount());
          int row = 0;
          for (int id = 1; id <= 48; id++) {
            if (id % 10 != 0) {
              assertEquals("Software Intern " + id, table.getValueAt(row++, 0));
            }
          }
          assertFalse(more.isEnabled());
          assertEquals(List.of(1, 2), pages);
          button(panel, "Search").doClick();
          assertEquals(List.of(1, 2, 1), pages);
          company.setText("Cresta");
          assertFalse(more.isEnabled());
          assertEquals(0, table.getRowCount());
          return null;
        });
  }

  @Test
  void missingCredentialsLeaveCompanySearchAvailable() throws Exception {
    onEdt(
        () -> {
          JTabbedPane tabs =
              FinderApplication.createLiveView(
                  new DemoJobSource(),
                  Optional.empty(),
                  Runnable::run,
                  Runnable::run,
                  url -> {},
                  "Company boards");
          Container board = (Container) tabs.getComponentAt(0);
          components(board, JTextField.class).get(0).setText("python");
          button(board, "Search").doClick();
          assertEquals(2, components(board, JTable.class).get(0).getRowCount());
          JLabel jooble = (JLabel) tabs.getComponentAt(tabs.indexOfTab("Jooble Canada"));
          assertTrue(jooble.getText().contains("JOOBLE_API_KEY"));
          assertEquals(2, tabs.getTabCount());
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
