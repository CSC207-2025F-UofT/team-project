package data_access;

import entity.Movie;
import user_case.MovieDetail.MovieDetailDataAccessInterface;


public class MovieDetailDataAccessObject implements MovieDetailDataAccessInterface {

    private final GetMovieData getMovieData;

    public MovieDetailDataAccessObject(GetMovieData getMovieData) {
        this.getMovieData = getMovieData;
    }

    @Override
    public Movie getMovieById(int movieID) {
        try {
            String rawJson = getMovieData.get("movie/" + movieID);
            if (rawJson == null || rawJson.isBlank()) {
                return null;
            }

            String title = extractJsonString(rawJson, "title");
            if (title.isBlank()) {
                title = extractJsonString(rawJson, "original_title");
            }
            if (title.isBlank()) {
                title = "Movie #" + movieID;
            }

            int rating = (int) Math.round(extractJsonDouble(rawJson, "vote_average"));
            String description = extractJsonString(rawJson, "overview");
            String releaseDate = extractJsonString(rawJson, "release_date");
            String posterPath = extractJsonString(rawJson, "poster_path");

            return new Movie(
                    title,
                    rating,
                    description,
                    releaseDate,
                    movieID,
                    posterPath
            );
        } catch (Exception e) {
            return null;
        }
    }

    private String extractJsonString(String json, String key) {
        String pattern = "\"" + key + "\"";
        int keyIndex = json.indexOf(pattern);
        if (keyIndex == -1) {
            return "";
        }
        int colonIndex = json.indexOf(':', keyIndex + pattern.length());
        if (colonIndex == -1) {
            return "";
        }

        int i = colonIndex + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
            i++;
        }
        if (i >= json.length()) {
            return "";
        }
        if (json.startsWith("null", i)) {
            return "";
        }
        if (json.charAt(i) != '"') {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        i++;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);
                if (next == '"') {
                    sb.append('"');
                    i += 2;
                } else if (next == '\\') {
                    sb.append('\\');
                    i += 2;
                } else if (next == 'n') {
                    sb.append('\n');
                    i += 2;
                } else if (next == 'r') {
                    sb.append('\r');
                    i += 2;
                } else if (next == 't') {
                    sb.append('\t');
                    i += 2;
                } else {
                    sb.append(next);
                    i += 2;
                }
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private double extractJsonDouble(String json, String key) {
        String pattern = "\"" + key + "\"";
        int keyIndex = json.indexOf(pattern);
        if (keyIndex == -1) {
            return 0.0;
        }
        int colonIndex = json.indexOf(':', keyIndex + pattern.length());
        if (colonIndex == -1) {
            return 0.0;
        }

        int i = colonIndex + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
            i++;
        }
        if (i >= json.length() || json.startsWith("null", i)) {
            return 0.0;
        }

        int start = i;
        while (i < json.length()) {
            char c = json.charAt(i);
            if ((c >= '0' && c <= '9') || c == '-' || c == '+' || c == '.' || c == 'e' || c == 'E') {
                i++;
            } else {
                break;
            }
        }
        try {
            return Double.parseDouble(json.substring(start, i));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}