package use_case.shared;

import entity.Round;

/** The round being played right now. Used by Start Round, Submit Guess and Finish Round. */
public interface CurrentRoundDataAccessInterface {
    /** @return the current round, or null if no round has started */
    Round getCurrentRound();

    void setCurrentRound(Round round);
}
