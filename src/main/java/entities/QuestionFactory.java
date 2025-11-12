package entities;

import java.util.List;

public class QuestionFactory {
    public Question createQuestion(String prompt, List<String> choices, int correctIndex) {
        return new Question(prompt, choices, correctIndex);
    }
}
