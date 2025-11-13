package data_access;

import entity.Sportbet;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SportsAPIDataAccess {
    private OkHttpClient client = new OkHttpClient();
    private String apiKey = "0c5ba9bf08780b2ed18e605b84f07565";

    public void fetchOdds() {
        String sport = "basketball_nba";
        String url = "https://api.the-odds-api.com/v4/sports/" + sport +
                "/odds?regions=us&markets=h2h&apiKey=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Request failed: " + response.code());
                return;
            }

            String jsonData = response.body().string();

            try (FileWriter writer = new FileWriter("odds.txt")) {
                writer.write(jsonData);
            }

            System.out.println("Data saved to odds.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readdata(){
        ArrayList<Sportbet> possibleBets = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("odds.txt"));
            String line;
            while ((line = br.readLine()) != null){
                String[] splits = line.split("id");
                splits = Arrays.copyOfRange(splits,1,splits.length);
                for(String i: splits){
                    if(i.substring(i.indexOf("bookmakers")).length()>20){
                        System.out.println(i);
                        String id = i.substring(3,35);
                        String sport = i.substring(i.indexOf("sport_title")+14,i.indexOf("commence")-3);
                        String teamodds = i.substring(i.indexOf("outcomes")+10,i.length()-8);
                        String[] teams = teamodds.split("},\\{");
                        for (String a:teams){
                            System.out.println(a);
                        }
                        //System.out.println(id);
                        System.out.println(sport);
                        //System.out.println("ok");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
