package com.studyarc.use_case.job_postings;

import com.studyarc.entity.job_postings.KeywordList;
import com.studyarc.use_case.job_postings.generate_keywords.KeywordGenerator;

/**
 * The Job Postings Interactor.
 */
public class JobPostingsInteractor implements JobPostingsInputBoundary {
    private final JobPostingsDataAccessInterface userDataAccessObject;
    private final JobPostingsOutputBoundary jobPostingsPresenter;
    private final KeywordGenerator keywordGenerator;


    public JobPostingsInteractor(JobPostingsDataAccessInterface userDataAccessObject, JobPostingsOutputBoundary jobPostingsPresenter, KeywordGenerator keywordGenerator) {
        this.userDataAccessObject = userDataAccessObject;
        this.jobPostingsPresenter = jobPostingsPresenter;
        this.keywordGenerator = keywordGenerator;
    }

    @Override
    public void execute(JobPostingsInputData jobPostingsInputData) {
        // generates keywords for each focus the user has in their profile
        KeywordList keywords = keywordGenerator.generate(jobPostingsInputData.getFocus());
    }
}
