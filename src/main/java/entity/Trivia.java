package entity;

import java.util.List;

/** Retained question. The expected answer is deliberately absent from its public data. */
public final class Trivia {
    private final String prompt;
    private final List<String> choices;
    private final String expectedAnswer;

    public Trivia(String prompt, List<String> choices, String expectedAnswer) {
        if (prompt == null || prompt.isBlank() || expectedAnswer == null || expectedAnswer.isBlank()
                || choices == null || choices.stream().anyMatch(value -> value == null || value.isBlank())) {
            throw new IllegalArgumentException("A question requires a prompt, valid choices and an answer");
        }
        if (choices.stream().distinct().count() != choices.size()) throw new IllegalArgumentException("Duplicate choices");
        this.prompt = prompt;
        this.choices = List.copyOf(choices);
        this.expectedAnswer = expectedAnswer;
    }

    public String prompt() { return prompt; }
    public List<String> choices() { return choices; }
    public boolean isCorrect(String answer) {
        return answer != null && expectedAnswer.trim().equalsIgnoreCase(answer.trim());
    }
}
