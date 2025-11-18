package data_access;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.Comment;
import entity.Media;
import entity.Movie;
import entity.User;
import use_case.rate_and_comment.CommentUserDataAccessInterface;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUserDataAccessObject implements CommentUserDataAccessInterface {

    private final String filePath = "users.json";
    private final Gson gson = new Gson();
    private final Type mapType = new TypeToken<Map<String, User>>(){}.getType();

    private Map<String, User> readFile() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return new HashMap<>();
            }
            FileReader reader = new FileReader(file);
            Map<String, User> map = gson.fromJson(reader, mapType);
            reader.close();
            return map == null ? new HashMap<>() : map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void writeFile(Map<String, User> map) {
        try {
            FileWriter writer = new FileWriter(filePath);
            gson.toJson(map, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void createUser(String username, String password, int accountID) {
        Map<String, User> map = readFile();
        if (!map.containsKey(username)) {
            map.put(username, new User(username, password, accountID));
            writeFile(map);
        }
    }

    //@Override
    public User getUser(String username) {
        return readFile().get(username);
    }

    //@Override
    public void addToWatchlist(String username, Media movie) {
        Map<String, User> map = readFile();
        map.get(username).addWatchlist(movie);
        writeFile(map);
    }

    //@Override
    public void deleteFromWatchlist(String username, Media movie) {
        Map<String, User> map = readFile();
        map.get(username).removeWatchList(movie);
        writeFile(map);
    }

    //@Override
    public void addToFavoritelist(String username, Media movie) {
        Map<String, User> map = readFile();
        map.get(username).addFavorite(movie);
        writeFile(map);
    }

    //@Override
    public void deleteFromFavoritelist(String username, Media movie) {
        Map<String, User> map = readFile();
        map.get(username).removeFavorite(movie);
        writeFile(map);
    }

    //@Override
    public List<Media> getWatchlist(String username) {
        return readFile().get(username).getWatchlist();
    }

    //@Override
    public List<Media> getFavoritelist(String username) {
        return readFile().get(username).getFavorites();
    }

    @Override
    public void addComment(String username, Comment comment) {
        Map<String, User> map = readFile();
        map.get(username).addcomment(comment);
        writeFile(map);
    }

    //@Override
    public List<Comment> getComments(String username) {
        return readFile().get(username).getComments();
    }

    //@Override
    public String getPassword(String username) {
        return readFile().get(username).getPassword();
    }

    //@Override
    public void changePassword(String username, String newPassword) {
        Map<String, User> map = readFile();
        map.get(username).setPassword(newPassword);
        writeFile(map);
    }
}

