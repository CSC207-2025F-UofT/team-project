package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Application;
import entity.ApplicationStatus;
import use_case.review_applications.ReviewApplicationsDataAccessInterface;
import use_case.submit_application.SubmitApplicationDataAccessInterface;
import use_case.track_applications.TrackApplicationsDataAccessInterface;

/**
 * DAO for applications, stored in a JSON file so they are kept between runs of the program.
 * Saving an application replaces the saved one with the same id. Changes to an application
 * are stored only when it is saved.
 */
public class FileApplicationDataAccessObject implements SubmitApplicationDataAccessInterface,
        TrackApplicationsDataAccessInterface, ReviewApplicationsDataAccessInterface {

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
            replaceFile(array.toString(2));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // The old file is replaced only after the new one is completely written,
    // so a save that fails halfway cannot damage it.
    private void replaceFile(String text) throws IOException {
        final Path target = jsonPath.toAbsolutePath();
        // Needed because moving a file onto a read-only file would succeed on macOS and Linux.
        if (Files.exists(target) && !Files.isWritable(target)) {
            throw new AccessDeniedException(target.toString());
        }
        final Path temporaryFile = Files.createTempFile(target.getParent(), "applications-", ".tmp");
        try {
            Files.write(temporaryFile, text.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.WRITE, StandardOpenOption.SYNC);
            Files.move(temporaryFile, target,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        }
        finally {
            Files.deleteIfExists(temporaryFile);
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
        final Application previous = applications.put(id, application.copy());
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
                result.add(application.copy());
            }
        }
        return result;
    }

    @Override
    public Application getApplicationById(String applicationId) {
        final Application application = applications.get(applicationId);
        if (application == null) {
            return null;
        }
        return application.copy();
    }

    @Override
    public List<Application> getAllApplications() {
        final List<Application> result = new ArrayList<>();
        for (Application application : applications.values()) {
            result.add(application.copy());
        }
        return result;
    }
}
