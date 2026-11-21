package data_access;

import entity.Student;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.course_analysis.AnalysisDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

import java.time.Duration;

import java.io.IOException;
import org.xml.sax.SAXException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.io.ByteArrayInputStream;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
                                                     LoginUserDataAccessInterface,
                                                     ChangePasswordUserDataAccessInterface,
                                                     LogoutUserDataAccessInterface,
        AnalysisDataAccessInterface {

    private final Map<String, Student> users = new HashMap<>();

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(Student student) {
        users.put(student.getName(), student);
    }

    @Override
    public Student get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void changePassword(Student student) {
        // Replace the old entry with the new password
        users.put(student.getName(), student);
    }

    @Override
    public String getCourseDescription(String courseCode) {
        String BASE_URL =
                "https://api.easi.utoronto.ca/ttb/getCoursesByCodeAndSectionCode/";

        HttpClient client = HttpClient.newHttpClient();

        String encoded = URLEncoder.encode(courseCode, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + encoded))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(response.body().getBytes(StandardCharsets.UTF_8)));

            NodeList node = doc.getElementsByTagName("description");

            return node.item(0).getTextContent().trim();

        } catch (IOException | InterruptedException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getSkill(String description) {

        String BASE_URL =
                "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent";

        HttpClient client = HttpClient.newHttpClient();

        String safePrompt = description.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        String jsonPayload = "{%n" +
                             "  \"contents\": [{%n" +
                             "    \"parts\": [{%n" +
                             "      \"text\": \"%s\"%n" +
                             "    }]%n" +
                             "  }]%n" +
                             "}%n".formatted(safePrompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("x-goog-api-key", System.getenv("API_KEY"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
