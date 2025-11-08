import javax.swing.*;
import java.awt.*;

public class Main {


    public static void main(String[]args){
        final String CARD_SEARCH = "SEARCH";
        final String CARD_RESULTS = "RESULTS";
        final String CARD_FAVORITES = "FAVORITES";
        final String CARD_ADVANCED = "ADVANCED";
        JList<String> favorites = new JList<>(new String[]{
                "Movie 1", "Movie 2", "Movie 3"
        });

        JTextArea resultsArea = new JTextArea(6,24);

        SwingUtilities.invokeLater(()->{
            JFrame frame = new JFrame("Movie Finder");
            frame.setMinimumSize(new java.awt.Dimension(300,200));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel cardPanel = new JPanel(new CardLayout());

            JPanel searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

            JPanel firstSearchPanel = new JPanel();
            firstSearchPanel.add(new JLabel("Enter Keyword"));
            JTextField searchField = new JTextField(10);
            searchField.setMaximumSize(searchField.getPreferredSize());
            firstSearchPanel.add(searchField);

            JPanel secondSearchPanel = new JPanel();
            JButton favoriteButton = new JButton("favorite list");
            JButton advancedButton = new JButton("Advanced Search");
            secondSearchPanel.add(favoriteButton);
            secondSearchPanel.add(advancedButton);

            searchPanel.add(firstSearchPanel);
            searchPanel.add(secondSearchPanel);

            JPanel resultsPanel = new JPanel();
            resultsPanel.setLayout(new BoxLayout(resultsPanel,BoxLayout.Y_AXIS));
            JButton resultsBackButton = new JButton("Back");
            resultsPanel.add(new JLabel("Search Results"));
            resultsPanel.add(new JScrollPane(resultsArea));
            resultsPanel.add(resultsBackButton);

            JPanel favoritesPanel = new JPanel();
            favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
            favoritesPanel.add(new JLabel("Your Favorite Movies:"));
            favoritesPanel.add(favorites);
            JButton favoritesBackButton = new JButton("Back");
            favoritesPanel.add(favoritesBackButton);

            JPanel advancedPanel = new JPanel();
            advancedPanel.setLayout(new BoxLayout(advancedPanel, BoxLayout.Y_AXIS));

            JPanel titlePanel = new JPanel();
            JTextField titleText = new JTextField(10);
            titlePanel.add(new JLabel("Title:"));
            titlePanel.add(titleText);

            JPanel genrePanel = new JPanel();
            JTextField genreText = new JTextField(10);
            genrePanel.add(new JLabel("Genre:"));
            genrePanel.add(genreText);

            JPanel directorPanel = new JPanel();
            JTextField directorText = new JTextField(10);
            directorPanel.add(new JLabel("Director:"));
            directorPanel.add(directorText);

            JButton advancedBackButton = new JButton("Back");

            advancedPanel.add(titlePanel);
            advancedPanel.add(genrePanel);
            advancedPanel.add(directorPanel);
            advancedPanel.add(advancedBackButton);


            cardPanel.add(searchPanel, CARD_SEARCH);
            cardPanel.add(favoritesPanel, CARD_FAVORITES);
            cardPanel.add(advancedPanel, CARD_ADVANCED);
            cardPanel.add(resultsPanel, CARD_RESULTS);


            frame.setContentPane(cardPanel);
            frame.setVisible(true);

            CardLayout cl = (CardLayout)(cardPanel.getLayout());

            searchField.addActionListener(e-> { String search = searchField.getText().trim();
            if (search.isEmpty()){
                JOptionPane.showMessageDialog(frame, "Please enter a keyword.");
                return;
            }

            resultsArea.setText("Showing results for: "+search+":");
            cl.show(cardPanel,CARD_RESULTS);
            });

            resultsBackButton.addActionListener(e->cl.show(cardPanel,CARD_SEARCH));
            favoritesBackButton.addActionListener(e->cl.show(cardPanel,CARD_SEARCH));
            advancedBackButton.addActionListener(e->cl.show(cardPanel, CARD_SEARCH));

            favoriteButton.addActionListener(e -> cl.show(cardPanel, CARD_FAVORITES));
            advancedButton.addActionListener(e-> cl.show(cardPanel, CARD_ADVANCED));

            frame.pack();

        });

        }
}