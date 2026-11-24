package data_access;

import entity.Watchlist;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.watchlist.WatchListDataAccessInterface;

import java.io.IOException;

public class AccoutWatchListDataAccessObject implements WatchListDataAccessInterface {

    private static final String BASE_URL = "https://csc207-20269-vm.teach.cs.toronto.edu";

    private static final String INFO_FIELD = "info";
    private static final String WATCHLIST_KEY = "watchlist";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public Watchlist get(String userName){
        JSONObject user = getUser(userName);
        JSONObject info = user.optJSONObject(INFO_FIELD);

        Watchlist watchList = new Watchlist();
        if(info !=null && info.optJSONArray(WATCHLIST_KEY) != null){
            JSONArray stockWatchers = info.getJSONArray(WATCHLIST_KEY);
            for (int i = 0; i< stockWatchers.length(); i++){
                watchList.addStock(stockWatchers.getString(i));
            }
        }

        return watchList;
    }

    @Override
    public void save(String userName, Watchlist watchList){
        JSONObject user = getUser(userName);
        JSONObject info = user.optJSONObject(INFO_FIELD);

        if (info == null){
            info = new JSONObject();
        }

        info.put(WATCHLIST_KEY, new JSONArray(watchList.getStocks()));

        JSONObject body = new JSONObject();
        body.put("username", user.getString("username"));
        body.put("password", user.getString("password"));
        body.put(INFO_FIELD, info);

        Request request = new Request.Builder()
                .url(BASE_URL + "/user")
                .put(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .build();

        JSONObject result = executeRequest(request);

        if (result.getInt("status_code") != 200){
            throw new RuntimeException(result.optString("message", "Could not save watchlist"));
        }
    }

    private JSONObject getUser(String username){
        HttpUrl url = HttpUrl.parse(BASE_URL + "/user")
                .newBuilder()
                .addQueryParameter("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        JSONObject result = executeRequest(request);

        if (result.getInt("status_code") != 200){
            throw new RuntimeException(result.optString("message", "Could not find account"));
        }

        return result.getJSONObject("user");
    }

    private JSONObject executeRequest(Request request){
        try(Response response = client.newCall(request).execute()){
            if(response.body() == null){
                throw new RuntimeException("No API response");
            }
            return new JSONObject(response.body().string());
        }
        catch (IOException error){
            throw new RuntimeException("Could not connect to User API", error);
        }
    }
}
