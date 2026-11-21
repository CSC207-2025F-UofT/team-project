package use_case.add_question;

import java.util.List;

/**
 * Input Data object containing all details required to add a new Question.
 */
public class AddQuestionInputData {
    private final String questionText;
    private final List<String> primaryAnswers;
    private final List<Integer> scores;
    private final List<List<String>> acceptedVariants;

    public AddQuestionInputData(String questionText,
                                List<String> primaryAnswers,
                                List<Integer> scores,
                                List<List<String>> acceptedVariants) {
        this.questionText = questionText;
        this.primaryAnswers = primaryAnswers;
        this.scores = scores;
        this.acceptedVariants = acceptedVariants;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getPrimaryAnswers() {
        return primaryAnswers;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public List<List<String>> getAcceptedVariants() {
        return acceptedVariants;
    }
}
