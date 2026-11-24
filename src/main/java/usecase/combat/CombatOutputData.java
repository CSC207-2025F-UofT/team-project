package usecase.combat;

public class CombatOutputData {
    private final String characterName;
    private final String monsterName;
    private final int characterHealth;
    private final int monsterHealth;
    private final String combatMessage;
    private final boolean combatFinished;
    private final boolean characterWon;

    public CombatOutputData(String characterName,
                            String monsterName,
                            int characterHealth,
                            int monsterHealth,
                            String combatMessage,
                            boolean combatFinished,
                            boolean characterWon) {

        this.characterName = characterName;
        this.monsterName = monsterName;
        this.characterHealth = characterHealth;
        this.monsterHealth = monsterHealth;
        this.combatMessage = combatMessage;
        this.combatFinished = combatFinished;
        this.characterWon = characterWon;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public int getCharacterHealth() {
        return characterHealth;
    }

    public int getMonsterHealth() {
        return monsterHealth;
    }

    public String getCombatMessage() {
        return combatMessage;
    }

    public boolean isCombatFinished() {
        return combatFinished;
    }

    public boolean isCharacterWon() {
        return characterWon;
    }
}
