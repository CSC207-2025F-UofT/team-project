package view;

import interface_adapter.browse.BrowseViewModel;

import javax.swing.*;
import java.awt.*;

public class BrowseView extends JPanel {

    private JTextField searchField;
    private JButton searchButton;
    private JComboBox searchTypeSelect;
    private final BrowseViewModel viewModel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("browse View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new BrowseView(new BrowseViewModel()));

        frame.pack();
        frame.setSize(400,400);

        frame.setVisible(true);
    }

    public BrowseView(BrowseViewModel viewModel) {
        this.viewModel = viewModel;
        createUIComponents();
    }

    private void createUIComponents() {

        this.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

        searchField = new JTextField();
        searchButton = new JButton("Search");
        searchTypeSelect = new JComboBox();
        searchTypeSelect.setModel(new DefaultComboBoxModel(new String[] {"Movie", "Actor"}));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(searchTypeSelect);


        JPanel mediaDisplayPanel = new JPanel();
        mediaDisplayPanel.setLayout(new GridLayout(0, 4, 10, 10));

        for (int i = 0; i < 20; i++) { // Example: 20 movies
            mediaDisplayPanel.add(createMovieItemPanel(
                    "Movie Name " + i,
                    "8.5",
                    "120 min",
                    "Action, Sci-Fi"
            ));
        }

        JScrollPane scrollPane = new JScrollPane(mediaDisplayPanel);


        this.add(searchPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

    }


    public JPanel createMovieItemPanel(String movieName, String rating, String runtime, String genres) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));

        JLabel imagePlaceholder = new JLabel();
        imagePlaceholder.setPreferredSize(new Dimension(150, 200));
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePlaceholder.setText("Image Placeholder");

        JLabel nameLabel = new JLabel(movieName);
        JLabel ratingLabel = new JLabel("Rating: " + rating + " - Runtime: " + runtime);
        JLabel genresLabel = new JLabel("Genres: " + genres);

        itemPanel.add(imagePlaceholder);
        itemPanel.add(nameLabel);
        itemPanel.add(ratingLabel);
        itemPanel.add(genresLabel);

        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return itemPanel;
    }
}
