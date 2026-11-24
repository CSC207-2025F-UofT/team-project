package use_case.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import entity.JobPosting;
import entity.QualificationProfile;
import entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.SourceFailure;
import use_case.profile.ProfileData;
import use_case.profile.ProfileRepository;
import use_case.profile.SavedProfile;

/** Exercises the primary MVP use case through its boundaries without Swing, files or HTTP. */
class RecommendationInteractorTest {
  @Test
  void missingProfileReturnsBeforeRetrievingPublicPostingsOrMatching() {
    Output output = new Output();
    RecommendationInteractor interactor =
        new RecommendationInteractor(
            new MemoryProfiles(),
            () -> fail("Source called without a profile"),
            (profile, jobs) -> fail("Matcher called"),
            output);
    interactor.execute();
    assertTrue(output.error.contains("Create and save a profile"));
    assertNull(output.results);
  }

  @Test
  void forwardsTheLatestProfileAndSharedPostingsToTheStrategyAndRetainsBoardWarnings() {
    MemoryProfiles profiles = new MemoryProfiles();
    final SavedProfile saved = profiles.create(profile("Java"));
    JobPosting posting =
        new JobPosting.Builder("greenhouse", "board", "1")
            .title("Intern")
            .company("Company")
            .applicationUrl("https://example.org/job")
            .build();
    JobSource jobs =
        () ->
            new JobFetchResult(
                List.of(posting),
                List.of("board"),
                List.of(new SourceFailure("unavailable", SourceFailure.Reason.NETWORK)));
    InternshipMatcher matcher =
        (current, fetched) -> {
          assertSame(posting, fetched.get(0));
          return List.of(new InternshipMatch(fetched.get(0), current.getSkills()));
        };
    Output output = new Output();
    RecommendationInteractor interactor =
        new RecommendationInteractor(profiles, jobs, matcher, output);
    interactor.execute();
    assertEquals(List.of("Java"), output.results.getMatches().get(0).getMatchedSkills());
    profiles.update(profile("SQL"), saved.getRevision());
    interactor.execute();
    assertEquals(List.of("SQL"), output.results.getMatches().get(0).getMatchedSkills());
    assertEquals(List.of("unavailable"), output.results.getFailedBoards());
    assertNull(output.error);
  }

  private static QualificationProfile profile(String skill) {
    return new ProfileData("CS", "", skill, "", "", "ANY").toProfile(new User("local", "", ""));
  }

  private static final class MemoryProfiles implements ProfileRepository {
    private SavedProfile value;

    @Override
    public Optional<SavedProfile> loadSnapshot() {
      return Optional.ofNullable(value);
    }

    @Override
    public SavedProfile create(QualificationProfile profile) {
      value = new SavedProfile(profile, UUID.randomUUID());
      return value;
    }

    @Override
    public SavedProfile update(QualificationProfile profile, UUID expectedRevision) {
      assertEquals(expectedRevision, value.getRevision());
      return create(profile);
    }
  }

  private static final class Output implements RecommendationOutputBoundary {
    private RecommendationOutputData results;
    private String error;

    @Override
    public void presentResults(RecommendationOutputData data) {
      results = data;
    }

    @Override
    public void presentError(String message) {
      error = message;
    }
  }
}
