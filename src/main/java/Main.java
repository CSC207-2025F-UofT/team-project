import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        JSONObject person = new JSONObject();
        person.put("name", "Ada");
        person.put("age", 36);
        person.put("guitar", "les paul");

        String text = person.toString();      // e.g. {"name":"Ada","age":36}
        // (key order is not guaranteed)
        String pretty = person.toString(2);

        System.out.print(pretty);
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://api.example.com/weather?city=Toronto")
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful()) {
//                String body = response.body().string();  // the response as text
//                System.out.println(body);
//            } else {
//                System.out.println("Request failed with code " + response.code());
//            }
//        } catch (IOException e) {
//            // A network problem (no connection, timeout, ...) — handle it gracefully.
//            e.printStackTrace();
//        }
    }
}
