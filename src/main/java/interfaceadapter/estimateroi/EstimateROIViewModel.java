package interfaceadapter.estimateroi;

import interfaceadapter.ViewModel;

/** Holds the Estimate ROI state for the view. */
public class EstimateROIViewModel extends ViewModel<EstimateROIState> {
    public static final String STATE_PROPERTY = "estimate";

    public EstimateROIViewModel() {
        super("estimate roi");
        setState(new EstimateROIState(null, null));
    }
}