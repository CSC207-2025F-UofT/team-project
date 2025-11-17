package com.studyarc.use_case.track_plan;

import com.studyarc.entity.StudyPlan;

import java.util.ArrayList;

public class TrackPlanDataAccessTool implements TrackPlanDataAccessinterface{

//the actual tool that use to get plans from the database.

    @Override
    public ArrayList<StudyPlan> getPlans(String username) {
        ArrayList<StudyPlan> result = new ArrayList<>();
        return result;
    }

    private void getPlansHelper(String username){

    }
}
