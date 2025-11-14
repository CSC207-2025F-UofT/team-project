package com.studyarc.use_case.job_postings.generate_postings;

import com.openai.core.JsonObject;
import com.studyarc.entity.job_postings.JobListing;
import com.studyarc.entity.job_postings.KeywordList;
import com.studyarc.use_case.job_postings.JobPostingsInputData;

import io.github.cdimascio.dotenv.Dotenv;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import java.util.List;

public class AdzunaJobGenerator implements JobRepository {

    private static final Dotenv DOTENV = Dotenv.load();
    private static final String API_KEY = DOTENV.get("ADZUNA_API_KEY");
    private static final String API_ID = DOTENV.get("ADZUNA_ID");


    private static final OkHttpClient client = new OkHttpClient();

    @Override
    public List<JobListing> getJobListings(KeywordList keywords) {
        return List.of();
    }

    public static void main(String[] args) throws IOException {
        getJobs("gb");
    }

    public static void getJobs(String countryCode) throws IOException {
        String url = "https://api.adzuna.com/v1/api/jobs/" + countryCode +"/search/1?app_id=" + API_ID + "&app_key=" + API_KEY + "&results_per_page=20&what_or=";

        // adds the keywords
        url += "machine%20learning%20ml%20deep%20learning%20neural%20networks%20data%20science%20ai%20artificial%20intelligence"
                + "&content-type=application/json";

        System.out.println(url);

        final Request request = new Request.Builder().url(url).build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            System.out.println("Getting jobs from api...");

            try {
                if (responseBody.get("results") != null) {

                    // gets the list of job listings from the api
                    JSONArray jobResults = responseBody.getJSONArray("results");

                    // creates the list that will be returned containing the JobListing entities
                    List<JobListing> listings = new ArrayList<>();

                    for (int i = 0; i < jobResults.length(); i++) {
                        JSONObject job = jobResults.getJSONObject(i);
                        JSONObject jobCompany = job.getJSONObject("company");
                        JSONObject jobLocation = job.getJSONObject("location");

//                        System.out.println(job);
//                        System.out.println(jobCompany.get("display_name"));
//                        System.out.println(jobLocation.get("display_name"));

                        JobListing newJob = new JobListing(job.get("title").toString(), Long.parseLong(job.get("id").toString()), jobCompany.get("display_name").toString(), Double.parseDouble(job.get("salary_min").toString()), Double.parseDouble(job.get("salary_max").toString()), job.get("description").toString(), jobLocation.get("display_name").toString(), job.get("redirect_url").toString());
                        listings.add(newJob);

//                        System.out.println(newJob.getJobId() + " " + newJob.getTitle() + " " + newJob.getCompanyName() + " " + newJob.getSalaryMin() + " " + newJob.getSalaryMax() + " " + newJob.getJobLoc() + " " + newJob.getRedirectUrl() + " " + newJob.getJobDesc());

                    }

                    System.out.println("Finished");


                }

            } catch (Exception e) {
                throw new IOException(e);
            }

    } catch (IOException e) {
            System.out.println("Something went wrong :(");
            System.out.println(e.getMessage());
        }


    }}
