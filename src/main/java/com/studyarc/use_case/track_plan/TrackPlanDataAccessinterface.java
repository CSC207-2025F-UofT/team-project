package com.studyarc.use_case.track_plan;

import com.studyarc.entity.StudyPlan;

import java.util.ArrayList;

public interface TrackPlanDataAccessinterface {
    //after the entity “user” is impelemented， may add some parameter to getPlans call.

    ArrayList<StudyPlan> getPlans(String username);
}
