package entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class User implements FavoriteList, Watchlist {
    private String username;
    private String password;
    private final int accountID;
    private final Media[] favorites;
    private final Media[] watchlist;
    public List<Comment> comments= new ArrayList<>();

    public User(String username, String password, int accountID, Media[] favorites, Media[] watchlist) {
        this.username = username;
        this.password = password;
        this.accountID = accountID;
        this.favorites = favorites;
        this.watchlist = watchlist;
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public int getAccountID() {return accountID;}
    public Media[] getFavorites() {return favorites;}
    public Media[] getWatchlist() {return watchlist;}
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public void addFavorite(Media media) {
        // TODO document why this method is empty
    }

    @Override
    public void removeFavorite(Media media) {
        // TODO document why this method is empty

    }

    @Override
    public void addWatchlist(Media media) {
        // TODO document why this method is empty

    }

    @Override
    public void removeWatchList(Media media) {
        // TODO document why this method is empty

    }
}
