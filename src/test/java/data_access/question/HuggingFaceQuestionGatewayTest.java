package data_access.question;

import entity.Question;
import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import use_case.question.QuestionLoadException;
import use_case.question.QuestionPurpose;
import use_case.question.QuestionTopic;
import static org.junit.jupiter.api.Assertions.*;

class HuggingFaceQuestionGatewayTest {
    private String fixture(MmluSelection selection) {
        StringBuilder rows = new StringBuilder();
        for (int index : selection.rows) {
            if (rows.length() > 0) { rows.append(','); }
            rows.append("{\"row_idx\":").append(index).append(",\"truncated_cells\":[],\"row\":{")
                    .append("\"subject\":\"").append(selection.subject)
                    .append("\",\"question\":\"A test question\",\"choices\":[\"A\",\"B\",\"C\",\"D\"],\"answer\":2}}");
        }
        return "{\"partial\":false,\"rows\":[" + rows + "]}";
    }

    @Test void loadsAllSixTopicsWithDisjointPracticeAndExamIds() throws Exception {
        for (QuestionTopic topic : QuestionTopic.values()) {
            java.util.Set<String> practice = new HashSet<>();
            for (QuestionPurpose purpose : QuestionPurpose.values()) {
                MmluSelection selection = MmluSelection.forCourse(topic, purpose);
                HuggingFaceQuestionGateway gateway = new HuggingFaceQuestionGateway(uri -> {
                    assertEquals("https", uri.getScheme());
                    assertEquals("datasets-server.huggingface.co", uri.getHost());
                    assertTrue(uri.getRawQuery().contains("dataset=cais%2Fmmlu"));
                    assertTrue(uri.getQuery().contains("offset=" + selection.offset));
                    assertFalse(uri.getQuery().contains("difficulty"));
                    return fixture(selection);
                });
                List<Question> questions = gateway.getQuestions(topic, purpose, 3);
                assertEquals(3, questions.size());
                for (Question question : questions) {
                    assertEquals(2, question.getCorrectAnswer());
                    assertTrue(question.getExplanation().isEmpty());
                    assertTrue(practice.add(question.getId()), "Practice and exam must not overlap");
                }
                assertEquals(1, gateway.getQuestions(topic, purpose, 1).size());
            }
        }
    }

    @Test void throwsOnNetworkAndTimeoutWithoutFallback() {
        for (IOException failure : List.of(new IOException("offline"), new HttpTimeoutException("slow"))) {
            HuggingFaceQuestionGateway gateway = new HuggingFaceQuestionGateway(uri -> { throw failure; });
            QuestionLoadException exception = assertThrows(QuestionLoadException.class,
                    () -> gateway.getQuestions(QuestionTopic.ECOLOGY, QuestionPurpose.PRACTICE, 3));
            assertSame(failure, exception.getCause());
        }
    }

    @Test void rejectsUnsupportedCountsBeforeSendingARequest() {
        HuggingFaceQuestionGateway gateway = new HuggingFaceQuestionGateway(uri -> {
            fail("Invalid requests must not reach the service");
            return "";
        });
        for (int count : new int[]{0, 4}) {
            assertThrows(IllegalArgumentException.class, () -> gateway.getQuestions(
                    QuestionTopic.ECOLOGY, QuestionPurpose.EXAM, count));
        }
    }

    @Test void preservesInterruptStatus() {
        try {
            HuggingFaceQuestionGateway gateway = new HuggingFaceQuestionGateway(uri -> {
                throw new InterruptedException("cancelled");
            });
            assertThrows(QuestionLoadException.class, () -> gateway.getQuestions(
                    QuestionTopic.ECOLOGY, QuestionPurpose.PRACTICE, 3));
            assertTrue(Thread.currentThread().isInterrupted());
        }
        finally { Thread.interrupted(); }
    }

    @Test void rejectsMissingMalformedTruncatedAndInvalidAnswers() {
        String valid = fixture(MmluSelection.forCourse(QuestionTopic.ECOLOGY, QuestionPurpose.PRACTICE));
        for (String body : List.of("not json", "{}", "{\"partial\":false,\"rows\":[]}",
                valid.replace("false", "true"), valid.replace("\"answer\":2", "\"answer\":4"),
                valid.replace("\"answer\":2", "\"answer\":1.5"),
                valid.replace("\"answer\":2", "\"answer\":\"2\""),
                valid.replace("\"truncated_cells\":[]", "\"truncated_cells\":[\"question\"]"),
                valid.replace("high_school_biology", "unknown"))) {
            HuggingFaceQuestionGateway gateway = new HuggingFaceQuestionGateway(uri -> body);
            assertThrows(QuestionLoadException.class, () -> gateway.getQuestions(
                    QuestionTopic.ECOLOGY, QuestionPurpose.PRACTICE, 3));
        }
    }
}
