package use_case.combat;

public interface CombatInputBoundary {

    void startCombat(CombatInputData inputData);

    void attack(CombatInputData inputData);
}
