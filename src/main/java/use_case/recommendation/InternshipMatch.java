package use_case.recommendation;

import entity.JobPosting;
import java.util.List;

/** A posting paired with the distinct profile skills that matched it. */
public final class InternshipMatch {
  private final JobPosting posting;
  private final List<String> matchedSkills;

  /** Preserves the posting and matched skills in profile order. */
  public InternshipMatch(JobPosting posting, List<String> matchedSkills) {
    this.posting = posting;
    this.matchedSkills = List.copyOf(matchedSkills);
  }

  public JobPosting getPosting() {
    return posting;
  }

  public List<String> getMatchedSkills() {
    return matchedSkills;
  }
}
