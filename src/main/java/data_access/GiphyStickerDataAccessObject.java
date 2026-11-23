package data_access;

import entity.Sticker;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.search_sticker.SearchStickerDataAccessInterface;
import use_case.search_sticker.StickerSearchException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches stickers with the GIPHY API and converts the JSON into Sticker entities.
 * This is the only class that knows about GIPHY.
 */
public class GiphyStickerDataAccessObject implements SearchStickerDataAccessInterface {
    private static final String SEARCH_URL = "https://api.giphy.com/v1/stickers/search";
    private static final int LIMIT = 12;

    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public GiphyStickerDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Sticker> searchStickers(String query) throws StickerSearchException {
        if (apiKey == null || apiKey.isBlank()) {
            throw new StickerSearchException("GIPHY_API_KEY is not set");
        }
        HttpUrl url = HttpUrl.parse(SEARCH_URL).newBuilder()
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("q", query)
                .addQueryParameter("limit", String.valueOf(LIMIT))
                .addQueryParameter("rating", "g")
                .build();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new StickerSearchException("GIPHY returned " + response.code());
            }
            JSONObject json = new JSONObject(response.body().string());
            return parseStickers(json.getJSONArray("data"));
        }
        catch (IOException | JSONException ex) {
            throw new StickerSearchException("Could not reach GIPHY: " + ex.getMessage());
        }
    }

    /** Adapts GIPHY's JSON format into our Sticker entity. */
    private List<Sticker> parseStickers(JSONArray data) {
        List<Sticker> stickers = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            String url = item.getJSONObject("images")
                    .getJSONObject("fixed_width")
                    .getString("url");
            stickers.add(new Sticker(item.getString("id"), item.optString("title", ""), url));
        }
        return stickers;
    }
}