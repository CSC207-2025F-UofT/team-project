package service;

import entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;

import api.APIInterface;

import javax.swing.*;


/**
 * The Backend service class stores all core domain data for users, pets,
 * admins and adoption applications. It also provides methods for updating
 * application status and importing/exporting JSON data files.
 */
public class Backend {
	private User currentUser = null;

    private int userIdCounter = 0;
    private int petIdCounter = 0;
    private int applicationIdCounter = 0;

    private final Map<Integer, User> users = new HashMap<>();
    public final Map<Integer, Pet> pets = new HashMap<>();
    private final Map<Integer, Admin> admins = new HashMap<>();
    private final Map<Integer, Application> applications = new HashMap<>();

    public int newUserId() {
        return ++userIdCounter;
    }

    public int newPetId() {
        return ++petIdCounter;
    }

    public int newApplicationId() {
        return ++applicationIdCounter;
    }

    public void addUser(User user) {
        assert(!users.containsKey(user.getId()));
        users.put(user.getId(), user);
    }

    public User getUserById(int userId) {
        assert(!users.containsKey(userId));
        return users.get(userId);
    }

    public void addPet(Pet pet) {
        assert(!pets.containsKey(pet.getId()));
        pets.put(pet.getId(), pet);
    }

    public void removePet(Pet pet) {
        assert(pets.containsKey(pet.getId()));
        pets.remove(pet.getId());
    }

    public Pet getPetById(int petId) {
        assert(!pets.containsKey(petId));
        return pets.get(petId);
    }

//    public void addAdmin(Admin adminK) {
//        assert(!admins.containsKey(adminK.getId()));
//        admins.put(adminK.getId(), adminK);
//    }

    public void addApplication(Application application) {
        assert(!applications.containsKey(application.getId()));
        assert(pets.containsKey(application.getPetId()));
        applications.put(application.getId(), application);
    }

    public void removeApplication(Application application) {
        assert (applications.containsKey(application.getId()));
        applications.remove(application.getId());
    }

    /**
     * Returns an array of all unique animal species present in the pet data.
     *
     * @return an array of species names with duplicates removed
     */
    public String[] getUniqueSpecies() {
        final Set<String> speciesSeen = new HashSet<>();
        for (Pet pet : pets.values()) {
            speciesSeen.add(pet.getSpecies());
        }

        final String[] uniqueSpecies = new String[speciesSeen.size()];
        int i = 0;
        for (String species : speciesSeen) {
            uniqueSpecies[i++] = species;
        }

        return uniqueSpecies;
    }

    /**
     * Returns a list of applications filtered by a specific application status.
     *
     * @param status the target status to filter by
     * @return a list of all applications matching the given status
     */
    private List<Application> getApplicationsWithStatus(Application.Status status) {
        final List<Application> applicationsWithStatus = new ArrayList<>();

        for (Application application : applications.values()) {
            if (application.getStatus() == status) {
                applicationsWithStatus.add(application);
            }
        }

        return applicationsWithStatus;
    }

    public List<Application> getPendingApplications() {
        return getApplicationsWithStatus(Application.Status.PENDING);
    }

    public List<Application> getApprovedApplications() {
        return getApplicationsWithStatus(Application.Status.APPROVED);
    }

    public List<Application> getRejectedApplications() {
        return getApplicationsWithStatus(Application.Status.REJECTED);
    }

    /**
     * Retrieves all applications submitted for a specific pet.
     *
     * @param petId the pet's ID
     * @return a list of applications belonging to this pet
     */
    private List<Application> getApplicationsForPet(int petId) {
        final List<Application> forPet = new ArrayList<>();

        for (Application application : applications.values()) {
            if (application.getPetId() == petId) {
                forPet.add(application);
            }
        }

        return forPet;
    }

