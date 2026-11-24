package usecase.combat;

public class CombatInteractor implements CombatInputBoundary {
    private final CombatDataAccessInterface dataAccess;
    private final CombatOutputBoundary presenter;

    public CombatInteractor(
            CombatDataAccessInterface dataAccess,
            CombatOutputBoundary presenter) {

        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void startCombat(CombatInputData inputData) {
        String characterID = inputData.getCharacterID();
        String monsterID = inputData.getMonsterID();

        //CombatEncounter combat = new CombatEncounter();
    }

    @Override
    public void attack(CombatInputData inputData) {

    }
}
