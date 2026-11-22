package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Application;
import entity.ApplicationStatus;
import use_case.submit_application.SubmitApplicationDataAccessInterface;
import use_case.track_applications.TrackApplicationsDataAccessInterface;

/**
 * DAO for applications, stored in a JSON file so they are kept between runs of the program.
 * Saving an application replaces the saved one with the same id.
 */
public class FileApplicationDataAccessObject implements SubmitApplicationDataAccessInterface,
        TrackApplicationsDataAccessInterface {

    private final Path jsonPath;
    private final Map<String, Application> applications = new LinkedHashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param jsonPath the path of the file to save to
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileApplicationDataAccessObject(String jsonPath) {
        this.jsonPath = Paths.get(jsonPath);
        if (Files.exists(this.jsonPath)) {
            load();
        }
    }

    private void load() {
        try {
            final String text = new String(Files.readAllBytes(jsonPath), StandardCharsets.UTF_8);
            final JSONArray array = new JSONArray(text);
            for (int i = 0; i < array.length(); i++) {
                final JSONObject json = array.getJSONObject(i);
                final Application application = new Application(json.getString("id"), json.getString("adopterId"),
                        json.getString("petId"), json.getString("housingType"), json.getString("petExperience"),
                        json.getString("householdMembers"), json.getString("contactInfo"),
                        json.getString("note"), LocalDate.parse(json.getString("submittedAt")),
                        ApplicationStatus.valueOf(json.getString("status")), json.getString("adminNote"));
                applications.put(application.getId(), application);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void write() {
        final JSONArray array = new JSONArray();
        for (Application application : applications.values()) {
            final JSONObject json = new JSONObject();
            json.put("id", application.getId());
            json.put("adopterId", application.getAdopterId());
            json.put("petId", application.getPetId());
            json.put("housingType", application.getHousingType());
            json.put("petExperience", application.getPetExperience());
            json.put("householdMembers", application.getHouseholdMembers());
            json.put("contactInfo", application.getContactInfo());
            json.put("note", application.getNote());
            json.put("submittedAt", application.getSubmittedAt().toString());
            json.put("status", application.getStatus().name());
            json.put("adminNote", application.getAdminNote());
            array.put(json);
        }
        try {
            Files.write(jsonPath, array.toString(2).getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean hasActiveApplication(String adopterId, String petId) {
        for (Application application : applications.values()) {
            if (application.getAdopterId().equals(adopterId) && application.getPetId().equals(petId)
                    && application.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Application application) {
        final String id = application.getId();
        final Application previous = applications.put(id, application);
        try {
            write();
        }
        catch (RuntimeException ex) {
            if (previous == null) {
                applications.remove(id);
            }
            else {
                applications.put(id, previous);
            }
            throw ex;
        }
    }

    @Override
    public List<Application> getApplicationsByAdopter(String adopterId) {
        final List<Application> result = new ArrayList<>();
        for (Application application : applications.values()) {
            if (application.getAdopterId().equals(adopterId)) {
                result.add(application);
            }
        }
        return result;
    }
}