    /**
     * Approves an application and marks the related pet as adopted.
     * Also automatically rejects all other pending applications for the same pet.
     *
     * @param applicationId the ID of the application to approve
     */
    public void approveApplication(int applicationId) {
        assert(applications.containsKey(applicationId));
        final Application app = applications.get(applicationId);
        app.setStatus(Application.Status.APPROVED);

        // mark pet as adopted
        final int petId = app.getPetId();
        assert (pets.containsKey(petId));
        final Pet pet = pets.get(petId);
        assert (!pet.isAdopted());
        pet.setAdopted();

        // reject all other applications for the same pet
        List<Application> otherAppsForSamePet = getApplicationsForPet(petId);
        for (Application otherApp : otherAppsForSamePet) {
            if (otherApp.getId() != applicationId) {
                assert (otherApp.getStatus() == Application.Status.PENDING);
                rejectApplication(otherApp.getId());
            }
        }
    }

    /**
     * Rejects an application by setting its status to REJECTED.
     *
     * @param applicationId the ID of the application to reject
     */
    public void rejectApplication(int applicationId) {
        assert (applications.containsKey(applicationId));
        final Application app = applications.get(applicationId);
        app.setStatus(Application.Status.REJECTED);
    }

    // ==== IMPORT EXPORT METHODS
    /**
     * Serializes all user objects into a JSON array.
     *
     * @return a JSONArray representing all users
     */
    private JSONArray usersToJson() {
        final JSONArray usersJson = new JSONArray();

        for (User user : users.values()) {
            usersJson.put(user.toJson());
        }

        return usersJson;
    }

    /**
     * Loads users from a JSONArray and inserts them into memory.
     *
     * @param usersJson array of user JSON objects
     */
    private void loadUsersFromJson(JSONArray usersJson) {
        // Get an iterator over the keys
        for (int i = 0; i < usersJson.length(); i++) {
            // Get each JSONObject
            final User user = new User(usersJson.getJSONObject(i));
            addUser(user);
        }
    }

    private JSONArray petsToJson() {
        final JSONArray petsJson = new JSONArray();

        for (Pet pet : pets.values()) {
            petsJson.put(pet.toJson());
        }

        return petsJson;
    }

    private void loadPetsFromJson(JSONArray petsJson) {
        for (int i = 0; i < petsJson.length(); i++) {
            final Pet pet = new Pet(petsJson.getJSONObject(i));
            addPet(pet);
        }
    }

    private JSONArray adminsToJson() {
        final JSONArray adminsJson = new JSONArray();

        for (Admin adminK : admins.values()) {
            adminsJson.put(adminK.toJson());
        }

        return adminsJson;
    }

//    private void loadAdminsFromJson(JSONArray adminsJson) {
//        for (int i = 0; i < adminsJson.length(); i++) {
//            final Admin adminK = new Admin(adminsJson.getJSONObject(i));
//            addAdmin(adminK);
//        }
//    }

    private JSONArray applicationsToJson() {
        final JSONArray applicationsJson = new JSONArray();

        for (Application application : applications.values()) {
            applicationsJson.put(application.toJson());
        }

        return applicationsJson;
    }

    private void loadApplicationsFromJson(JSONArray applicationsJson) {
        for (int i = 0; i < applicationsJson.length(); i++) {
            final Application application = new Application(applicationsJson.getJSONObject(i));
            addApplication(application);
        }
    }

