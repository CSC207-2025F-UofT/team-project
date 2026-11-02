package states;

import game.gameModel.Entity;

public class WalkingState extends ActionState{

    public WalkingState(Entity entity) {
        super("walking", entity);
    }
}
