package com.studyarc.use_case.job_postings.generate_postings;

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
        String url = "https://api.adzuna.com/v1/api/jobs/" + countryCode +"/search/1?app_id=" + API_KEY + "&app_key=" + API_ID + "&results_per_page=20&what=";

        // adds the keywords
        url += "Machine%20Learning Deep%20Learning Neural%20Networks AI Artificial%20Intelligence Data%20Science Reinforcement"
                + "&content-type=application/json";

        final Request request = new Request.Builder().url(url).build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            System.out.println("Getting jobs from api...");
            // gets the status of the call
//            String status = responseBody.getString("status");

            System.out.println(responseBody);

            // checks if its successful
//            if (!status.equals("Not Found")) {
//                System.out.println(responseBody);
//            } else {
//                System.out.println("Didn't work :(");
//            }
    } catch (IOException e) {
            System.out.println("Something went wrong :(");
        }


    }}
