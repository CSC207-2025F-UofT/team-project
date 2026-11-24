package usecase.combat;

public class CombatInputData {
    private final String characterID;
    private final String monsterID;

    public CombatInputData(String characterID, String monsterID) {
        this.characterID = characterID;
        this.monsterID = monsterID;
    }

    public String getCharacterID() {
        return characterID;
    }

    public String getMonsterID() {
        return monsterID;
    }
}
