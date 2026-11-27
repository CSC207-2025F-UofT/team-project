package use_case.common;

import java.util.regex.Pattern;

/** Shared title-based internship policy; does not infer eligibility or match "Internal". */
public final class InternshipClassifier {
  private static final Pattern INDICATOR =
      Pattern.compile(
          "(?<![\\p{L}\\p{N}])(?:intern(?:ship)?s?|co[\\p{Z}\\p{Pd}\\s]?op)(?![\\p{L}\\p{N}])",
          Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

  private InternshipClassifier() {}

  /** Returns whether a job title contains an explicit internship indicator. */
  public static boolean isInternship(String text) {
    return text != null && INDICATOR.matcher(text).find();
  }
}
