package adapter;

import entity.Monster;
import entity.PlayerCharacter;
import usecase.combat.CombatInputBoundary;
import usecase.combat.CombatInputData;

public class CombatController {
    private final CombatInputBoundary interactor;

    public CombatController(CombatInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void startCombat(PlayerCharacter character, Monster monster) {
        CombatInputData inputData = new CombatInputData(character, monster);

        interactor.startCombat(inputData);
    }

    public void attack(PlayerCharacter character, Monster monster) {
        CombatInputData inputData = new CombatInputData(character, monster);

        interactor.attack(inputData);
    }
}
