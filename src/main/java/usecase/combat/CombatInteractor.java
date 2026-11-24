package usecase.combat;

import entity.CombatEncounter;
import entity.DiceRoller;
import entity.Monster;
import entity.PlayerCharacter;

public class CombatInteractor implements CombatInputBoundary {
    private final CombatDataAccessInterface dataAccess;
    private final CombatOutputBoundary presenter;
    private final DiceRoller diceRoller;

    public CombatInteractor(
            CombatDataAccessInterface dataAccess,
            CombatOutputBoundary presenter,
            DiceRoller diceRoller) {

        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.diceRoller = diceRoller;
    }

    @Override
    public void startCombat(CombatInputData inputData) {
        PlayerCharacter character = inputData.getCharacter();
        Monster monster = inputData.getMonster();

        if (!character.isAlive() || !monster.isAlive()) {
            //TODO display error
        }

        CombatEncounter combat = new CombatEncounter(character, monster);

        CombatOutputData outputData = new CombatOutputData(character.getName(), monster.getName(),
                                                            character.getHealth(), monster.getCurrentHealth(),
                                                            "Started combat with " + monster.getName(),
                                                            false, false);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void attack(CombatInputData inputData) {

    }
}
