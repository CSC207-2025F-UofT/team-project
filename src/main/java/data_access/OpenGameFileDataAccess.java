//package opening_game.data_access;
//
//import com.google.gson.Gson;
//import entity.GameState;
//import opening_game.use_case.openGame.OpenGameDataAccessInterface;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class OpenGameFileDataAccess implements OpenGameDataAccessInterface {
//
//    // private static final String SAVE_FILE_PATH = "game_save.json";
//    private final Gson gson = new Gson();
//    private final String saveFilePath;
//
//    public OpenGameFileDataAccess(String saveFilePath) {
//        this.saveFilePath = saveFilePath;
//    }
//
//    @Override
//    public GameState loadGame() {
//        try {
//            File file = new File(saveFilePath);
//            if (!file.exists()) {
//                return null;
//            }
//
//            FileReader reader = new FileReader(file);
//            GameState state = gson.fromJson(reader, GameState.class);
//            reader.close();
//            return state;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;  // return null on error
//        }
//    }
//
//    @Override
//    public void saveGame(GameState state) {
//        try {
//            FileWriter writer = new FileWriter(saveFilePath);
//            gson.toJson(state, writer);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean saveFileExists() {
//        File file = new File(saveFilePath);
//        return file.exists();
//    }
//
//    @Override
//    public void deleteSaveFile() {
//        File file = new File(saveFilePath);
//        if (file.exists()) {
//            file.delete();
//        }
//    }
//}


//package opening_game.data_access;
//
//import entity.GameState;
//import opening_game.use_case.openGame.OpenGameDataAccessInterface;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//public class OpenGameFileDataAccess implements OpenGameDataAccessInterface {
//
//    private final String saveFilePath;
//
//    public OpenGameFileDataAccess(String saveFilePath) {
//        this.saveFilePath = saveFilePath;
//    }
//
//    @Override
//    public GameState loadGame() {
//        try {
//            File file = new File(saveFilePath);
//
//            if (!file.exists()) {
//                return null;
//            }
//
//            // Read file content
//            String content = new String(
//                    Files.readAllBytes(Paths.get(saveFilePath)),
//                    StandardCharsets.UTF_8
//            );
//
//            JSONObject json = new JSONObject(content);
//
//            String currentLocation = json.getString("currentLocation");
//            String finalDestination = json.getString("finalDestination");
//            boolean isCompleted = json.getBoolean("isCompleted");
//
//            GameState state = new GameState(currentLocation, finalDestination);
//
//            // If completed, adjust location
//            if (isCompleted) {
//                state.setCurrentLocation(finalDestination);
//            }
//
//            // Load inventory
//            JSONArray invArr = json.getJSONArray("inventory");
//            for (int i = 0; i < invArr.length(); i++) {
//                state.addItem(invArr.getString(i));
//            }
//
//            return state;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public void saveGame(GameState state) {
//        try {
//            JSONObject json = new JSONObject();
//
//            json.put("currentLocation", state.getCurrentLocation());
//            json.put("finalDestination", state.getFinalDestination());
//            json.put("isCompleted", state.isCompleted());
//            json.put("inventory", new JSONArray(state.getInventory()));
//
//            FileWriter writer = new FileWriter(saveFilePath);
//            writer.write(json.toString(4)); // pretty print
//            writer.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean saveFileExists() {
//        File file = new File(saveFilePath);
//        return file.exists();
//    }
//
//    @Override
//    public void deleteSaveFile() {
//        File file = new File(saveFilePath);
//        if (file.exists()) {
//            file.delete();
//        }
//    }
//}
//


package data_access;

import entity.GameState;
import use_case.openGame.OpenGameDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class OpenGameFileDataAccess implements OpenGameDataAccessInterface {

    private final String saveFilePath;

    public OpenGameFileDataAccess(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    @Override
    public GameState loadGame() {
        try {
            File file = new File(saveFilePath);
            if (!file.exists()) {
                return null;
            }

            // ----- Read file as text -----
            FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
            char[] buffer = new char[(int) file.length()];
            reader.read(buffer);
            reader.close();

            String jsonString = new String(buffer);

            // ----- Convert text → JSON -----
            JSONObject json = new JSONObject(jsonString);

            // ----- Build GameState -----
            String currentLocation = json.getString("currentLocation");
            String finalDestination = json.getString("finalDestination");

            GameState state = new GameState(currentLocation, finalDestination);

            // ----- Load inventory -----
            JSONArray items = json.getJSONArray("inventory");
            for (int i = 0; i < items.length(); i++) {
                state.addItem(items.getString(i));
            }

            // ----- Completed flag -----
            if (json.getBoolean("isCompleted")) {
                state.setCurrentLocation(finalDestination);  // auto marks completed
            }

            return state;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveGame(GameState state) {
        try {
            JSONObject json = new JSONObject();
            json.put("currentLocation", state.getCurrentLocation());
            json.put("finalDestination", state.getFinalDestination());
            json.put("isCompleted", state.isCompleted());
            json.put("inventory", new JSONArray(state.getInventory()));

            FileWriter writer = new FileWriter(saveFilePath, StandardCharsets.UTF_8);
            writer.write(json.toString(4)); // pretty print
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean saveFileExists() {
        return new File(saveFilePath).exists();
    }

    @Override
    public void deleteSaveFile() {
        File file = new File(saveFilePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
