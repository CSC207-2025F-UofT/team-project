package entity;

import java.util.ArrayList;
import java.util.List;

public class QuestionBuilder {
    String questionText;
    List<Answer> correctAnswers;
    AnswerFactory answerFactory;

    public QuestionBuilder() {
        answerFactory = new AnswerFactory();
        correctAnswers = new ArrayList<>();
    }

    public QuestionBuilder setQuestionText(String questionText) {
        this.questionText = questionText;
        return this;
    }

    public QuestionBuilder addAnswer(String primaryAnswer, List<String> acceptedVariants, int score) {
        correctAnswers.add(answerFactory.create(primaryAnswer, acceptedVariants, score));
        return this;
    }

    public Question toQuestion() {
        return new Question(this.questionText, this.correctAnswers);
    }
}
