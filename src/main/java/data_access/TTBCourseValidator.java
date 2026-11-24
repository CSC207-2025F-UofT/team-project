package data_access;

import use_case.add_course.CourseValidator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TTBCourseValidator  implements CourseValidator {
    private static final String API_URL = "https://api.easi.utoronto.ca/ttb/getCoursesByCodeAndSectionCode/";
    @Override
    public String findFullCode(String courseCode){
        // Case 1: the student typed the full code
        if (courseCode.endsWith("H1") || courseCode.endsWith("Y1")) {
            if (courseExists(courseCode)) {
                return courseCode;
            } else {
                return null;
            }
        }

        // Case 2: the student typed only eg."CSC207"
        String halfYearCode = courseCode + "H1";
        if (courseExists(halfYearCode)) {
            return halfYearCode;
        }
        // If "H1" did not work, we try "Y1" (a full-year course).
        String fullYearCode = courseCode + "Y1";
        if (courseExists(fullYearCode)) {
            return fullYearCode;
        }
        // Neither one exists, so this course is not real.
        return null;

    }
     /*
      * Asks the TTB API whether a course with this full code exists.
      */


    private boolean courseExists(String fullCode) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + fullCode))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            return statusCode == 200;
        }
        catch (Exception e) {
            // Something went wrong (for example, no internet), so we treat it as "not found".
            return false;
        }
    }




}
