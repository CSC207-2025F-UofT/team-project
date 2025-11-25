package service;

import entity.Admin;
import entity.Application;
import entity.Pet;
import entity.User;

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

public class Backend {
    private int userIdCounter = 0;
    private int petIdCounter = 0;
    private int adminIdCounter = 0;
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

    public int newAdminId() {
        return ++adminIdCounter;
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

    public void addAdmin(Admin admin) {
        assert(!admins.containsKey(admin.getId()));
        admins.put(admin.getId(), admin);
    }

    public void addApplication(Application application) {
        assert(!applications.containsKey(application.getId()));
        assert(pets.containsKey(application.getPetId()));
        applications.put(application.getId(), application);
    }

    public void removeApplication(Application application) {
        assert(applications.containsKey(application.getId()));
        applications.remove(application.getId());
    }

    public String[] getUniqueSpecies() {
        Set<String> speciesSeen = new HashSet<>();
        for (Pet pet : pets.values()) {
            speciesSeen.add(pet.getSpecies());
        }

        String[] uniqueSpecies = new String[speciesSeen.size()];
        int i = 0;
        for (String species : speciesSeen) {
            uniqueSpecies[i++] = species;
        }

        return uniqueSpecies;
    }

    private List<Application> getApplicationsWithStatus(Application.Status status) {
        List<Application> applicationsWithStatus = new ArrayList<>();

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

    private List<Application> getApplicationsForPet(int petId) {
        List<Application> forPet = new ArrayList<>();

        for (Application application : applications.values()) {
            if (application.getPetId() == petId) {
                forPet.add(application);
            }
        }

        return forPet;
    }


    public void approveApplication(int applicationId) {
        assert(applications.containsKey(applicationId));
        Application app = applications.get(applicationId);
        app.setStatus(Application.Status.APPROVED);

        // mark pet as adopted
        int petId = app.getPetId();
        assert(pets.containsKey(petId));
        Pet pet = pets.get(petId);
        assert(!pet.isAdopted());
        pet.setAdopted();

        // reject all other applications for the same pet
        List<Application> otherAppsForSamePet = getApplicationsForPet(petId);
        for (Application otherApp : otherAppsForSamePet) {
            if (otherApp.getId() != applicationId) {
                assert(otherApp.getStatus() == Application.Status.PENDING);
                rejectApplication(otherApp.getId());
            }
        }
    }

    public void rejectApplication(int applicationId) {
        assert(applications.containsKey(applicationId));
        Application app = applications.get(applicationId);
        app.setStatus(Application.Status.REJECTED);
    }


    // ==== IMPORT EXPORT METHODS
    private JSONArray usersToJson() {
        JSONArray usersJson = new JSONArray();

        for (User user : users.values()) {
            usersJson.put(user.toJson());
        }

        return usersJson;
    }

    private void loadUsersFromJson(JSONArray usersJson) {
        // Get an iterator over the keys
        for (int i = 0; i < usersJson.length(); i++) {
            User user = new User(usersJson.getJSONObject(i)); // Get each JSONObject
            addUser(user);
        }
    }

    private JSONArray petsToJson() {
        JSONArray petsJson = new JSONArray();

        for (Pet pet : pets.values()) {
            petsJson.put(pet.toJson());
        }

        return petsJson;
    }

    private void loadPetsFromJson(JSONArray petsJson) {
        for (int i = 0; i < petsJson.length(); i++) {
            Pet pet = new Pet(petsJson.getJSONObject(i));
            addPet(pet);
        }
    }

    private JSONArray adminsToJson() {
        JSONArray adminsJson = new JSONArray();

        for (Admin admin : admins.values()) {
            adminsJson.put(admin.toJson());
        }

        return adminsJson;
    }

    private void loadAdminsFromJson(JSONArray adminsJson) {
        for (int i = 0; i < adminsJson.length(); i++) {
            Admin admin = new Admin(adminsJson.getJSONObject(i));
            addAdmin(admin);
        }
    }

    private JSONArray applicationsToJson() {
        JSONArray applicationsJson = new JSONArray();

        for (Application application : applications.values()) {
            applicationsJson.put(application.toJson());
        }

        return applicationsJson;
    }

    private void loadApplicationsFromJson(JSONArray applicationsJson) {
        for (int i = 0; i < applicationsJson.length(); i++) {
            Application application = new Application(applicationsJson.getJSONObject(i));
            addApplication(application);
        }
    }

    public void fromJsonFiles(String usersPath, String petsPath, String adminsPath, String applicationsPath) {
        try {
            String usersData = new String(Files.readAllBytes(Paths.get(usersPath)));
            JSONArray usersJson = new JSONArray(usersData);
            loadUsersFromJson(usersJson);
        } catch (IOException ex) {
            System.err.println("Error while reading users file");
            throw new RuntimeException(ex);
        }

        try {
            String petsData = new String(Files.readAllBytes(Paths.get(petsPath)));
            JSONArray petsJson = new JSONArray(petsData);
            loadPetsFromJson(petsJson);
        } catch (IOException ex) {
            System.err.println("Error while reading pets file");
            throw new RuntimeException(ex);
        }

        try {
            String adminsData = new String(Files.readAllBytes(Paths.get(adminsPath)));
            JSONArray adminsJson = new JSONArray(adminsData);
            loadAdminsFromJson(adminsJson);
        } catch (IOException ex) {
            System.err.println("Error while reading admins file");
            throw new RuntimeException(ex);
        }

        try {
            String applicationsData = new String(Files.readAllBytes(Paths.get(applicationsPath)));
            JSONArray applicationsJson = new JSONArray(applicationsData);
            loadApplicationsFromJson(applicationsJson);
        } catch (IOException ex) {
            System.err.println("Error while reading applications file");
            throw new RuntimeException(ex);
        }
    }

    public void toJsonFiles(String usersPath, String petsPath, String adminsPath, String applicationsPath) {
        try (FileWriter usersFile = new FileWriter(usersPath)) {
            usersFile.write(usersToJson().toString(4));
            usersFile.flush();
            System.out.println("Users written to file successfully");
        } catch (IOException ex) {
            System.err.println("Error while writing users file");
            throw new RuntimeException(ex);
        }

        try (FileWriter petsFile = new FileWriter(petsPath)) {
            petsFile.write(petsToJson().toString(4));
            petsFile.flush();
            System.out.println("Pets written to file successfully");
        } catch (IOException ex) {
            System.err.println("Error while writing pets file");
            throw new RuntimeException(ex);
        }

        try (FileWriter adminsFile = new FileWriter(adminsPath)) {
            adminsFile.write(adminsToJson().toString(4));
            adminsFile.flush();
            System.out.println("Admins written to file successfully");
        } catch (IOException ex) {
            System.err.println("Error while writing admins file");
            throw new RuntimeException(ex);
        }

        try (FileWriter applicationsFile = new FileWriter(applicationsPath)) {
            applicationsFile.write(applicationsToJson().toString(4));
            applicationsFile.flush();
            System.out.println("Applications written to file successfully");
        } catch (IOException ex) {
            System.err.println("Error while writing applications file");
            throw new RuntimeException(ex);
        }
    }
}
