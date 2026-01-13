package use_case.move;

import entity.Monster;
import entity.AdventureGame;
import entity.Direction;
import entity.Location;

public class MoveInteractor implements MoveInputBoundary {

    private final MoveGameDataAccessInterface moveGameDataAccess;

    private final MoveOutputBoundary movePresenter;

    public MoveInteractor(MoveGameDataAccessInterface moveGameDataAccess, MoveOutputBoundary movePresenter) {
        this.moveGameDataAccess = moveGameDataAccess;
        this.movePresenter = movePresenter;
    }

    @Override
    public void execute(MoveInputData moveInputData) {
        Direction direction = moveInputData.getDirection();
        AdventureGame game = moveGameDataAccess.getGame();
        boolean success = game.move(direction);
        if (success) {
            this.moveGameDataAccess.saveGame(game);
        }

        boolean canMoveLeft = game.canMove(Direction.LEFT);
        boolean canMoveRight = game.canMove(Direction.RIGHT);

        Location currentLocation = game.getGameMap().getCurrentLocation();
        String locationName = currentLocation.getName();
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();

        int currentIndex = game.getGameMap().getCurrentLocationIndex();
        int mapSize = game.getGameMap().getMapSize();

        Monster monster = currentLocation.getMonster();
        // Item item = currentLocation.getItem();
        MoveOutputData outputData = new MoveOutputData(
                locationName,
                latitude,
                longitude,
                currentIndex,
                mapSize,
                canMoveLeft,
                canMoveRight,
                monster
                // , ITEM
        );

        this.movePresenter.present(outputData);
    }

    @Override
    public void switchToBattleView(Monster monster) {
        movePresenter.switchToBattleView(moveGameDataAccess.getGame().getUser(), monster);
    }
}
