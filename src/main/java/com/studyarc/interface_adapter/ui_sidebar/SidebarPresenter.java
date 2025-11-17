package com.studyarc.interface_adapter.ui_sidebar;

import com.studyarc.interface_adapter.ViewManagerModel;
import com.studyarc.interface_adapter.job_postings.JobPostingsViewModel;
import com.studyarc.interface_adapter.milestone_tasks.MilestoneTasksViewModel;
import com.studyarc.use_case.ui_sidebar.SidebarOutputBoundary;

public class SidebarPresenter implements SidebarOutputBoundary {
    private final SidebarViewModel sidebarViewModel;
    private final JobPostingsViewModel jobPostingsViewModel;
    private final MilestoneTasksViewModel milestoneTasksViewModel;
    private final ViewManagerModel viewManagerModel;

    public SidebarPresenter(ViewManagerModel viewManagerModel, SidebarViewModel sidebarViewModel,
                            JobPostingsViewModel jobPostingsViewModel, MilestoneTasksViewModel milestoneTasksViewModel) {
        this.sidebarViewModel = sidebarViewModel;
        this.jobPostingsViewModel = jobPostingsViewModel;
        this.viewManagerModel = viewManagerModel;
        this.milestoneTasksViewModel = milestoneTasksViewModel;
    }

    @Override
    public void switchToJobBoard() {
        viewManagerModel.setState(jobPostingsViewModel.getViewName());
        viewManagerModel.firePropertyChange();
        System.out.println("viewManagerModel.firePropertyChange for job board: " + jobPostingsViewModel.getViewName());
    }

    @Override
    public void switchToMilestone() {
        viewManagerModel.setState(milestoneTasksViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
