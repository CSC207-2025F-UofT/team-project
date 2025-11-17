package entity;

import java.util.ArrayList;

public class User implements FavoriteList, Watchlist {
    private String username;
    private String password;
    private final int accountID;
    private final ArrayList<Media> favorites;
    private final ArrayList<Media> watchlist;

    public User(String username, String password, int accountID) {
        this.username = username;
        this.password = password;
        this.accountID = accountID;
        this.favorites = new ArrayList<>();
        this.watchlist =new ArrayList<>();
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public int getAccountID() {return accountID;}
    public ArrayList<Media> getFavorites() {return favorites;}
    public ArrayList<Media> getWatchlist() {return watchlist;}

    @Override
    public void addFavorite(Media media) {
        favorites.add(media);
    }

    @Override
    public void removeFavorite(Media media) {
        favorites.remove(media);
    }

    @Override
    public void addWatchlist(Media media) {
        watchlist.add(media);
    }

    @Override
    public void removeWatchList(Media media) {
        watchlist.remove(media);
    }
}
