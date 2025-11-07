package entity;

/**
 * An entity representing a player.
 */
public class Player {
    private final String name;
    private final String position;
    private final String club;
    private final float price;
    private final int points;
    private final boolean isSelected;

    /**
     * Creates a new player with the given name, position, club, price, points, and selection status.
     * @param name the player's full name
     * @param position the player's position
     * @param club the player's team/club
     * @param price the player's price (in millions)
     * @param points the player's total fantasy points
     * @param isSelected whether the player is selected in the user's team
     * @throws IllegalArgumentException if name, position, or club are empty, or if price or points are negative
     */
    public Player(String name, String position, String club, float price, int points, boolean isSelected) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Player's name cannot be empty");
        }
        if ("".equals(position)) {
            throw new IllegalArgumentException("Player's position cannot be empty");
        }
        if ("".equals(club)) {
            throw new IllegalArgumentException("Player's club cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Player's price cannot be negative");
        }
        if (points < 0) {
            throw new IllegalArgumentException("Player's fantasy points cannot be negative");
        }

        this.name = name;
        this.position = position;
        this.club = club;
        this.price = price;
        this.points = points;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getClub() {
        return club;
    }

    public float getPrice() {
        return price;
    }

    public int getPoints() {
        return points;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
