package states;

import game.gameModel.Entity;

public class RunningState extends ActionState{

    public RunningState(Entity entity) {
        super("running", entity);
    }
}
