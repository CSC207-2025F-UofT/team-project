import javax.swing.*;

public class AdvancedPanel extends JPanel {

    public JTextField titleText;
    public JTextField genreText;
    public JTextField directorText;
    public JButton advancedBackButton;

    public AdvancedPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel();
        titleText = new JTextField(10);
        titlePanel.add(new JLabel("Title:"));
        titlePanel.add(titleText);

        JPanel genrePanel = new JPanel();
        genreText = new JTextField(10);
        genrePanel.add(new JLabel("Genre:"));
        genrePanel.add(genreText);

        JPanel directorPanel = new JPanel();
        directorText = new JTextField(10);
        directorPanel.add(new JLabel("Director:"));
        directorPanel.add(directorText);

        advancedBackButton = new JButton("Back");

        add(titlePanel);
        add(genrePanel);
        add(directorPanel);
        add(advancedBackButton);
    }
}
