package com.studyarc.data_access;

import com.studyarc.entity.Milestone;
import com.studyarc.entity.StudyPlan;
import com.studyarc.entity.Task;
import com.studyarc.entity.User;
import com.studyarc.use_case.job_postings.JobPostingsDataAccessInterface;
import com.studyarc.use_case.login.LoginDataAccessInterface;
import com.studyarc.use_case.milestone_tasks.MilestoneTasksDataAccessInterface;
import com.studyarc.use_case.reflection_log.ReflectionLogDataAccessInterface;
import com.studyarc.use_case.track_plan.TempUser;
import com.studyarc.use_case.track_plan.TrackPlanDataAccessinterface;

import java.util.ArrayList;
import java.util.Map;

public class DatabaseAccess implements JobPostingsDataAccessInterface, LoginDataAccessInterface, MilestoneTasksDataAccessInterface, ReflectionLogDataAccessInterface, TrackPlanDataAccessinterface {

    @Override
    public ArrayList<String> getFocuses() {
        return null;
    }

    @Override
    public ArrayList<Task> getTasksForMilestone(User user, StudyPlan plan, Milestone milestone) {
        return null;
    }

    @Override
    public ArrayList<Milestone> getMilestones(User user, StudyPlan plan) {
        return null;
    }

    @Override
    public void savePlan(User user, StudyPlan plan, Map<Milestone, ArrayList<Task>> milestonesToTasks) {

    }

    @Override
    public void getPlans(TempUser user) {

    }

    @Override
    public boolean registerUser(User u) {
        return false;
    }

    @Override
    public User getUser(String username) {
        return null;
    }
}
