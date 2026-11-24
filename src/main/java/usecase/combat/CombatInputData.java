package usecase.combat;

import entity.Monster;
import entity.PlayerCharacter;

public class CombatInputData {
    private final PlayerCharacter character;
    private final Monster monster;

    public CombatInputData(PlayerCharacter character, Monster monster) {
        this.character = character;
        this.monster = monster;
    }

    public PlayerCharacter getCharacter() {
        return character;
    }

    public Monster getMonster() {
        return monster;
    }
}
