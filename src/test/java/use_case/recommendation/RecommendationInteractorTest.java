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
import org.junit.jupiter.api.Test;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.SourceFailure;
import use_case.profile.ProfileData;
import use_case.profile.ProfileRepository;

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
    profiles.create(profile("Java"));
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
    profiles.update(profile("SQL"));
    interactor.execute();
    assertEquals(List.of("SQL"), output.results.getMatches().get(0).getMatchedSkills());
    assertEquals(List.of("unavailable"), output.results.getFailedBoards());
    assertNull(output.error);
  }

  private static QualificationProfile profile(String skill) {
    return new ProfileData("CS", "", skill, "", "", "ANY").toProfile(new User("local", "", ""));
  }

  private static final class MemoryProfiles implements ProfileRepository {
    private QualificationProfile value;

    @Override
    public Optional<QualificationProfile> load() {
      return Optional.ofNullable(value);
    }

    @Override
    public void create(QualificationProfile profile) {
      value = profile;
    }

    @Override
    public void update(QualificationProfile profile) {
      value = profile;
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
