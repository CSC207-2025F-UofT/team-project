package entity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** An immutable multiple-choice question independent of its provider. */
public final class Question {
    private final String id;
    private final String text;
    private final List<String> options;
    private final int correctAnswer;
    private final String explanation;

    public Question(String id, String text, List<String> options, int correctAnswer,
                    String explanation) {
        this.id = requireText(id);
        this.text = requireText(text);
        this.options = List.copyOf(Objects.requireNonNull(options));
        if (this.options.size() < 2 || this.options.stream().anyMatch(String::isBlank)
                || correctAnswer < 0 || correctAnswer >= this.options.size()) {
            throw new IllegalArgumentException("Invalid answer options or answer index");
        }
        this.correctAnswer = correctAnswer;
        this.explanation = explanation == null ? "" : explanation.strip();
    }

    private static String requireText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Question ID and text must be present");
        }
        return value;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswer() { return correctAnswer; }
    public boolean isCorrect(int answer) { return answer == correctAnswer; }
    public Optional<String> getExplanation() {
        return explanation.isEmpty() ? Optional.empty() : Optional.of(explanation);
    }

    /** A simple exam hint can eliminate the first incorrect option. */
    public int getEliminatedOption() {
        return correctAnswer == 0 ? 1 : 0;
    }
}
