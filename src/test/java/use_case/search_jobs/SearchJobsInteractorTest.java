package use_case.search_jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.JobPosting;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import use_case.common.InternshipClassifier;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.JobSourceException;
import use_case.port.SourceFailure;

class SearchJobsInteractorTest {
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "\t\n", "\u2003", "\u00a0"})
  void blankInputFailsBeforeRetrieval(String query) {
    AtomicInteger fetches = new AtomicInteger();
    RecordingOutput output = new RecordingOutput();
    JobSource source =
        () -> {
          fetches.incrementAndGet();
          return new JobFetchResult(List.of(), List.of("a"), List.of());
        };
    new SearchJobsInteractor(source, output).execute(new SearchJobsInputData(query));
    assertEquals(0, fetches.get());
    assertEquals("Enter at least one keyword.", output.error);
    assertNull(output.success);
  }

  @Test
  void matchesEveryTermAcrossTitleAndDescriptionWithoutChangingSourceOrder() {
    JobPosting match = job("1", "SOFTWARE Intern", "Build services using Java.");
    List<JobPosting> jobs =
        new ArrayList<>(
            List.of(
                job("2", "Software Intern", "Python only"),
                match,
                job("3", "Internal Software Engineer", "Java"),
                job("4", "Java Co-op", "Develop software")));
    RecordingOutput output =
        run("  softWARE\tjava  ", new JobFetchResult(jobs, List.of("a"), List.of()));
    assertNull(output.error);
    assertEquals(List.of(match, jobs.get(3)), output.success.getJobs());
    assertEquals("2", jobs.get(0).getExternalJobId());
    assertThrows(UnsupportedOperationException.class, () -> output.success.getJobs().clear());
  }

  @Test
  void caseNormalizationDoesNotDependOnSystemLocale() {
    Locale previous = Locale.getDefault();
    try {
      Locale.setDefault(Locale.forLanguageTag("tr-TR"));
      RecordingOutput output =
          run(
              "INTERN",
              new JobFetchResult(List.of(job("1", "Intern", "")), List.of("a"), List.of()));
      assertEquals(1, output.success.getJobs().size());
    } finally {
      Locale.setDefault(previous);
    }
  }

  @Test
  void successfulEmptyBoardIsNotRetrievalFailure() {
    RecordingOutput output = run("java", new JobFetchResult(List.of(), List.of("a"), List.of()));
    assertNotNull(output.success);
    assertTrue(output.success.getJobs().isEmpty());
    assertNull(output.error);
  }

  @Test
  void partialFailureRemainsVisibleWhenNoPostingsMatch() {
    SourceFailure failure = new SourceFailure("offline", SourceFailure.Reason.TIMEOUT);
    RecordingOutput output =
        run(
            "java",
            new JobFetchResult(
                List.of(job("1", "Finance Intern", "Accounting")), List.of("a"), List.of(failure)));
    assertTrue(output.success.getJobs().isEmpty());
    assertEquals(List.of(failure), output.success.getFailures());
    assertNull(output.error);
  }

  @Test
  void completeFailureProducesOnlyAnError() {
    RecordingOutput output = new RecordingOutput();
    new SearchJobsInteractor(
            () -> {
              throw new JobSourceException("All boards failed");
            },
            output)
        .execute(new SearchJobsInputData("java"));
    assertNull(output.success);
    assertTrue(output.error.contains("could not be completed"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "Software Intern", "SUMMER INTERNSHIP", "Engineering Co-op",
        "Engineering Co op", "Engineering Co‑op", "Interns - Finance"
      })
  void recognizesExplicitInternshipIndicators(String title) {
    assertTrue(InternshipClassifier.isInternship(title));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "Internal Tools Engineer",
        "International Sales",
        "Internet Engineer",
        "Cooper",
        "internee"
      })
  void doesNotTreatSubstringsAsInternshipIndicators(String title) {
    assertFalse(InternshipClassifier.isInternship(title));
  }

  private RecordingOutput run(String query, JobFetchResult result) {
    RecordingOutput output = new RecordingOutput();
    new SearchJobsInteractor(() -> result, output).execute(new SearchJobsInputData(query));
    return output;
  }

  private JobPosting job(String id, String title, String description) {
    return new JobPosting.Builder("greenhouse", "a", id)
        .title(title)
        .company("Company A")
        .descriptionText(description)
        .applicationUrl("https://example.com/jobs/" + id)
        .build();
  }

  private static final class RecordingOutput implements SearchJobsOutputBoundary {
    private SearchJobsOutputData success;
    private String error;

    @Override
    public void prepareSuccessView(SearchJobsOutputData value) {
      success = value;
    }

    @Override
    public void prepareFailView(String message) {
      error = message;
    }
  }
}
