package org.accessgraph.interface_adapter.plan_route;

import org.accessgraph.use_case.plan_route.PlanRouteOutputBoundary;
import org.accessgraph.use_case.plan_route.PlanRouteOutputData;

import java.util.ArrayList;
import java.util.List;

/** Formats the use case result into text for the view model. */
public final class PlanRoutePresenter implements PlanRouteOutputBoundary {
    private final PlanRouteViewModel viewModel;

    public PlanRoutePresenter(PlanRouteViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(PlanRouteOutputData data) {
        String summary = "About " + data.getTotalMinutes() + " min · " + data.getTransfers()
                + (data.getTransfers() == 1 ? " transfer" : " transfers")
                + (data.isStepFreeProfile() ? " · step-free profile" : "");
        viewModel.setState(new PlanRouteState(summary, steps(data), ""));
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setState(new PlanRouteState("", List.of(), errorMessage));
        viewModel.firePropertyChanged();
    }

    private List<String> steps(PlanRouteOutputData data) {
        List<String> names = data.getStationNames();
        List<String> lines = data.getLegLines();
        List<String> steps = new ArrayList<>();
        steps.add("Board " + lines.get(0) + " at " + names.get(0));
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).equals(lines.get(i - 1))) {
                steps.add("Change to " + lines.get(i) + " at " + names.get(i));
            }
        }
        steps.add("Exit at " + names.get(names.size() - 1) + " (" + (names.size() - 1) + " stops)");
        steps.add("");
        steps.add("Stations: " + String.join(" → ", names));
        if (data.isStepFreeProfile()) {
            steps.add("");
            steps.add("You board, change lines and exit only at stations the TTC lists as accessible.");
            steps.add("Check TTC elevator alerts before travelling.");
        }
        return steps;
    }
}
