package interface_adapter.drop_object;

import java.util.Objects;

import use_case.drop_object.DropObjectOutputBoundary;
import use_case.drop_object.DropObjectOutputData;

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
