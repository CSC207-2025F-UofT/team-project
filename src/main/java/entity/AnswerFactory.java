package entity;

import java.util.List;

public class AnswerFactory {

    public Answer create(String primaryAnswer, List<String> acceptedVariants, int score) {
        return new Answer(primaryAnswer, acceptedVariants, score);
    }
}
