package com.studyarc.use_case.job_postings;

import com.studyarc.entity.job_postings.JobListing;
import com.studyarc.entity.job_postings.KeywordList;
import com.studyarc.use_case.job_postings.generate_keywords.KeywordGenerator;
import com.studyarc.use_case.job_postings.generate_postings.AdzunaJobGenerator;

import java.util.List;

/**
 * The Job Postings Interactor.
 */
public class JobPostingsInteractor implements JobPostingsInputBoundary {
    private final JobPostingsDataAccessInterface userDataAccessObject;
    private final JobPostingsOutputBoundary jobPostingsPresenter;
    private final KeywordGenerator keywordGenerator;
    private final AdzunaJobGenerator jobGenerator;


    public JobPostingsInteractor(JobPostingsDataAccessInterface userDataAccessObject, JobPostingsOutputBoundary jobPostingsPresenter, KeywordGenerator keywordGenerator, AdzunaJobGenerator jobGenerator) {
        this.userDataAccessObject = userDataAccessObject;
        this.jobPostingsPresenter = jobPostingsPresenter;
        this.keywordGenerator = keywordGenerator;
        this.jobGenerator = jobGenerator;
    }

    @Override
    public void execute(JobPostingsInputData jobPostingsInputData) {
        // generates keywords for the focus the user selected
        KeywordList keywords = keywordGenerator.generate(jobPostingsInputData.getFocus());

        // generates the job listings for the given keywords
        List<JobListing> jobListings = jobGenerator.getJobListings(keywords);

        // creates the output data object
        final JobPostingsOutputData jobPostingsOutputData = new JobPostingsOutputData(jobListings);

        // sends the success view
        jobPostingsPresenter.prepareSuccessView(jobPostingsOutputData);
    }
}
