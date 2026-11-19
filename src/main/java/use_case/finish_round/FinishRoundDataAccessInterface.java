package use_case.finish_round;

import entity.Player;
import use_case.shared.DataAccessException;

/** Player storage, as seen by Finish Round. */
public interface FinishRoundDataAccessInterface {
    void savePlayer(Player player) throws DataAccessException;
}
