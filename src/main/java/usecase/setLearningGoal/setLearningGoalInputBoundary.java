package usecase.setLearningGoal;

import usecase.deck.create_deck.CreateDeckInputData;

/**
 * Input Boundary for actions which are related to setting a learning goal.
 */
public interface setLearningGoalInputBoundary {
    void setLearningGoal(setLearningGoalInputData inputData);
}
