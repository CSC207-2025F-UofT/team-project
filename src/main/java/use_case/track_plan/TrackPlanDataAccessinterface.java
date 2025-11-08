package use_case.track_plan;

import use_case.TempUser;

public interface TrackPlanDataAccessinterface {
    //after the entity “user” is impelemented， may add some parameter to getPlans call.

    void getPlans(TempUser user);
}
