//Entity for multiple choice questions
//Mahir
package entity;

import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private final List<String> options;
    private final String correctAnswer;

    public MultipleChoiceQuestion(String questionText, int difficultyLevel,List<String> options, String correctAnswer){
        super(questionText,difficultyLevel);
        this.options = options;
        this.correctAnswer = correctAnswer;
    }


    @Override
    public boolean checkAnswer(String userAnswer) {
        return correctAnswer.equalsIgnoreCase(userAnswer.trim());
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
