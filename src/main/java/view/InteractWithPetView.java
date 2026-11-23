package view;

import interface_adapter.interact_with_pet.InteractWithPetState;
import interface_adapter.interact_with_pet.InteractWithPetViewModel;
import interface_adapter.interact_with_pet.InteractWithPetViewModelListener;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class InteractWithPetView extends JPanel
        implements InteractWithPetViewModelListener {

    private final InteractWithPetViewModel viewModel;

    private final JLabel nameLabel = new JLabel();
    private final JLabel speciesLabel = new JLabel();
    private final JLabel imageLabel =
            new JLabel("Pet picture will appear here.");
    private String displayedImageUrl = "";

    public InteractWithPetView(InteractWithPetViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Interact with Your Pet");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        speciesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(350, 250));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        add(title);
        add(nameLabel);
        add(speciesLabel);
        add(imagePanel);

        stateChanged();
    }

    private void loadPicture(String imageUrl) {
        imageLabel.setIcon(null);
        imageLabel.setText("Loading your pet...");

        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws IOException {
                URLConnection connection = new URL(imageUrl).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);

                final BufferedImage image;

                try (InputStream input = connection.getInputStream()) {
                    image = ImageIO.read(input);
                }

                if (image == null) {
                    throw new IOException("Unsupported image format.");
                }

                double scale = Math.min(
                        320.0 / image.getWidth(),
                        220.0 / image.getHeight()
                );

                int width = Math.max(1, (int) (image.getWidth() * scale));
                int height = Math.max(1, (int) (image.getHeight() * scale));

                BufferedImage resized = new BufferedImage(
                        width, height, BufferedImage.TYPE_INT_ARGB
                );

                Graphics2D graphics = resized.createGraphics();

                try {
                    graphics.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR
                    );
                    graphics.drawImage(image, 0, 0, width, height, null);
                } finally {
                    graphics.dispose();
                }

                return new ImageIcon(resized);
            }

            @Override
            protected void done() {
                if (!imageUrl.equals(displayedImageUrl)) {
                    return;
                }

                try {
                    imageLabel.setIcon(get());
                    imageLabel.setText("");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    displayedImageUrl = "";
                    imageLabel.setText("Picture loading was interrupted.");
                } catch (ExecutionException e) {
                    displayedImageUrl = "";
                    imageLabel.setText("Could not load your pet's picture.");
                }
            }
        }.execute();
    }

    @Override
    public void stateChanged() {
        InteractWithPetState state = viewModel.getState();

        nameLabel.setText("Name: " + state.getPetName());
        speciesLabel.setText("Species: " + state.getSpecies());

        String imageUrl = state.getImageUrl();

        if (imageUrl == null || imageUrl.isEmpty()) {
            displayedImageUrl = "";
            imageLabel.setIcon(null);
            imageLabel.setText("No pet picture.");
        } else if (!imageUrl.equals(displayedImageUrl)) {
            displayedImageUrl = imageUrl;
            loadPicture(imageUrl);
        }
    }
}