    /**
     * Loads all domain objects (users, pets, admins, applications)
     * from the given JSON files.
     *
     * @param usersPath        path to users.json
     * @param petsPath         path to pets.json
     * @param applicationsPath path to applications.json
     */
    public void fromJsonFiles(String usersPath, String petsPath, String applicationsPath) {
        try {
            final String usersData = new String(Files.readAllBytes(Paths.get(usersPath)));
            final JSONArray usersJson = new JSONArray(usersData);
            loadUsersFromJson(usersJson);
        }
        catch (IOException ex) {
            System.err.println("Error while reading users file");
            throw new RuntimeException(ex);
        }

        try {
            final String petsData = new String(Files.readAllBytes(Paths.get(petsPath)));
            final JSONArray petsJson = new JSONArray(petsData);
            loadPetsFromJson(petsJson);
        }
        catch (IOException ex) {
            System.err.println("Error while reading pets file");
            throw new RuntimeException(ex);
        }
//
//        try {
//            final String adminsData = new String(Files.readAllBytes(Paths.get(adminsPath)));
//            final JSONArray adminsJson = new JSONArray(adminsData);
//            loadAdminsFromJson(adminsJson);
//        }
//        catch (IOException ex) {
//            System.err.println("Error while reading admins file");
//            throw new RuntimeException(ex);
//        }

        try {
            final String applicationsData = new String(Files.readAllBytes(Paths.get(applicationsPath)));
            final JSONArray applicationsJson = new JSONArray(applicationsData);
            loadApplicationsFromJson(applicationsJson);
        }
        catch (IOException ex) {
            System.err.println("Error while reading applications file");
            throw new RuntimeException(ex);
        }
    }

    /**
     * Saves all domain objects to their respective JSON files in pretty format.
     *
     * @param usersPath        output file for users
     * @param petsPath         output file for pets
     * @param applicationsPath output file for applications
     */
    public void toJsonFiles(String usersPath, String petsPath, String applicationsPath) {
        try (FileWriter usersFile = new FileWriter(usersPath)) {
            usersFile.write(usersToJson().toString(4));
            usersFile.flush();
            System.out.println("Users written to file successfully");
        }
        catch (IOException ex) {
            System.err.println("Error while writing users file");
            throw new RuntimeException(ex);
        }

        try (FileWriter petsFile = new FileWriter(petsPath)) {
            petsFile.write(petsToJson().toString(4));
            petsFile.flush();
            System.out.println("Pets written to file successfully");
        }
        catch (IOException ex) {
            System.err.println("Error while writing pets file");
            throw new RuntimeException(ex);
        }
//        try (FileWriter adminsFile = new FileWriter(adminsPath)) {
//            adminsFile.write(adminsToJson().toString(4));
//            adminsFile.flush();
//            System.out.println("Admins written to file successfully");
//        }
//        catch (IOException ex) {
//            System.err.println("Error while writing admins file");
//            throw new RuntimeException(ex);
//        }

        try (FileWriter applicationsFile = new FileWriter(applicationsPath)) {
            applicationsFile.write(applicationsToJson().toString(4));
            applicationsFile.flush();
            System.out.println("Applications written to file successfully");
        }
        catch (IOException ex) {
            System.err.println("Error while writing applications file");
            throw new RuntimeException(ex);
        }
    }

    /**
     * Fetches pets from the Petfinder API and merges them into the in-memory pets map.
     * Only adds pets whose IDs do not already exist locally (to avoid collisions).
     */
    public void importPetsFromApi() {
        APIInterface api = new APIInterface();
        try {
            List<Pet> apiPets = api.getPets();

            int added = 0;
            for (Pet pet : apiPets) {
                if (!pets.containsKey(pet.getId())) {
                    addPet(pet);
                    added++;
                }
            }
            // return added to presenter
            System.out.println("Imported " + added + " pets from Petfinder API.");
        } catch (Exception e) {
            System.err.println("Failed to import pets from Petfinder API");
            e.printStackTrace();
        }
    }

    public void getPetsFromApi() {
        String message = getApiMessage();
        JOptionPane.showMessageDialog(
                null,
                message,
                "Petfinder API",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    public String getApiMessage() {
        APIInterface api = new APIInterface();
        try {
            List<Pet> apiPets = api.getPets();

            int added = 0;
            for (Pet pet : apiPets) {
                if (!pets.containsKey(pet.getId())) {
                    addPet(pet);
                    added++;
                }
            }
            // return added to presenter
            return ("Add " + added + " pets from Petfinder API.");
        } catch (Exception e) {
            return ("Failed to import pets from Petfinder API");
        }
    }


    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
