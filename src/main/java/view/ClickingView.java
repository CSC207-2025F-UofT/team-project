package view;

import interface_adapter.clicking.ClickingController;
import interface_adapter.clicking.ClickingViewModel;

import javax.swing.*;
import java.awt.*;

public class ClickingView extends JPanel {
    private final ClickingController controller = null;
    private final ClickingViewModel  viewModel;

    private JLabel errorLabel = new JLabel();
    private JLabel languageLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JLabel posterLabel = new JLabel();
    private JTextArea overviewText = new JTextArea();
    private JLabel yearLabel = new JLabel();
    private JLabel ratingLabel = new JLabel();
    private JLabel genresLabel = new JLabel();

    public ClickingView() {
        this.controller = controller;
        this.viewModel=  viewModel;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(errorLabel, BorderLayout.NORTH);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1,2));
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.add(posterLabel, BorderLayout.CENTER);
        centerPanel.add(posterPanel);

        overviewText.setLineWrap(true);
        overviewText.setWrapStyleWord(true);
        overviewText.setEditable(false);
        overviewText.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(overviewText);
        centerPanel.add(scroll);
        add(centerPanel, BorderLayout.CENTER);

        JPanel detailPanel = new JPanel(new GridLayout(4, 1));
        detailPanel.add(yearLabel);
        detailPanel.add(ratingLabel);
        detailPanel.add(languageLabel);
        detailPanel.add(genresLabel);

        add(detailPanel, BorderLayout.SOUTH);
    }
    public void updateView() {

        errorLabel.setText(viewModel.errorMessage);

        if (viewModel.errorMessage != null && !viewModel.errorMessage.isEmpty()) {
            titleLabel.setText("");
            yearLabel.setText("");
            ratingLabel.setText("");
            languageLabel.setText("");
            genresLabel.setText("");
            overviewText.setText("");
            posterLabel.setIcon(null);
            return;
        }
        titleLabel.setText(viewModel.title);

        yearLabel.setText("Year: " + viewModel.releaseYear);
        ratingLabel.setText("Rating: " + viewModel.rating);
        languageLabel.setText("Language: " + viewModel.language);
        genresLabel.setText("Genres: " + viewModel.genres);

        overviewText.setText(viewModel.overview);

        ImageIcon icon = new ImageIcon(viewModel.posterUrl);
        posterLabel.setIcon(icon);
    }

    public String getViewName() {
        return viewModel.viewName;
    }
}







