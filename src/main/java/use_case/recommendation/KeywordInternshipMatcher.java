package use_case.recommendation;

import entity.JobPosting;
import entity.QualificationProfile;
import entity.WorkArrangement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import use_case.common.InternshipClassifier;

/**
 * Ranks internship titles and descriptions by the number of distinct profile skills they mention.
 */
public final class KeywordInternshipMatcher implements InternshipMatcher {
  private static final Comparator<InternshipMatch> ORDER =
      Comparator.comparingInt((InternshipMatch match) -> match.getMatchedSkills().size())
          .reversed()
          .thenComparing(match -> match.getPosting().getTitle(), String.CASE_INSENSITIVE_ORDER)
          .thenComparing(match -> match.getPosting().getCompany(), String.CASE_INSENSITIVE_ORDER)
          .thenComparing(match -> match.getPosting().getStableId());

  @Override
  public List<InternshipMatch> rank(QualificationProfile profile, List<JobPosting> postings) {
    Map<String, Pattern> skills = new LinkedHashMap<>();
    profile.getSkills().forEach(skill -> skills.put(skill, term(skill)));
    List<Pattern> roles = patterns(profile.getPreferredRoles());
    List<Pattern> locations = patterns(profile.getPreferredLocations());
    Map<String, InternshipMatch> matches = new LinkedHashMap<>();
    for (JobPosting job : postings) {
      if (!InternshipClassifier.isInternship(job.getTitle())
          || !allows(roles, job.getTitle())
          || !allows(locations, job.getLocation())
          || !allowsArrangement(profile, job.getWorkArrangement())) {
        continue;
      }
      List<String> matched =
          skills.entrySet().stream()
              .filter(entry -> mentionsSkill(job, entry.getValue()))
              .map(Map.Entry::getKey)
              .collect(Collectors.toList());
      if (!matched.isEmpty()) {
        matches.putIfAbsent(job.getStableId(), new InternshipMatch(job, matched));
      }
    }
    return matches.values().stream().sorted(ORDER).collect(Collectors.toUnmodifiableList());
  }

  private static boolean allowsArrangement(QualificationProfile profile, WorkArrangement mode) {
    return mode == WorkArrangement.UNKNOWN
        || profile.getPreferredArrangement().orElse(mode) == mode;
  }

  private static boolean mentionsSkill(JobPosting posting, Pattern skill) {
    return skill.matcher(posting.getTitle()).find()
        || skill.matcher(posting.getDescriptionText()).find();
  }

  private static List<Pattern> patterns(List<String> terms) {
    return terms.stream().map(KeywordInternshipMatcher::term).collect(Collectors.toList());
  }

  private static boolean allows(List<Pattern> preferences, String text) {
    return preferences.isEmpty()
        || preferences.stream().anyMatch(pattern -> pattern.matcher(text).find());
  }

  private static Pattern term(String term) {
    String phrase =
        Arrays.stream(term.split(" ")).map(Pattern::quote).collect(Collectors.joining("\\s+"));
    // Include + and # in token boundaries so C, C# and C++ remain distinct skills.
    return Pattern.compile(
        "(?<![\\p{L}\\p{N}_+#])" + phrase + "(?![\\p{L}\\p{N}_+#])",
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
  }
}
