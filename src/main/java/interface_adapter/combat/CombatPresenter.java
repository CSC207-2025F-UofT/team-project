package interface_adapter.combat;

import use_case.combat.CombatOutputBoundary;
import use_case.combat.CombatOutputData;

public class CombatPresenter implements CombatOutputBoundary {
    private final CombatViewModel viewModel;

    public CombatPresenter(CombatViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CombatOutputData outputData) {

    }
}
