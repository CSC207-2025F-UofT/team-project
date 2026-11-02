package states;

import game.gameModel.Entity;

public class RestState extends ActionState{

    public RestState(Entity entity) {
        super("rest", entity);
    }
}
