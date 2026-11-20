package use_case.start_round;

import entity.AnswerCity;

/** City dataset, as seen by Start Round. */
public interface StartRoundDataAccessInterface {
    /** @return a random city (with landmark) to use as a round's target */
    AnswerCity getRandomAnswerCity();
}
