package app;

import data_access.question.HuggingFaceQuestionGateway;
import entity.Question;
import use_case.question.QuestionPurpose;
import use_case.question.QuestionTopic;

/** An opt-in live API check; failures propagate instead of loading local questions. */
public final class QuestionApiCheck {
    private QuestionApiCheck() { }

    public static void main(String[] args) throws Exception {
        HuggingFaceQuestionGateway gateway = new HuggingFaceQuestionGateway();
        for (QuestionTopic topic : QuestionTopic.values()) {
            for (QuestionPurpose purpose : QuestionPurpose.values()) {
                java.util.List<Question> questions = gateway.getQuestions(topic, purpose, 3);
                System.out.println(topic + " " + purpose + ": " + questions.size()
                        + " live questions; first ID=" + questions.get(0).getId());
            }
        }
    }
}
