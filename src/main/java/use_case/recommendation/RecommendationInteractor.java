package use_case.recommendation;

import entity.QualificationProfile;
import java.util.Optional;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.JobSourceException;
import use_case.profile.ProfileRepository;
import use_case.profile.ProfileStorageException;

/** Loads the latest saved qualifications and ranks postings from Finder's shared source. */
public final class RecommendationInteractor implements RecommendationInputBoundary {
  private final ProfileRepository profiles;
  private final JobSource jobs;
  private final InternshipMatcher matcher;
  private final RecommendationOutputBoundary output;

  /** Injects both data sources, the matching strategy and the presentation boundary. */
  public RecommendationInteractor(
      ProfileRepository profiles,
      JobSource jobs,
      InternshipMatcher matcher,
      RecommendationOutputBoundary output) {
    this.profiles = profiles;
    this.jobs = jobs;
    this.matcher = matcher;
    this.output = output;
  }

  @Override
  public void execute() {
    try {
      Optional<QualificationProfile> profile = profiles.load();
      if (profile.isEmpty()) {
        output.presentError("Create and save a profile to get internship recommendations.");
        return;
      }
      JobFetchResult fetched = jobs.fetch();
      output.presentResults(
          new RecommendationOutputData(
              matcher.rank(profile.get(), fetched.getJobs()), fetched.getFailedBoards()));
    } catch (ProfileStorageException exception) {
      output.presentError(exception.getMessage());
    } catch (JobSourceException exception) {
      output.presentError("Could not retrieve job postings. Please refresh to try again.");
    }
  }
}
