package interface_adapter.check_compatibility;

import java.util.ArrayList;
import java.util.List;

import use_case.check_compatibility.CheckCompatibilityOutputBoundary;
import use_case.check_compatibility.CheckCompatibilityOutputData;
import use_case.check_compatibility.RuleResult;

/**
 * Turns rule results into short text lines for the compatibility panel.
 */
public class CheckCompatibilityPresenter implements CheckCompatibilityOutputBoundary {
    private final CompatibilityViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the model the compatibility panel listens to
     */
    public CheckCompatibilityPresenter(CompatibilityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentCompatibility(CheckCompatibilityOutputData outputData) {
        final List<String> lines = new ArrayList<>();
        final List<Boolean> conflicts = new ArrayList<>();
        int conflictCount = 0;
        for (RuleResult result : outputData.getResults()) {
            final boolean conflict = result.getStatus() == RuleResult.Status.CONFLICT;
            lines.add(label(result.getStatus()) + ": " + result.getTrait() + " - " + result.getExplanation());
            conflicts.add(conflict);
            if (conflict) {
                conflictCount++;
            }
        }
        final CompatibilityState state = new CompatibilityState();
        state.setLines(lines, conflicts);
        state.setHeadline(headline(outputData.getPetName(), conflictCount));
        publish(state);
    }

    @Override
    public void presentNoPreferences() {
        final CompatibilityState state = new CompatibilityState();
        state.setNeedsPreferences(true);
        state.setMessage("Log in and fill in My profile to see how well this cat fits your home.");
        publish(state);
    }

    @Override
    public void presentNotFound(String message) {
        final CompatibilityState state = new CompatibilityState();
        state.setMessage(message);
        publish(state);
    }

    private void publish(CompatibilityState state) {
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    private String headline(String petName, int conflictCount) {
        if (conflictCount == 0) {
            return "No conflicts: " + petName + " fits your home.";
        }
        final String noun = conflictCount == 1 ? "conflict" : "conflicts";
        return conflictCount + " " + noun + " to think about for " + petName + ".";
    }

    private String label(RuleResult.Status status) {
        final String label;
        switch (status) {
            case MATCH:
                label = "Match";
                break;
            case CONFLICT:
                label = "Conflict";
                break;
            default:
                label = "Not relevant";
                break;
        }
        return label;
    }
}
