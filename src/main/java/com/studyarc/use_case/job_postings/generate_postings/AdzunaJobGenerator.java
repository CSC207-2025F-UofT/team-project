package com.studyarc.use_case.job_postings.generate_postings;

import com.studyarc.entity.job_postings.JobListing;
import com.studyarc.entity.job_postings.KeywordList;
import com.studyarc.use_case.job_postings.JobPostingsInputData;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class AdzunaJobGenerator implements JobRepository {

    private static final Dotenv DOTENV = Dotenv.load();
    private static final String API_KEY = DOTENV.get("ADZUNA_API_KEY");
    private static final String API_ID = DOTENV.get("ADZUNA_ID");

    @Override
    public List<JobListing> getJobListings(KeywordList keywords) {
        return List.of();
    }

    public static void main(String[] args) {

    }
}
