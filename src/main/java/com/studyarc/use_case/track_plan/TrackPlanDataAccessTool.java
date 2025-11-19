package com.studyarc.use_case.track_plan;

import com.studyarc.entity.Milestone;
import com.studyarc.entity.StudyPlan;
import com.studyarc.entity.Task;

import java.util.ArrayList;
import java.util.Date;

public class TrackPlanDataAccessTool implements TrackPlanDataAccessinterface{

//the actual tool that use to get plans from the database.

    @Override
    public ArrayList<StudyPlan> getPlans(String username) {

        ArrayList<StudyPlan> result = new ArrayList<>();
        return result;
    }

    private void getPlansHelper(String username){

    }
    @Override
    public ArrayList<StudyPlan> generateTestPlans() {
        ArrayList<StudyPlan> plans = new ArrayList<>();

        // Plan 1
        StudyPlan plan1 = new StudyPlan("Plan 1", new ArrayList<>());

        Milestone p1m1 = new Milestone("Milestone 1");
        p1m1.getTasks().add(new Task("Do step1", new Date()));
        p1m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p1m2 = new Milestone("Milesone 2");
        p1m2.getTasks().add(new Task("Do step1", new Date()));
        p1m2.getTasks().add(new Task("Do step2", new Date()));

        plan1.getMilestones().add(p1m1);
        plan1.getMilestones().add(p1m2);

        // Plan 2
        StudyPlan plan2 = new StudyPlan("Plan 2", new ArrayList<>());

        Milestone p2m1 = new Milestone("Milestone 1");
        p2m1.getTasks().add(new Task("Do step1", new Date()));
        p2m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p2m2 = new Milestone("Milestone 2");
        p2m2.getTasks().add(new Task("Do step1", new Date()));
        p2m2.getTasks().add(new Task("Do step2", new Date()));

        plan2.getMilestones().add(p2m1);
        plan2.getMilestones().add(p2m2);

        // Plan 3
        StudyPlan plan3 = new StudyPlan("Plan 3", new ArrayList<>());

        Milestone p3m1 = new Milestone("Milestone 1");
        p3m1.getTasks().add(new Task("Do step1", new Date()));
        p3m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p3m2 = new Milestone("Milestone 2");
        p3m2.getTasks().add(new Task("Do step1", new Date()));
        p3m2.getTasks().add(new Task("Do step2", new Date()));

        plan3.getMilestones().add(p3m1);
        plan3.getMilestones().add(p3m2);

        // plan 4
        StudyPlan plan4 = new StudyPlan("Plan 4", new ArrayList<>());

        Milestone p4m1 = new Milestone("Milestone 1");
        p3m1.getTasks().add(new Task("Do step1", new Date()));
        p3m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p4m2 = new Milestone("Milestone 2");
        p3m2.getTasks().add(new Task("Do step1", new Date()));
        p3m2.getTasks().add(new Task("Do step2", new Date()));

        plan4.getMilestones().add(p4m1);
        plan4.getMilestones().add(p4m2);
        // Plan 5
        StudyPlan plan5 = new StudyPlan("Plan 5", new ArrayList<>());

        Milestone p5m1 = new Milestone("Milestone 1");
        p5m1.getTasks().add(new Task("Do step1", new Date()));
        p5m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p5m2 = new Milestone("Milestone 2");
        p5m2.getTasks().add(new Task("Do step1", new Date()));
        p5m2.getTasks().add(new Task("Do step2", new Date()));

        plan5.getMilestones().add(p5m1);
        plan5.getMilestones().add(p5m2);

        //plan 6
        StudyPlan plan6 = new StudyPlan("Plan 6", new ArrayList<>());

        Milestone p6m1 = new Milestone("Milestone 1");
        p6m1.getTasks().add(new Task("Do step1", new Date()));
        p6m1.getTasks().add(new Task("Do step2", new Date()));

        Milestone p6m2 = new Milestone("Milestone 2");
        p6m2.getTasks().add(new Task("Do step1", new Date()));
        p6m2.getTasks().add(new Task("Do step2", new Date()));

        plan6.getMilestones().add(p6m1);
        plan6.getMilestones().add(p6m2);

        //add all plans to plans
        plans.add(plan1);
        plans.add(plan2);
        plans.add(plan3);
        plans.add(plan4);
        plans.add(plan5);
        plans.add(plan6);

        return plans;

    }
}
