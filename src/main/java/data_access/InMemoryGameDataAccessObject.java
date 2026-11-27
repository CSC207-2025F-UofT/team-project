package data_access;

import entity.Game;
import use_case.playcard.PlayCardDataAccessInterface;

public class InMemoryGameDataAccessObject
        implements PlayCardDataAccessInterface {

    private Game currentGame;

    @Override
    public Game getCurrentGame() {
        return currentGame;
    }

    @Override
    public void saveGame(Game game) {
        this.currentGame = game;
    }
}