package com.studyarc.interface_adapter.job_postings;

import com.studyarc.use_case.job_postings.JobPostingsOutputBoundary;
import com.studyarc.use_case.job_postings.JobPostingsOutputData;
import com.studyarc.view.JobPostingsView;

import java.util.ArrayList;

/**
 * The Presenter for the Job Postings Use Case.
 */
public class JobPostingsPresenter implements JobPostingsOutputBoundary {
    private final JobPostingsView jobPostingsView;

    public JobPostingsPresenter(JobPostingsView jobPostingsView) {
        this.jobPostingsView = jobPostingsView;
    }


    @Override
    public void prepareSuccessView(JobPostingsOutputData outputData) {

    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
