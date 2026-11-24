package adapter;

import usecase.combat.CombatOutputBoundary;
import usecase.combat.CombatOutputData;

public class CombatPresenter implements CombatOutputBoundary {
    private final CombatViewModel viewModel;

    public CombatPresenter(CombatViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CombatOutputData outputData) {

    }
}
