package entities;

public class Card {
    private String question;
    private String answer;
    private boolean questionFacingUp;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
        questionFacingUp = true;
    }

    public String get_question() {
        return question;
    }

    public String get_answer() {
        return answer;
    }

    public void flip() {
        questionFacingUp = !questionFacingUp;
    }

    public boolean getQuestionFacingUp() {
        return questionFacingUp;
    }
}
