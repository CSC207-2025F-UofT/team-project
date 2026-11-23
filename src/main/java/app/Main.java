package app;
import data_access.GetMovieData;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        //Read TMDB token from Intellij
        String token = System.getenv("TMDB_ACCESS_TOKEN");
        //Create the shared API client
        GetMovieData apiClient = new GetMovieData(token);

        try{
            String result = apiClient.get("search/movie?query=Interstellar");
            System.out.println(result);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
