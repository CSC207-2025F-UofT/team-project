package usecase.combat;

public interface CombatInputBoundary {

    void startCombat(CombatInputData inputData);

    void attack(CombatInputData inputData);
}
