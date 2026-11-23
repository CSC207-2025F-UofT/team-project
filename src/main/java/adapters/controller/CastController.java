package adapters.controller;

import yuhangusecase.castspell.CastInputBoundary;
import yuhangusecase.castspell.CastInputData;

public class CastController {
    private final CastInputBoundary castInteractor;

    public CastController(CastInputBoundary castInteractor) {
        this.castInteractor = castInteractor;
    }

    public void execute(String playerName, int x, int y,
                        int spellIndex, String direction) {
        CastInputData inputData = new CastInputData(
                playerName,
                x,
                y,
                spellIndex,
                direction
        );

        castInteractor.execute(inputData);
    }
}
