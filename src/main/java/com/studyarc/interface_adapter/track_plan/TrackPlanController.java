package com.studyarc.interface_adapter.track_plan;

import com.studyarc.use_case.track_plan.TrackPlanInputBoundary;

public class TrackPlanController {
    final TrackPlanInputBoundary trackplaninteractor;

    public TrackPlanController(TrackPlanInputBoundary trackplaninteractor){
        this.trackplaninteractor = trackplaninteractor;
    }

    //Maybe will add parameter of inputdata to this execute method
    public void execute(){
//        this.trackplaninteractor.execute();
    }

}
