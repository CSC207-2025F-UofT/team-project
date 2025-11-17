package com.studyarc.interface_adapter.job_postings;

import com.studyarc.use_case.job_postings.JobPostingsInputBoundary;

/**
 * The controller for the Job Postings Use Case.
 */
public class JobPostingsController {
    private final JobPostingsInputBoundary jobPostingsUseCaseInteractor;

    public JobPostingsController(JobPostingsInputBoundary jobPostingsUseCaseInteractor) {
        this.jobPostingsUseCaseInteractor = jobPostingsUseCaseInteractor;
    }
}
