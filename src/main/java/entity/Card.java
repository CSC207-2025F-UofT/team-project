package entity;

// how to ensure our card id is unique each time?

public class Card {
    private final int id;
    private String question;
    private String answer;

    public Card(int id, String question, String answer) {
        this.id = id;
        updateContent(question, answer);
    }

    public void updateContent(String question, String answer) {
        String validQuestion = requireText(question, "Question");
        String validAnswer = requireText(answer, "Answer");
        this.question = validQuestion;
        this.answer = validAnswer;
    }

    public int getId() {
        return this.id;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank.");
        }
        return value.strip();
    }
}


