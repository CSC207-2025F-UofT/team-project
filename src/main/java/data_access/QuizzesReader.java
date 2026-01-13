package data_access;

import entity.AnswerOption;
import entity.Question;
import entity.Quiz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizzesReader {
    public void loadQuizzes(InMemoryQuizDataAccessObject repo) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            getClass().getResourceAsStream("/quizzes.txt")
                    )
            );

            if (reader == null) {
                System.err.println("Could not find quizzes.txt in resources.");
                return;
            }

            String line;
            Integer quizId = null;
            Integer questionId = null;
            String questionText = null;
            List<AnswerOption> options = new ArrayList<>(4);
            Integer correctOptionId = null;

            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(";");
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                // question line
                if (parts.length == 3) {

                    // save previous question before starting next one
                    if (quizId != null && questionId != null && options.size() == 4 && correctOptionId != null) {
                        Question q = new Question(questionId, questionText, options, correctOptionId);
                        Quiz quiz = new Quiz(quizId, q);
                        repo.put(quiz);
                    }

                    quizId = Integer.parseInt(parts[0]);
                    questionId = Integer.parseInt(parts[1]);
                    questionText = parts[2];
                    options = new ArrayList<>(4);
                    correctOptionId = null;
                }

                // answer line
                else if (parts.length == 4) {
                    String label = parts[1];
                    String text = parts[2];
                    boolean isCorrect = Boolean.parseBoolean(parts[3]);

                    int optionId = convertOptionLabelToId(label);

                    AnswerOption option = new AnswerOption(optionId, text);
                    options.add(option);

                    if (isCorrect) {
                        correctOptionId = optionId;
                    }
                }
            }

            // save final question after finishing quizzes file
            if (quizId != null && questionId != null && options.size() == 4 && correctOptionId != null) {
                Question q = new Question(questionId, questionText, options, correctOptionId);
                Quiz quiz = new Quiz(quizId, q);
                repo.put(quiz);
            }

            reader.close();

        } catch (Exception e) {
            System.err.println("Error loading quizzes: " + e.getMessage());
        }
    }

    private int convertOptionLabelToId(String label) {
        label = label.toUpperCase();
        switch (label) {
            case "A": return 1;
            case "B": return 2;
            case "C": return 3;
            case "D": return 4;
            default: return 1; // should not happen if file is correct
        }
    }
}
