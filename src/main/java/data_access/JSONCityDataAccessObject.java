package data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.AnswerCity;
import entity.City;
import use_case.start_round.StartRoundDataAccessInterface;
import use_case.submit_guess.SubmitGuessDataAccessInterface;

public class JSONCityDataAccessObject implements StartRoundDataAccessInterface, SubmitGuessDataAccessInterface {
    private final List<AnswerCity> answerCities = new ArrayList<>();
    private final Map<String, City> guessableCities = new HashMap<>();
    private final Random random = new Random();

    public JSONCityDataAccessObject(String answersPath, String guessablePath) {
        JSONArray guessableArray = readJsonArray(guessablePath);
        for (int i = 0; i < guessableArray.length(); i++) {
            JSONObject obj = guessableArray.getJSONObject(i);
            City guessableCity = toCity(obj);

            guessableCities.put(key(guessableCity.getName()), guessableCity);

            JSONArray aliases = obj.optJSONArray("aliases");
            if (aliases != null) {
                for (int j = 0; j < aliases.length(); j++) {
                    String alias = aliases.getString(j);
                    guessableCities.put(key(alias), guessableCity);
                }
            }
        }

        if (guessableCities.isEmpty()) {
            throw new IllegalStateException("No guessable cities in " + guessablePath);
        }

        JSONArray answerArray = readJsonArray(answersPath);
        for (int i = 0; i < answerArray.length(); i++) {
            AnswerCity answerCity = toAnswerCity(answerArray.getJSONObject(i));
            City guessableCity = guessableCities.get(key(answerCity.getName()));

            if (guessableCity == null || !guessableCity.getName().equals(answerCity.getName())) {
                System.err.println("Skipping answer \"" + answerCity.getName()
                        + "\" (" + answerCity.getLandmark() + "): not a guessable city name");
                continue;
            }

            answerCities.add(answerCity);
        }

        if (answerCities.isEmpty()) {
            throw new IllegalStateException("No answer cities in " + answersPath);
        }

    }

    @Override
    public AnswerCity getRandomAnswerCity() {
        return answerCities.get(random.nextInt(answerCities.size()));
    }

    @Override
    public City findCityByName(String name) {
        return guessableCities.get(key(name));
    }

    private static JSONArray readJsonArray(String filePath) {
        try {
            return new JSONArray(Files.readString(Path.of(filePath)));
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + filePath, e);
        }
    }

    private static String key(String name) {
        return name.trim().toLowerCase();
    }

    private static City toCity(JSONObject obj) {
        return new City(
                obj.getString("name"),
                obj.getDouble("lat"),
                obj.getDouble("lon")
        );
    }

    private static AnswerCity toAnswerCity(JSONObject obj) {
        return new AnswerCity(
                obj.getString("name"),
                obj.getString("landmark"),
                obj.getDouble("lat"),
                obj.getDouble("lon")
        );
    }
}
