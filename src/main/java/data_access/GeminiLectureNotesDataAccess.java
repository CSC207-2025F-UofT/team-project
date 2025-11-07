package data_access;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class GeminiLectureNotesDataAccess implements LectureNotesDataAccessInterface {

    // Model used for the Gemini API
    private static final String MODEL_NAME = "gemini-2.5-flash";
    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL_NAME + ":generateContent";

    private final HttpClient httpClient;
    private final String apiKey;

    public GeminiLectureNotesDataAccess() {
        // Read the API key from the environment
        this.apiKey = System.getenv("GOOGLE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GOOGLE_API_KEY environment variable is not set.");
        }
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String generateLectureNotes(String courseId,
                                       List<String> filePaths,
                                       String topic)
            throws LectureNotesDataAccessException {

        String prompt = buildPrompt(courseId, topic, filePaths);
        String requestJson = buildRequestBody(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                // Give up after 30 seconds so we don't hang forever
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new LectureNotesDataAccessException(
                        "Gemini API returned status " + response.statusCode());
            }

            String body = response.body();
            String text = extractAllText(body);
            if (text == null || text.isBlank()) {
                throw new LectureNotesDataAccessException("Failed to parse Gemini response.");
            }
            return text;

        } catch (InterruptedException e) {
            // Restore interrupt status and wrap in our domain exception
            Thread.currentThread().interrupt();
            throw new LectureNotesDataAccessException(
                    "Error while calling Gemini API.", e);
        } catch (IOException e) {
            throw new LectureNotesDataAccessException(
                    "Error while calling Gemini API.", e);
        }
    }

    // Build the JSON request body: contents -> parts -> text
    private String buildRequestBody(String prompt) {
        String escaped = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        return "{ \"contents\": [ { \"parts\": [ { \"text\": \"" +
                escaped + "\" } ] } ] }";
    }

    // Extract all "text" chunks from the JSON and join them
    private String extractAllText(String json) {
        StringBuilder combined = new StringBuilder();
        int index = 0;

        int keyPos = json.indexOf("\"text\"", index);
        while (keyPos != -1) {
            int quoteStart = json.indexOf('"', keyPos + 6);
            if (quoteStart == -1) {
                // no more complete "text" strings
                break;
            }

            StringBuilder part = new StringBuilder();
            index = readJsonString(json, quoteStart, part);
            combined.append(part).append('\n');

            // look for the next "text" after this one
            keyPos = json.indexOf("\"text\"", index);
        }

        String result = combined.toString().trim();
        return result.isEmpty() ? null : result;
    }


    /**
     * Read a JSON string starting at the given quote.
     * Handles \n, \t, \", \\ escapes and stops at the first unescaped quote.
     * Returns the index just after the closing quote.
     */
    private int readJsonString(String json, int quoteStart, StringBuilder out) {
        int i = quoteStart + 1;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                i = handleEscape(json.charAt(i + 1), out, i);
            } else if (c == '"') {
                return i + 1;
            } else {
                out.append(c);
                i++;
            }
        }
        return i;
    }

    private int handleEscape(char esc, StringBuilder out, int index) {
        char decoded;
        switch (esc) {
            case 'n':
                decoded = '\n';
                break;
            case 't':
                decoded = '\t';
                break;
            case '"':
                decoded = '"';
                break;
            case '\\':
                decoded = '\\';
                break;
            default:
                decoded = esc;
                break;
        }
        out.append(decoded);
        return index + 2;
    }

    // Prompt is written for general students, not a specific course code
    private String buildPrompt(String courseId,
                               String topic,
                               List<String> filePaths) {
        StringBuilder sb = new StringBuilder();

        if (courseId != null && !courseId.isBlank()) {
            sb.append("You are a helpful teaching assistant for the course ")
                    .append(courseId)
                    .append(".\n");
        } else {
            sb.append("You are a helpful teaching assistant who helps students learn various subjects.\n");
        }

        sb.append("Generate clear, structured lecture notes on the topic: ")
                .append(topic)
                .append(".\n\n");

        sb.append("Requirements:\n");
        sb.append("- Use headings and bullet points.\n");
        sb.append("- Explain key concepts, definitions, and important formulas.\n");
        sb.append("- Include 1–2 short, simple examples.\n");
        sb.append("- Make the notes suitable for learners at an introductory level (no course-specific assumptions).\n");
        sb.append("- End with a short concluding paragraph that summarizes the key ideas.\n");

        if (filePaths != null && !filePaths.isEmpty()) {
            sb.append("\nThe user has selected the following resource files related to this topic:\n");
            for (String path : filePaths) {
                sb.append("- ").append(path).append('\n');
            }
            sb.append("You cannot see the actual contents of the files, ")
                    .append("but you should assume they are slides and readings that support the topic.\n");
        }

        return sb.toString();
    }
}
