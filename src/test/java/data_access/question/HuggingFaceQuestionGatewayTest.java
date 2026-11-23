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
                int count = purpose == QuestionPurpose.EXAM ? 5 : 3;
                List<Question> questions = gateway.getQuestions(topic, purpose, count);
                assertEquals(count, questions.size());
                for (Question question : questions) {
                    assertEquals(2, question.getCorrectAnswer());
                    assertTrue(question.getExplanation().isEmpty());
                    assertTrue(practice.add(question.getId()), "Practice and exam must not overlap");
                }
                assertEquals(1, gateway.getQuestions(topic, purpose, 1).size());
                List<Question> three = gateway.getQuestions(topic, purpose, 3);
                for (int index = 0; index < 3; index++) {
                    assertEquals(questions.get(index).getId(), three.get(index).getId());
                }
                if (purpose == QuestionPurpose.EXAM) {
                    assertEquals(4, gateway.getQuestions(topic, purpose, 4).size());
                }
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
        for (QuestionPurpose purpose : QuestionPurpose.values()) {
            int tooMany = purpose == QuestionPurpose.EXAM ? 6 : 4;
            for (int count : new int[]{0, tooMany}) {
                assertThrows(IllegalArgumentException.class, () -> gateway.getQuestions(
                        QuestionTopic.ECOLOGY, purpose, count));
            }
        }
    }

    @Test void missingFifthExamQuestionFailsInsteadOfReturningAShorterSession() throws Exception {
        MmluSelection selection = MmluSelection.forCourse(QuestionTopic.CELL_BIOLOGY, QuestionPurpose.EXAM);
        String response = fixture(selection).replace("\"row_idx\":" + selection.rows.get(4),
                "\"row_idx\":999");
        HuggingFaceQuestionGateway gateway = new HuggingFaceQuestionGateway(uri -> response);
        assertEquals(3, gateway.getQuestions(QuestionTopic.CELL_BIOLOGY, QuestionPurpose.EXAM, 3).size());
        assertThrows(QuestionLoadException.class, () -> gateway.getQuestions(
                QuestionTopic.CELL_BIOLOGY, QuestionPurpose.EXAM, 5));
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
