package use_case.common;

import java.util.regex.Pattern;

/** Shared title-based internship policy; does not infer eligibility or match "Internal". */
public final class InternshipClassifier {
  private static final Pattern INDICATOR =
      Pattern.compile(
          "(?<![\\p{L}\\p{N}])(?:intern(?:ship)?s?|co[\\p{Z}\\p{Pd}\\s]?op)(?![\\p{L}\\p{N}])",
          Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

  private InternshipClassifier() {}

  /** Returns whether a title contains an explicit intern, internship or co-op indicator. */
  public static boolean isInternship(String title) {
    return title != null && INDICATOR.matcher(title).find();
  }
}
