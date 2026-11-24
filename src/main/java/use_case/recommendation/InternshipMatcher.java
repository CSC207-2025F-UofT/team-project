package use_case.recommendation;

import entity.JobPosting;
import entity.QualificationProfile;
import java.util.List;

/** Strategy for ranking postings against a saved qualification profile. */
@FunctionalInterface
public interface InternshipMatcher {
  /** Returns ordered suggestions and the skills that explain each match. */
  List<InternshipMatch> rank(QualificationProfile profile, List<JobPosting> postings);
}
