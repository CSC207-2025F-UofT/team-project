package com.studyarc.use_case.track_plan;

import com.studyarc.entity.StudyPlan;

import java.util.ArrayList;
import java.util.List;

public class TrackPlanOutputData {
    private String username;
    private ArrayList<StudyPlan> listofplan;

    public TrackPlanOutputData(String username, ArrayList<StudyPlan> plans) {
        this.username = username;
        this.listofplan = plans;
    }
    //this class may contain all the plans we get from the database and pack it to the presenter.
}
