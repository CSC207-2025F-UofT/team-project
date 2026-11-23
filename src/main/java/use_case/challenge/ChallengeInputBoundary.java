package use_case.challenge;

import use_case.question.QuestionLoadException;

public interface ChallengeInputBoundary {
    void execute(ChallengeInputData input) throws QuestionLoadException, ChallengeStateException;
}
