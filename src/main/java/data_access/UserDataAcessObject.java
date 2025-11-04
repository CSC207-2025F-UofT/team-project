package data_access;

import entity.User;
import entity.Game;
import java.util.*;
import java.io.IOException;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

// NOTE: All Steamapi calls are of the format: 
// https://api.steampowered.com/<Interface>/<Method>/v<Version>/?key=<apikey>&<args>

// This is the DAO for a logged in user. (Steamid -> User object)
// TODO: fill this out after getting an apikey and some experimenting with api returns.
public class UserDataAcessObject {
    private static final String apikey = System.getenv("APIKEY");

    //Override this later in accordance with furture interfaces.
    public User get(long steamid) {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request friendRequest = new Request.Builder()
            .url(String.format("https://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=%s&steamid=%l&relationship=friend", apikey, steamid))
            .build();

        try {
            final Response response = client.newCall(friendRequest).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

        } catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }

        return new User(0, "", new ArrayList<User>(), new ArrayList<Game>());
    }
}
