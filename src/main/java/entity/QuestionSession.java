package entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/** In-progress answers only; the owning use case decides when to commit a result. */
public final class QuestionSession {
    private final List<Question> questions;
    private final int[] answers;
    private boolean hintUsed;

    public QuestionSession(List<Question> questions) {
        this.questions = List.copyOf(questions);
        if (questions.isEmpty() || new HashSet<>(questions.stream()
                .map(Question::getId).collect(java.util.stream.Collectors.toList())).size()
                != questions.size()) {
            throw new IllegalArgumentException("A session needs distinct questions");
        }
        answers = new int[questions.size()];
        Arrays.fill(answers, -1);
    }

    public void answer(int questionIndex, int optionIndex) {
        Question question = questions.get(questionIndex);
        if (optionIndex < 0 || optionIndex >= question.getOptions().size()) {
            throw new IllegalArgumentException("Select a valid answer");
        }
        answers[questionIndex] = optionIndex;
    }

    public int useHint(int questionIndex) {
        if (hintUsed) {
            throw new IllegalStateException("The session hint has already been used");
        }
        int eliminated = questions.get(questionIndex).getEliminatedOption();
        hintUsed = true;
        return eliminated;
    }

    public List<Question> getQuestions() { return questions; }
    public int getAnswer(int questionIndex) { return answers[questionIndex]; }
    public boolean isHintUsed() { return hintUsed; }
    public boolean isComplete() { return Arrays.stream(answers).allMatch(answer -> answer >= 0); }

    public int getCorrectCount() {
        int correct = 0;
        for (int index = 0; index < answers.length; index++) {
            if (questions.get(index).isCorrect(answers[index])) {
                correct++;
            }
        }
        return correct;
    }
}
