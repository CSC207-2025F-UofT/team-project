package interface_adapter;

import java.util.Objects;

import use_case.DropObjectOutputBoundary;
import use_case.DropObjectOutputData;

public final class DropObjectPresenter implements DropObjectOutputBoundary {

    private final DropObjectViewModel viewModel;

    public DropObjectPresenter(DropObjectViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public void presentDropResult(DropObjectOutputData outputData) {
        Objects.requireNonNull(outputData, "outputData");
        viewModel.setLastDropSuccessful(outputData.wasDropped());
    }
}
