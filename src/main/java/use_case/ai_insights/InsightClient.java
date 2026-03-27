package use_case.ai_insights;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InsightClient {

    private static final String MODEL = "gemini-2.5-flash";
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");

    public String generateInsight(String prompt) {

        if (API_KEY == null) {
            throw new IllegalStateException("Missing GEMINI_API_KEY environment variable.");
        }

        String body = """
        {
          "contents": [
            {
              "parts": [
                { "text": "%s" }
              ]
            }
          ]
        }  \s
       \s""".formatted(prompt.replace("\"", "'"));

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1/models/"
                            + MODEL + ":generateContent?key=" + API_KEY))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body().trim();

        } catch (Exception e) {
            return "Error contacting Gemini: " + e.getMessage();
        }
    }

}
