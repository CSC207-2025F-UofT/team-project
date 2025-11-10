package com.studyarc.use_case.milestone_tasks;

import com.studyarc.entity.Milestone;
import com.studyarc.entity.Task;

import java.util.ArrayList;

public class MilestoneTasksInputData {
    private Milestone milestone;
    private ArrayList<Task> tasks;

    public MilestoneTasksInputData(Milestone milestone, ArrayList<Task> tasks) {
        this.milestone = milestone;
        this.tasks = tasks;
    }

    Milestone getMilestone() { return milestone; }

    ArrayList<Task> getTasks() { return tasks; }
}
