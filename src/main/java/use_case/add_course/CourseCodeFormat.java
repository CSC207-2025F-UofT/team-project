package use_case.add_course;

import java.util.regex.Pattern;

/**
 * Format checks for UofT St. George course codes, done before any API call.
 * Accepts "CSC207" or "CSC207H1" / "MAT237Y1" (case-insensitive, spaces ignored).
 */
public final class CourseCodeFormat {

    private static final Pattern COURSE_CODE = Pattern.compile("^[A-Z]{3}\\d{3}([HY]\\d)?$");

    private CourseCodeFormat() {
    }

    /**
     * Uppercases the input and removes all whitespace, e.g. " csc 207 " becomes "CSC207".
     * @param rawInput the text the student typed
     * @return the normalized course code
     */
    public static String normalize(String rawInput) {
        if (rawInput == null) {
            return "";
        }
        return rawInput.replaceAll("\\s+", "").toUpperCase();
    }

    /**
     * Returns true if the normalized code looks like a UofT course code.
     * @param normalizedCode a course code after normalize()
     * @return true if the format is valid
     */
    public static boolean isValidFormat(String normalizedCode) {
        return COURSE_CODE.matcher(normalizedCode).matches();
    }
}