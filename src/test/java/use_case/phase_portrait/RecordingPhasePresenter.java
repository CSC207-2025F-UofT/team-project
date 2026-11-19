package use_case.phase_portrait;

import java.util.ArrayList;
import java.util.List;

// keeps what the use cases said, for tests
public class RecordingPhasePresenter implements PhasePortraitOutputBoundary {
    public final List<PhasePortraitOutputData> views = new ArrayList<>();
    public final List<Boolean> fresh = new ArrayList<>();
    public final List<PhaseTraceOutputData> traces = new ArrayList<>();
    public String error;

    @Override
    public void preparePortraitView(PhasePortraitOutputData data, boolean isFresh) {
        views.add(data);
        fresh.add(isFresh);
    }

    @Override
    public void prepareTraceView(PhaseTraceOutputData data) {
        traces.add(data);
    }

    @Override
    public void prepareFailView(String message) {
        error = message;
    }

    public PhasePortraitOutputData last() {
        return views.get(views.size() - 1);
    }
}
