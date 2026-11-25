package user_case.Search;


import entity.Movie;

import java.util.List;

/**
 * Output Data for the Search Use Case.
 */
public class SearchOutputData {


    private final String[][] outputList;

    //results returned in the form of {name, description, poster path}
    //TODO: make this into a javadoc
    public SearchOutputData(List<Movie> movies) {
        outputList = new String[movies.size()][];
        int index = 0;
        for(Movie movie: movies){
            outputList[index] = new String[] {movie.getName(), movie.getReleaseDate(), movie.getPosterPath(), String.valueOf(movie.getMovieID())};
            index++;
        }
    }


    public String[][] getOutputList(){return outputList;}

//    public String getName() {
//        return name;
//    }
//
//    public String getReleaseDate() {
//        return releaseDate;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public int getRating() {
//        return rating;
//    }
}

