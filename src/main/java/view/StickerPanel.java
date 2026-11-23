package view;

import interface_adapter.search_sticker.SearchStickerController;
import interface_adapter.search_sticker.SearchStickerState;
import interface_adapter.search_sticker.SearchStickerViewModel;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The sticker panel: a search bar and a grid of sticker results.
 */
public class StickerPanel extends JPanel implements PropertyChangeListener {
    private final SearchStickerController controller;
    private final JTextField searchField = new JTextField(20);
    private final JLabel message = new JLabel(" ");
    private final JPanel results = new JPanel(new GridLayout(0, 3, 8, 8));

    public StickerPanel(SearchStickerViewModel viewModel, SearchStickerController controller) {
        this.controller = controller;
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        final JButton searchButton = new JButton("Search");
        final JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar.add(searchField);
        searchBar.add(searchButton);

        final JPanel north = new JPanel(new BorderLayout());
        north.add(searchBar, BorderLayout.NORTH);
        north.add(message, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(results), BorderLayout.CENTER);
        add(new JLabel("Powered by GIPHY"), BorderLayout.SOUTH);

        searchButton.addActionListener(event -> search());
        searchField.addActionListener(event -> search());
    }

    private void search() {
        message.setText("Searching...");
        final String query = searchField.getText();
        // Run off the UI thread so the window doesn't freeze during the HTTP call
        new Thread(() -> controller.execute(query)).start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SearchStickerState state = (SearchStickerState) evt.getNewValue();
        SwingUtilities.invokeLater(() -> showState(state));
    }

    private void showState(SearchStickerState state) {
        results.removeAll();
        if (state.getError() != null) {
            message.setText(state.getError());
        }
        else {
            message.setText(state.getImageUrls().size() + " stickers for \"" + state.getQuery() + "\"");
            for (String url : state.getImageUrls()) {
                try {
                    results.add(new JLabel(new ImageIcon(new URL(url))));
                }
                catch (MalformedURLException ex) {
                    results.add(new JLabel("?"));
                }
            }
        }
        results.revalidate();
        results.repaint();
    }
}