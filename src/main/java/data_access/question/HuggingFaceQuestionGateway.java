package data_access.question;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Strictness;
import entity.Question;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import use_case.question.QuestionGateway;
import use_case.question.QuestionLoadException;
import use_case.question.QuestionPurpose;
import use_case.question.QuestionTopic;

/** Loads selected introductory MMLU rows from Hugging Face on every request. */
public final class HuggingFaceQuestionGateway implements QuestionGateway {
    private static final String ENDPOINT = "https://datasets-server.huggingface.co/rows";
    private final QuestionHttpClient http;
    private final Gson gson = new GsonBuilder().setStrictness(Strictness.STRICT).create();

    public HuggingFaceQuestionGateway() {
        this(new JdkQuestionHttpClient());
    }

    public HuggingFaceQuestionGateway(QuestionHttpClient http) {
        this.http = Objects.requireNonNull(http);
    }

    @Override
    public List<Question> getQuestions(QuestionTopic topic, QuestionPurpose purpose, int count)
            throws QuestionLoadException {
        Objects.requireNonNull(topic);
        Objects.requireNonNull(purpose);
        MmluSelection selection = MmluSelection.forCourse(topic, purpose);
        if (count < 1 || count > selection.rows.size()) {
            throw new IllegalArgumentException(purpose + " supports 1 to "
                    + selection.rows.size() + " questions per course");
        }
        URI uri = URI.create(ENDPOINT + "?dataset=cais%2Fmmlu&config=" + selection.subject
                + "&split=test&offset=" + selection.offset + "&length=50");
        final String body;
        try {
            body = http.get(uri);
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new QuestionLoadException("Question loading was interrupted.", exception);
        }
        catch (IOException exception) {
            throw new QuestionLoadException("Cannot reach the question service. Check your connection "
                    + "and retry; no offline questions are available.", exception);
        }
        try {
            return parse(body, selection, count);
        }
        catch (RuntimeException exception) {
            throw new QuestionLoadException("The question service returned incomplete or invalid data. "
                    + "Please try again later.", exception);
        }
    }

    private List<Question> parse(String body, MmluSelection selection, int count) {
        JsonObject root = gson.fromJson(body, JsonObject.class);
        JsonElement partial = root.get("partial");
        if (partial == null || !partial.isJsonPrimitive() || !partial.getAsJsonPrimitive().isBoolean()
                || partial.getAsBoolean()) {
            throw new IllegalArgumentException("Expected a complete response");
        }
        Map<Integer, Question> questions = new HashMap<>();
        List<Integer> wanted = selection.rows.subList(0, count);
        for (JsonElement element : root.getAsJsonArray("rows")) {
            JsonObject entry = element.getAsJsonObject();
            int index = integer(entry.get("row_idx"));
            if (!wanted.contains(index)) {
                continue;
            }
            JsonArray truncated = entry.getAsJsonArray("truncated_cells");
            if (truncated == null || !truncated.isEmpty() || questions.containsKey(index)) {
                throw new IllegalArgumentException("Truncated or duplicate question");
            }
            JsonObject row = entry.getAsJsonObject("row");
            if (!selection.subject.equals(string(row.get("subject")))) {
                throw new IllegalArgumentException("Unexpected subject");
            }
            List<String> options = new ArrayList<>();
            for (JsonElement choice : row.getAsJsonArray("choices")) {
                options.add(string(choice));
            }
            if (options.size() != 4) {
                throw new IllegalArgumentException("MMLU must have four choices");
            }
            questions.put(index, new Question("mmlu:" + selection.subject + ":test:" + index,
                    string(row.get("question")), options, integer(row.get("answer")), null));
        }
        List<Question> result = new ArrayList<>();
        for (Integer index : wanted) {
            if (!questions.containsKey(index)) {
                throw new IllegalArgumentException("A selected remote question is missing");
            }
            result.add(questions.get(index));
        }
        return List.copyOf(result);
    }

    private static int integer(JsonElement value) {
        if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
            throw new IllegalArgumentException("Expected an integer");
        }
        return value.getAsBigDecimal().intValueExact();
    }

    private static String string(JsonElement value) {
        if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("Expected a string");
        }
        return value.getAsString();
    }
}
