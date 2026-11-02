package states;

import game.gameModel.Entity;

public abstract class ActionState extends EntityState{

    public ActionState(String name, Entity entity) {
        super(name, entity);
    }
}
