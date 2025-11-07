package quiz_system.entity;

import java.util.List;

public final class Question {
    private final int questionId;
    private final String text;
    private final List<AnswerOption> options; // must be 4
    private final int correctOptionId;

    public Question(int questionId, String text, List<AnswerOption> options, int correctOptionId) {
        if (options == null || options.size() != 4) {
            throw new IllegalArgumentException("Question must have exactly 4 options");
        }

        boolean containsCorrect = options.stream()
                .anyMatch(o -> o.getOptionId() == correctOptionId);

        if (!containsCorrect) {
            throw new IllegalArgumentException("correctOptionId must match one of the options");
        }

        this.questionId = questionId;
        this.text = text;
        this.options = List.copyOf(options); // makes the internal list read-only
        this.correctOptionId = correctOptionId;
    }

    public int getQuestionId() { return questionId; }
    public String getText() { return text; }
    public List<AnswerOption> getOptions() { return options; }
    public int getCorrectOptionId() { return correctOptionId; }

    public AnswerOption getOptionById(int optionId) {
        return options.stream()
                .filter(o -> o.getOptionId() == optionId)
                .findFirst()
                .orElse(null);
    }
}
