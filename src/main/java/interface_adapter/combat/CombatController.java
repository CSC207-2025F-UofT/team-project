package interface_adapter.combat;

import entity.characters.Monster;
import entity.characters.PlayerCharacter;
import use_case.combat.CombatInputBoundary;
import use_case.combat.CombatInputData;

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
