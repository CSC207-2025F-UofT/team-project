package use_case.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.JobPosting;
import entity.QualificationProfile;
import entity.User;
import entity.WorkArrangement;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import use_case.profile.ProfileData;

class KeywordInternshipMatcherTest {
  private final InternshipMatcher matcher = new KeywordInternshipMatcher();

  @Test
  void ranksDistinctSkillsWithStableTiesAndKeepsBoardScopedIdentity() {
    QualificationProfile profile = profile("Java, python, JAVA", "", "", "ANY");
    JobPosting one = job("a", "1", "Web Intern", "Ottawa", "Java Java", WorkArrangement.HYBRID);
    JobPosting two = job("b", "1", "Web Intern", "", "python and JAVA", WorkArrangement.UNKNOWN);
    JobPosting alpha = job("a", "9", "Cloud Co-op", "", "python", WorkArrangement.REMOTE);
    JobPosting other =
        job("a", "3", "Internal Tools Engineer", "", "Java python", WorkArrangement.REMOTE);
    List<JobPosting> input = List.of(one, other, two, alpha, one);

    List<InternshipMatch> result = matcher.rank(profile, input);

    assertEquals(List.of(two, alpha, one), postings(result));
    assertEquals(List.of("Java", "python"), result.get(0).getMatchedSkills());
    assertEquals(List.of("Java"), result.get(2).getMatchedSkills());
    assertEquals(List.of(one, other, two, alpha, one), input);
  }

  @Test
  void appliesAnyRoleAndAnyLocationButRequiresEachSpecifiedPreferenceCategory() {
    QualificationProfile profile =
        profile("sql", "Data Science, Backend", "Toronto, Ottawa", "REMOTE");
    JobPosting unknown =
        job("a", "1", "Backend Intern", "Ottawa, ON", "SQL", WorkArrangement.UNKNOWN);
    JobPosting remote =
        job("a", "2", "Data Science Co-op", "Toronto", "sql", WorkArrangement.REMOTE);
    JobPosting wrongMode =
        job("a", "3", "Backend Intern", "Toronto", "sql", WorkArrangement.HYBRID);
    JobPosting wrongRole =
        job("a", "4", "Marketing Intern", "Ottawa", "sql", WorkArrangement.REMOTE);
    JobPosting wrongCity = job("a", "5", "Data Science Intern", "", "sql", WorkArrangement.UNKNOWN);
    JobPosting noSkill = job("a", "6", "Backend Intern", "Ottawa", "Go", WorkArrangement.REMOTE);
    assertEquals(
        List.of(unknown, remote),
        postings(
            matcher.rank(
                profile, List.of(wrongMode, wrongRole, wrongCity, noSkill, remote, unknown))));
  }

  @Test
  void treatsPunctuationAndMultipleWordSkillsLiterallyAndCountsTitleSkills() {
    QualificationProfile profile = profile("C, c++, C#, Java, data science, .NET", "", "", "any");
    JobPosting job =
        job(
            "a",
            "1",
            "C++ Co-op",
            "",
            "JavaScript with C# and DATA   SCIENCE on .NET",
            WorkArrangement.UNKNOWN);
    assertEquals(
        List.of("c++", "C#", "data science", ".NET"),
        matcher.rank(profile, List.of(job)).get(0).getMatchedSkills());
    assertTrue(
        matcher
            .rank(
                profile,
                List.of(job("a", "2", "Web Intern", "", "JavaScript", WorkArrangement.REMOTE)))
            .isEmpty());
  }

  private static List<JobPosting> postings(List<InternshipMatch> matches) {
    return matches.stream().map(InternshipMatch::getPosting).collect(Collectors.toList());
  }

  private static QualificationProfile profile(
      String skills, String roles, String locations, String mode) {
    return new ProfileData("CS", "", skills, roles, locations, mode).toProfile(new User());
  }

  private static JobPosting job(
      String board,
      String id,
      String title,
      String location,
      String description,
      WorkArrangement mode) {
    return new JobPosting.Builder("greenhouse", board, id)
        .title(title)
        .company("Example")
        .location(location)
        .descriptionText(description)
        .workArrangement(mode)
        .applicationUrl("https://example.org/apply/" + id)
        .build();
  }
}
