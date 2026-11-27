package interface_adapter.search_jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.JobPosting;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import use_case.port.SourceFailure;
import use_case.search_jobs.SearchJobsOutputData;

class SearchJobsPresentationTest {
  @Test
  void presenterSchedulesDisplayStateAndFormatsMissingLocation() {
    SearchJobsViewModel model = new SearchJobsViewModel();
    Queue<Runnable> uiTasks = new ArrayDeque<>();
    AtomicInteger events = new AtomicInteger();
    model.addPropertyChangeListener(event -> events.incrementAndGet());
    SearchJobsPresenter presenter = new SearchJobsPresenter(model, uiTasks::add);
    JobPosting job =
        new JobPosting.Builder("greenhouse", "a", "1")
            .title("Intern")
            .company("A")
            .applicationUrl("https://example.com")
            .build();
    presenter.prepareSuccessView(new SearchJobsOutputData(List.of(job), List.of()));
    assertEquals(0, events.get());
    uiTasks.remove().run();
    assertEquals(1, events.get());
    assertEquals("Not specified", model.getState().getRows().get(0).getLocation());
    assertEquals("", job.getLocation());
    assertEquals("1 matching internship.", model.getState().getMessage());
  }

  @Test
  void zeroMatchesRetainsIncompleteWarningAndFailureClearsStaleRows() {
    SearchJobsViewModel model = new SearchJobsViewModel();
    SearchJobsPresenter presenter = new SearchJobsPresenter(model, Runnable::run);
    presenter.prepareSuccessView(
        new SearchJobsOutputData(
            List.of(), List.of(new SourceFailure("missing", SourceFailure.Reason.TIMEOUT))));
    assertTrue(model.getState().getMessage().startsWith("0 matches"));
    assertTrue(model.getState().getWarning().contains("missing"));
    assertFalse(model.getState().isError());
    presenter.prepareFailView("Try again.");
    assertTrue(model.getState().isError());
    assertFalse(model.getState().isLoading());
    assertTrue(model.getState().getRows().isEmpty());
  }

  @Test
  void controllerQueuesWorkSuppressesConcurrentSubmissionsAndAllowsRetry() {
    SearchJobsViewModel model = new SearchJobsViewModel();
    Queue<Runnable> workerTasks = new ArrayDeque<>();
    Queue<Runnable> uiTasks = new ArrayDeque<>();
    SearchJobsPresenter presenter = new SearchJobsPresenter(model, uiTasks::add);
    AtomicInteger calls = new AtomicInteger();
    SearchJobsController controller =
        new SearchJobsController(
            input -> {
              calls.incrementAndGet();
              assertEquals("java", input.getQuery());
              presenter.prepareSuccessView(new SearchJobsOutputData(List.of(), List.of()));
            },
            model,
            workerTasks::add,
            uiTasks::add);
    controller.execute("java");
    controller.execute("ignored");
    assertEquals(0, calls.get());
    assertEquals(1, workerTasks.size());
    workerTasks.remove().run();
    assertTrue(model.getState().isLoading());
    uiTasks.remove().run();
    assertFalse(model.getState().isLoading());
    controller.execute("java");
    assertEquals(1, workerTasks.size());
  }

  @Test
  void unexpectedFailureAndRejectedWorkerBothReleaseLoadingState() {
    SearchJobsViewModel model = new SearchJobsViewModel();
    SearchJobsController broken =
        new SearchJobsController(
            input -> {
              throw new IllegalStateException("unexpected failure");
            },
            model,
            Runnable::run,
            Runnable::run);
    broken.execute("java");
    assertFalse(model.getState().isLoading());
    assertTrue(model.getState().isError());
    Executor unavailable =
        task -> {
          throw new RejectedExecutionException();
        };
    new SearchJobsController(input -> {}, model, unavailable, Runnable::run).execute("java");
    assertFalse(model.getState().isLoading());
    assertTrue(model.getState().getMessage().contains("reopen"));
  }
}
