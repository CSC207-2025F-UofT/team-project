package entity;

import java.time.LocalDate;
import java.util.List;

public class Event {
    final String id;
    final String name;
    final List<String> artists;
    final String venue;
    final String city;
    final String country;
    final LocalDate date;
    final int priceMin;
    final int priceMax;
    final String ticketUrl;
    final List<String> genres;

    public Event(String id, String name, List<String> artists, String venue, String city, String country, LocalDate date, int priceMin, int priceMax, String ticketUrl, List<String> genres) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.venue = venue;
        this.city = city;
        this.country = country;
        this.date = date;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.ticketUrl = ticketUrl;
        this.genres = genres;
    }
    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<String> getArtists() {
        return artists;
    }
    public String getVenue() {
        return venue;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getPriceMin() {
        return priceMin;
    }
    public int getPriceMax() {
        return priceMax;
    }
    public String getTicketUrl() {
        return ticketUrl;
    }
    public List<String> getGenres() {
        return genres;
    }

}
