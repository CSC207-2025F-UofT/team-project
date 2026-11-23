package data_access.question;

import java.io.IOException;
import java.net.URI;
import use_case.question.QuestionLoadException;

/** Transport seam for deterministic API tests without a live service. */
@FunctionalInterface
public interface QuestionHttpClient {
    String get(URI uri) throws IOException, InterruptedException, QuestionLoadException;
}
