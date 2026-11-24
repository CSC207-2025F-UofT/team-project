package data_access;

import entity.Job;
import entity.JobFactory;
import entity.Student;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.job_match.JobFetchException;
import use_case.job_match.JobMatchUserDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Job DAO backed by the Adzuna job search API (https://developer.adzuna.com/).
 *
 * <p>Jobs are fetched with one search for any of the student's skills. Every job that is fetched
 * is stored in memory, so repeated requests for the same set of skills do not call the API
 * again, and other parts of the program can look jobs up by id.</p>
 *
 * <p>The Adzuna credentials are read with {@link EnvConfig} from {@code ADZUNA_APP_ID} and
 * {@code ADZUNA_APP_KEY} (environment variables or the {@code .env} file).</p>
 */
public class JobDataAccessObject implements JobMatchUserDataAccessInterface {

    private static final String BASE_URL = "https://api.adzuna.com/v1/api/jobs/";
    private static final String APP_ID_NAME = "ADZUNA_APP_ID";
    private static final String APP_KEY_NAME = "ADZUNA_APP_KEY";
    private static final int RESULTS_PER_PAGE = 50;
    /** How many jobs to show a student who has no skills yet. */
    private static final int NO_SKILLS_RESULTS = 5;
    private static final int DATE_LENGTH = 10;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final JobFactory jobFactory;
    private final String countryCode;
    private final String location;

    /** Every job fetched so far, keyed by job id. */
    private final Map<Integer, Job> jobsById = new LinkedHashMap<>();
    /** Every job fetched so far, keyed by Adzuna's id (used to avoid duplicates). */
    private final Map<String, Job> jobsByAdzunaId = new HashMap<>();
    /** Cached search results, keyed by the (normalized) set of skills searched. */
    private final Map<String, ArrayList<Job>> resultsBySkills = new HashMap<>();

    /**
     * Creates a DAO that searches for jobs in Toronto, Canada.
     */
    public JobDataAccessObject() {
        this(new JobFactory(), "ca", "Toronto");
    }

    /**
     * Creates a DAO that searches for jobs in the given country and location.
     * @param jobFactory factory used to create Job entities
     * @param countryCode Adzuna country code, e.g. "ca", "us", "gb"
     * @param location location to search in, or an empty string for country-wide
     */
    public JobDataAccessObject(JobFactory jobFactory, String countryCode, String location) {
        this.jobFactory = jobFactory;
        this.countryCode = countryCode;
        this.location = location;
    }

    @Override
    public ArrayList<Job> getJobs(Student student) {
        final List<String> skills = normalizeSkills(student.getSkills());

        // An empty key holds the default jobs shown to students with no skills yet.
        final String cacheKey = String.join("|", skills);
        final ArrayList<Job> cached = resultsBySkills.get(cacheKey);
        if (cached != null) {
            return new ArrayList<>(cached);
        }

        final int resultsWanted;
        if (skills.isEmpty()) {
            resultsWanted = NO_SKILLS_RESULTS;
        }
        else {
            resultsWanted = RESULTS_PER_PAGE;
        }
        final ArrayList<Job> results = search(skills, resultsWanted);
        // Jobs that mention more of the student's skills come first.
        results.sort(Comparator.comparingInt((Job job) -> job.getSkillsNeeded().size()).reversed());
        resultsBySkills.put(cacheKey, results);
        return new ArrayList<>(results);
    }

    /**
     * Returns the job with the given id, if it has been fetched.
     * @param id the job id
     * @return the job, or null if no job with that id is stored
     */
    public Job getJob(int id) {
        return jobsById.get(id);
    }

    /**
     * Returns every job fetched so far.
     * @return all stored jobs, in the order they were fetched
     */
    public ArrayList<Job> getAllJobs() {
        return new ArrayList<>(jobsById.values());
    }

    /**
     * Clears all stored jobs and cached search results.
     */
    public void clear() {
        jobsById.clear();
        jobsByAdzunaId.clear();
        resultsBySkills.clear();
    }

    /**
     * Searches Adzuna for jobs matching any of the given skills.
     * @param skills the skills to search for; if empty, returns general jobs in the location
     * @param resultsWanted how many jobs to ask for
     */
    private ArrayList<Job> search(List<String> skills, int resultsWanted) {
        final String appId = EnvConfig.get(APP_ID_NAME);
        final String appKey = EnvConfig.get(APP_KEY_NAME);
        if (appId == null || appKey == null) {
            throw new JobFetchException("Job search is unavailable: set " + APP_ID_NAME + " and "
                    + APP_KEY_NAME + " in the .env file.");
        }

        final StringBuilder query = new StringBuilder(BASE_URL)
                .append(encode(countryCode)).append("/search/1")
                .append("?app_id=").append(encode(appId))
                .append("&app_key=").append(encode(appKey))
                .append("&results_per_page=").append(resultsWanted)
                .append("&content-type=application/json");
        if (!skills.isEmpty()) {
            // what_or matches jobs containing any of the words.
            query.append("&what_or=").append(encode(String.join(" ", skills)));
        }
        if (location != null && !location.isBlank()) {
            query.append("&where=").append(encode(location));
        }

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query.toString()))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        final HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (IOException exception) {
            throw new JobFetchException("Could not reach the job search service.", exception);
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new JobFetchException("Job search was interrupted.", exception);
        }

        if (response.statusCode() != 200) {
            throw new JobFetchException("Job search failed (HTTP " + response.statusCode() + "): "
                    + response.body());
        }
        return parseJobs(response.body(), skills);
    }

    private ArrayList<Job> parseJobs(String body, Collection<String> studentSkills) {
        final ArrayList<Job> jobs = new ArrayList<>();
        try {
            final JSONArray results = new JSONObject(body).optJSONArray("results");
            if (results == null) {
                return jobs;
            }
            for (int i = 0; i < results.length(); i++) {
                jobs.add(toJob(results.getJSONObject(i), studentSkills));
            }
        }
        catch (JSONException exception) {
            throw new JobFetchException("Job search returned an unexpected response.", exception);
        }
        return jobs;
    }

    private Job toJob(JSONObject jobJson, Collection<String> studentSkills) {
        final String adzunaId = jobJson.optString("id");
        final Job existing = jobsByAdzunaId.get(adzunaId);
        if (existing != null) {
            return existing;
        }

        final String title = stripHtml(jobJson.optString("title"));
        final String description = stripHtml(jobJson.optString("description"));
        final Job job = jobFactory.create(
                title,
                matchingSkills(title + " " + description, studentSkills),
                displayName(jobJson.optJSONObject("company")),
                formatDate(jobJson.optString("created")),
                description,
                displayName(jobJson.optJSONObject("location")),
                formatSalary(jobJson),
                jobJson.optString("redirect_url"));

        jobsById.put(job.getId(), job);
        if (!adzunaId.isEmpty()) {
            jobsByAdzunaId.put(adzunaId, job);
        }
        return job;
    }

    /**
     * Adzuna does not return a structured skill list, so the skills needed for a job are the
     * student's skills that appear in the job's title or description.
     */
    private static ArrayList<String> matchingSkills(String text, Collection<String> studentSkills) {
        final ArrayList<String> matches = new ArrayList<>();
        for (String skill : studentSkills) {
            // Match whole words only, so "Git" does not match "digital" (works for "C++"/"C#" too).
            final Pattern pattern = Pattern.compile(
                    "(?<![\\p{Alnum}])" + Pattern.quote(skill) + "(?![\\p{Alnum}+#])",
                    Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(text).find()) {
                matches.add(skill);
            }
        }
        return matches;
    }

    private static String displayName(JSONObject object) {
        if (object == null) {
            return "";
        }
        return stripHtml(object.optString("display_name"));
    }

    /** Turns Adzuna's ISO timestamp (e.g. 2026-11-20T18:07:39Z) into a date like 2026-11-20. */
    private static String formatDate(String created) {
        if (created.length() >= DATE_LENGTH) {
            return created.substring(0, DATE_LENGTH);
        }
        return created;
    }

    private static String formatSalary(JSONObject jobJson) {
        final boolean hasMin = jobJson.has("salary_min");
        final boolean hasMax = jobJson.has("salary_max");
        if (!hasMin && !hasMax) {
            return "";
        }
        final NumberFormat money = NumberFormat.getCurrencyInstance(Locale.CANADA);
        money.setMaximumFractionDigits(0);
        final double min = jobJson.optDouble("salary_min", jobJson.optDouble("salary_max"));
        final double max = jobJson.optDouble("salary_max", min);

        String salary = money.format(min);
        if (max != min) {
            salary += " - " + money.format(max);
        }
        if (jobJson.optInt("salary_is_predicted", 0) == 1) {
            salary += " (estimated)";
        }
        return salary;
    }

    private static List<String> normalizeSkills(List<String> skills) {
        final TreeSet<String> normalized = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (String skill : skills) {
            if (skill != null && !skill.isBlank()) {
                normalized.add(skill.trim());
            }
        }
        return new ArrayList<>(normalized);
    }

    private static String stripHtml(String text) {
        return text.replaceAll("<[^>]*>", "")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .trim();
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
