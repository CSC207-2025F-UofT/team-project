package com.studyarc.use_case.track_plan;

public class TrackPlanPresenter implements TrackPlanOutputBoundary{
    @Override
    public void prepareShowPlans(TrackPlanOutputData outputData) {
        // logic of showing all the plans.
    }

    @Override
    public void parepareShowRedirect(TrackPlanOutputData outputData) {
        //logic of show a redirect link to plan creation
    }
}
