package com.studyarc.use_case.job_postings;

/**
 * The Job Postings Interactor.
 */
public class JobPostingsInteractor implements JobPostingsInputBoundary {
    private final JobPostingsDataAccessInterface userDataAccessObject;
    private final JobPostingsOutputBoundary jobPostingsPresenter;

    public JobPostingsInteractor(JobPostingsDataAccessInterface userDataAccessObject, JobPostingsOutputBoundary jobPostingsPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.jobPostingsPresenter = jobPostingsPresenter;
    }

}
