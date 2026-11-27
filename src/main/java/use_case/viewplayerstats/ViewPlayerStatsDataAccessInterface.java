package use_case.viewplayerstats;

import entity.playerData;
public interface ViewPlayerStatsDataAccessInterface {
    playerData getPlayerData(String playerId);

}
