package helpers;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CarrierCodeConverter {

    private final Map<String, String> translations = new HashMap<>();

    public CarrierCodeConverter() {
        this("carrier_to_name(airline).json");
    }

    public CarrierCodeConverter(String filename) {

        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONObject jsonObject = new JSONObject(jsonString);

            for (String carrierCode : jsonObject.keySet()) {
                String airline =  jsonObject.getString(carrierCode);
                this.translations.put(carrierCode, airline);
            }

        }

        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    public String getAirline(String carrierCode) {
        return this.translations.get(carrierCode);
    }
}
