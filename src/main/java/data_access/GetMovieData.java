package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.FileWriter;
import java.io.IOException;

public class GetMovieData {

    public static void main(String[] args) {

        int movieId = 550; // Example: Fight Club
//        String token = System.getenv("eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMzA4YmFhNzE3MDgyZWFkNTQ4ODBkZWMxZmNlYTYwNiIsIm5iZiI6MTc5MTc0NDA0NS4wODgsInN1YiI6IjZhY2JkODJkNmVkZTFkZDZjZTE0ZTIzMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yJV30ApjkLuoz6cUk2JKP8F7h6MrR5MRp9lpMK_ELOs\n" +
//                "\n");

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMzA4YmFhNzE3MDgyZWFkNTQ4ODBkZWMxZmNlYTYwNiIsIm5iZiI6MTc5MTc0NDA0NS4wODgsInN1YiI6IjZhY2JkODJkNmVkZTFkZDZjZTE0ZTIzMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yJV30ApjkLuoz6cUk2JKP8F7h6MrR5MRp9lpMK_ELOs";

        OkHttpClient client = new OkHttpClient();

//        Request request = new Request.Builder()
//                .url("https://api.themoviedb.org/3/movie/11?language=en-US")
//                .get()
//                .addHeader("accept", "application/json")
//                .addHeader("Authorization", "Bearer " + token)
//                .build();
//
//        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/search/movie?query=Avatar&include_adult=false&language=en-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();


        try (Response response =
                     client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException(
                        "API request failed: " + response.code()
                );
            }

            if (response.body() == null) {
                throw new IOException("Empty response body");
            }

            // Step 1: Get the JSON response
            String jsonResponse = response.body().string();

            // Step 2: Save JSON into a file
            try (FileWriter file =
                         new FileWriter("movie.json")) {

                file.write(jsonResponse);
            }

            System.out.println("Movie JSON saved successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
