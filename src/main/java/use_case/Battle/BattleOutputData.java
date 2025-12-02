package use_case.Battle;

import entity.Monster;
import entity.User;

public class BattleOutputData {
    private final User user;
    private final Monster monster;

    public BattleOutputData(User user, Monster monster) {
        this.user = user;
        this.monster = monster;
        boolean battleEnded = !user.isAlive() || !monster.isAlive();
    }

    public User getUser() {
        return user;
    }

    public Monster getMonster() {
        return monster;
    }

    public double getUserHP() {
        return user.getHP();
    }

    public double getMonsterHP() {
        return monster.getHP();
    }
}
