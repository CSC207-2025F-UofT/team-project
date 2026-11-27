package use_case.port;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import entity.JobPosting;
import entity.WorkArrangement;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class JobContractTest {
  @Test
  void snapshotIdentityIncludesBoardAndSurvivesTitleChanges() {
    JobPosting.Builder builder =
        new JobPosting.Builder("greenhouse", "a", "1")
            .title("Original Intern")
            .company("A")
            .applicationUrl("https://example.com/1");
    JobPosting old = builder.build();
    JobPosting updated = builder.title("Updated Intern").build();
    JobPosting anotherBoard =
        new JobPosting.Builder("greenhouse", "b", "1")
            .title("Original Intern")
            .company("B")
            .applicationUrl("https://example.com/1")
            .build();
    assertEquals(old.getStableId(), updated.getStableId());
    assertNotEquals(old.getStableId(), anotherBoard.getStableId());
    assertEquals("Original Intern", old.getTitle());
    assertEquals("", old.getLocation());
    assertEquals(WorkArrangement.UNKNOWN, old.getWorkArrangement());
  }

  @Test
  void defensiveCopiesPreventConsumersFromReorderingSharedCollections() {
    List<JobPosting> jobs = new ArrayList<>();
    List<String> sources = new ArrayList<>(List.of("a"));
    List<SourceFailure> failures =
        new ArrayList<>(List.of(new SourceFailure("b", SourceFailure.Reason.NETWORK)));
    JobFetchResult result = new JobFetchResult(jobs, sources, failures);
    sources.clear();
    failures.clear();
    assertEquals(1, result.getSuccessCount());
    assertEquals(List.of("b"), result.getFailedBoards());
    assertThrows(UnsupportedOperationException.class, () -> result.getJobs().clear());
    assertThrows(UnsupportedOperationException.class, () -> result.getFailures().clear());
    assertThrows(UnsupportedOperationException.class, () -> result.getFailedBoards().clear());
  }

  @Test
  void noSuccessfulSourceCannotBeRepresentedAsSuccess() {
    assertThrows(
        IllegalArgumentException.class, () -> new JobFetchResult(List.of(), List.of(), List.of()));
  }
}
