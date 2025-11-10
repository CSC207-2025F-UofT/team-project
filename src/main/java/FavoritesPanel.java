import javax.swing.*;

public class FavoritesPanel extends JPanel {

    public JList<String> favorites;
    public JButton favoritesBackButton;

    public FavoritesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        favorites = new JList<>(new String[]{
                "Movie 1", "Movie 2", "Movie 3"
        });

        add(new JLabel("Your Favorite Movies:"));
        add(favorites);

        favoritesBackButton = new JButton("Back");
        add(favoritesBackButton);
    }
}
