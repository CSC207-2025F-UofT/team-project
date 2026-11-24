package use_case.start_round;

import entity.AnswerCity;
import entity.Round;

public interface StartRoundDataAccessInterface {
    AnswerCity getRandomAnswerCity();
    void saveRound(Round round);
}
