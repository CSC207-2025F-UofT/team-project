package data_access.question;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import use_case.question.QuestionLoadException;

public final class JdkQuestionHttpClient implements QuestionHttpClient {
    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();

    @Override
    public String get(URI uri) throws IOException, InterruptedException, QuestionLoadException {
        HttpRequest request = HttpRequest.newBuilder(uri).timeout(TIMEOUT)
                .header("Accept", "application/json").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new QuestionLoadException("Question service returned HTTP " + response.statusCode()
                    + ". Please try again later.");
        }
        return response.body();
    }
}
