package entity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A quiz: an ordered list of questions, plus the rule for scoring a set of answers.
 */
public class Quiz {
    private final String title;
    private final List<Question> questions;

    public Quiz(String title, List<Question> questions) {
        this.title = title;
        this.questions = List.copyOf(questions);
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    /** Used for the progress bar. */
    public int getQuestionCount() {
        return questions.size();
    }

    /**
     * Adds up each library's points from the answers of the entire quiz.
     *
     * @param chosenIndices the index of the question's chosen answers, in order
     * @throws IllegalArgumentException if not every question was answered
     */
    public Map<Library, Integer> score(List<Integer> chosenIndices) {
        if (chosenIndices.size() != questions.size()) {
            throw new IllegalArgumentException("Every question must be answered.");
        }
        Map<Library, Integer> scores = new EnumMap<>(Library.class);
        for (Library library : Library.values()) {
            scores.put(library, 0);
        }
        for (int i = 0; i < questions.size(); i++) {
            AnswerOption chosen = questions.get(i).getOption(chosenIndices.get(i));
            for (Library library : Library.values()) {
                scores.put(library, scores.get(library) + chosen.getWeight(library));
            }
        }
        return scores;
    }

    /** Returns the library with the highest score (ties broken at random). */
    public static Library bestLibrary(Map<Library, Integer> scores) {
        int max = Integer.MIN_VALUE; // -∞ 

        // Identify max
        for (Library library : Library.values()) {
            max = Math.max(max, scores.getOrDefault(library, 0));
        }

        // Collect maxes
        List<Library> best = new ArrayList<>();
        for (Library library : Library.values()) {
            if (scores.getOrDefault(library, 0) == max) {
                best.add(library);
            }
        }

        // Pick one at random
        int k = ThreadLocalRandom.current().nextInt(best.size()); // 0 to best.size() - 1
        return best.get(k);
    }
}
