package helpers;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to convert a city to its corresponding IATA code. For example "Toronto" -> "YYZ"
 */

public class CityCodeConverter {

    private final Map<String, String> translations = new HashMap<>();

    public CityCodeConverter() {
        this("city_codes_to_name.json");
    }

    public CityCodeConverter(String filename) {

        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONObject jsonObject = new JSONObject(jsonString);

            for (String cityCode : jsonObject.keySet()) {
                String city =  jsonObject.getString(cityCode);
                this.translations.put(city, cityCode);
            }

        }

        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    public String getCode(String cityName) {
        return this.translations.get(cityName);
    }
}
