package com.studyarc.use_case.job_postings.generate_postings;

import com.studyarc.entity.job_postings.JobListing;
import com.studyarc.entity.job_postings.KeywordList;
import com.studyarc.use_case.job_postings.JobPostingsInputData;

import java.util.List;

public class AdzunaJobGenerator implements JobRepository {

    @Override
    public List<JobListing> getJobListings(KeywordList keywords) {
        return List.of();
    }
}
