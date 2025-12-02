package service;

import api.APIInterface;
import entity.*;
import org.json.JSONArray;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static entity.User.idType.PHOTO_CARD;

public class DummyDataGenerator {
    public static void main(String[] args) throws Exception {
        final Random random = new Random();

        final int num_users = 20;
        final int num_admins = 30;
        final int num_applications = 20;

        // Users
        JSONArray users = new JSONArray();

        final User.idType[] idTypes = User.idType.values();
        for (int i = 1; i <= num_users; i++) {
            User userK = new User(
                    i,
                    "User " + i,
                    "Address " + i,
                    idTypes[random.nextInt(idTypes.length)],
                    i,
                    "user"+i+"@example.com",
                    "user" + i,
                    "pass" + i,
                    i == 1 ? "admin" :  "user"
            );

            users.put(userK.toJson());
        }

        // Pets
        JSONArray pets = new JSONArray();
        APIInterface api = new APIInterface();
        List<Integer> seenPetIds = new ArrayList<>();
        for (Pet pet : api.getPets()) {
            pets.put(pet.toJson());
            seenPetIds.add(pet.getId());
        }

        // Admins
        JSONArray admins = new JSONArray();

        for (int i = 1; i <= num_admins; i++) {
            User adminK = new User(
                    i,
                    "Admin" + i,
                    "address" + i,
                    PHOTO_CARD,
                    i,
                    "admin"+i+"@pet.com",
                    "admin" + i,
                    "pass" + i,
                    "admin"
            );

            admins.put(adminK.toJson());
        }

        // Applications
        JSONArray applications = new JSONArray();

        for (int i = 1; i <= num_applications; i++) {
            Application application = new Application(
                    i,
                    i,
                    seenPetIds.get(random.nextInt(seenPetIds.size()))
            );

            applications.put(application.toJson());
        }

        // Save files
        // try (FileWriter fw = new FileWriter("users.json")) { fw.write(users.toString(4)); }
        try (FileWriter fw = new FileWriter("pets.json")) { fw.write(pets.toString(4)); }
        try (FileWriter fw = new FileWriter("applications.json")) { fw.write(applications.toString(4)); }

        System.out.println("Dummy JSON files generated!");
    }
}