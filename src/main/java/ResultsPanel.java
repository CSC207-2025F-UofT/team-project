import javax.swing.*;

public class ResultsPanel extends JPanel {

    public JList<Movie> movieList;
    public JButton resultsBackButton;

    public ResultsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        movieList = new JList<>();
        add(new JLabel("Search Results"));
        add(new JScrollPane(movieList));

        resultsBackButton = new JButton("Back");
        add(resultsBackButton);
    }
}
