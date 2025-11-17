package com.studyarc.interface_adapter.track_plan;

import com.studyarc.use_case.track_plan.TrackPlanOutputBoundary;
import com.studyarc.use_case.track_plan.TrackPlanOutputData;

public class TrackPlanPresenter implements TrackPlanOutputBoundary {

    public  TrackPlanPresenter(){

    }

    @Override
    public void prepareShowPlans(TrackPlanOutputData outputData) {
        // logic of showing all the plans.
    }

    @Override
    public void parepareShowRedirect(TrackPlanOutputData outputData) {
        //logic of show a redirect link to plan creation
    }
}
