package use_case.track_plan;
//
//use case Interactor for tracking a plan
//1. access all plans from the user in database
//2. display all the plan in w.e the view is called

import use_case.TempUser;

public class TrackPlanInteractor implements TrackPlanInputBoundary {
    final TrackPlanOutputBoundary presenter;
    final TrackPlanDataAccessinterface getPlanTool;

    public TrackPlanInteractor(TrackPlanOutputBoundary presenter, TrackPlanDataAccessinterface getPlanTool) {
        this.presenter = presenter;
        this.getPlanTool = getPlanTool;
    }

    @Override
    public void execute(TrackPlanInputData inputData) {

        System.out.println("interactor executes");
    }
}
