package entity;

public class CombatEncounter {
    private PlayerCharacter character;
    private Monster monster;
    private boolean isDone;

    public CombatEncounter(PlayerCharacter character, Monster monster) {
        this.character = character;
        this.monster = monster;
        this.isDone = false;
    }

    public PlayerCharacter getCharacter() {
        return character;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean isDone() {
        return isDone;
    }

    public void checkStatus() {
        if (!character.isAlive() || !monster.isAlive()) {
            isDone = true;
        }
    }
}
