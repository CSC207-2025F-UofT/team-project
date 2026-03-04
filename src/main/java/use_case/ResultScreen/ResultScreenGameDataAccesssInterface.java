package use_case.ResultScreen;

import entity.AdventureGame;

public interface ResultScreenGameDataAccesssInterface {

    AdventureGame get();
    void clearGameData();
}
