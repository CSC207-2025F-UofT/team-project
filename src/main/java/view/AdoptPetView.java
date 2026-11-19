package view;

import interface_adapter.adopt_pet.AdoptPetState;
import interface_adapter.adopt_pet.AdoptPetViewModel;
import interface_adapter.adopt_pet.AdoptPetViewModelListener;
import interface_adapter.get_pet_picture.GetPetPictureController;
import interface_adapter.adopt_pet.AdoptPetController;

import java.util.concurrent.ExecutionException;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AdoptPetView extends JPanel
        implements AdoptPetViewModelListener {

    private final AdoptPetViewModel viewModel;

    private final JComboBox<String> speciesInput =
            new JComboBox<>(new String[]{"DOG", "CAT"});
    private final JTextField nameInput = new JTextField(15);
    private final JLabel imageLabel =
            new JLabel("Choose a species, then request a picture.");
    private final JLabel errorLabel = new JLabel();

    private final JButton anotherPictureButton =
            new JButton("Generate Picture");
    private final JButton adoptButton = new JButton("Adopt");
    private boolean updatingFields = false;
    private GetPetPictureController getPetPictureController;
    private AdoptPetController adoptPetController;
    private String displayedImageUrl = "";

    public void setGetPetPictureController(
            GetPetPictureController getPetPictureController) {
        this.getPetPictureController = getPetPictureController;
    }

    public void setAdoptPetController(AdoptPetController adoptPetController) {
        this.adoptPetController = adoptPetController;
    }

    private void requestAnotherPicture() {
        AdoptPetState state = viewModel.getState();

        if (state.isLoading()) {
            return;
        }

        final String species = state.getSpecies();

        state.setLoading(true);
        state.setImageUrl("");
        state.setErrorMessage("");
        viewModel.notifyStateChanged();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                getPetPictureController.execute(species);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showRequestError("Picture request was interrupted.");
                } catch (ExecutionException e) {
                    showRequestError(
                            "Could not request a picture. Please try again."
                    );
                }
            }
        }.execute();
    }

    private void showRequestError(String message) {
        AdoptPetState state = viewModel.getState();
        state.setLoading(false);
        state.setImageUrl("");
        state.setErrorMessage(message);
        viewModel.notifyStateChanged();
    }
    public AdoptPetView(AdoptPetViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Adopt a Pet");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel speciesPanel = new JPanel();
        speciesPanel.add(new JLabel("Species:"));
        speciesPanel.add(speciesInput);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(350, 250));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Pet name:"));
        namePanel.add(nameInput);

        JPanel buttons = new JPanel();
        buttons.add(anotherPictureButton);
        buttons.add(adoptButton);

        add(title);
        add(speciesPanel);
        add(imagePanel);
        add(namePanel);
        add(errorLabel);
        add(buttons);


        speciesInput.addActionListener(evt -> {
            if (!updatingFields) {
                AdoptPetState state = viewModel.getState();
                state.setSpecies((String) speciesInput.getSelectedItem());

                // A picture of the previous species is no longer valid.
                state.setImageUrl("");
                state.setErrorMessage("");

                viewModel.notifyStateChanged();
            }
        });

        nameInput.getDocument().addDocumentListener(new DocumentListener() {

            private void updateName() {
                if (!updatingFields) {
                    viewModel.getState().setPetName(nameInput.getText());
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateName();
            }
        });

        anotherPictureButton.addActionListener(
                evt -> requestAnotherPicture()
        );

        adoptButton.addActionListener(evt -> {
            AdoptPetState state = viewModel.getState();

            adoptPetController.execute(
                    state.getUsername(),
                    state.getSpecies(),
                    state.getPetName(),
                    state.getImageUrl()
            );
        });

        stateChanged();
    }

    private void loadPicture(String imageUrl) {
        imageLabel.setIcon(null);
        imageLabel.setText("Downloading picture...");
        adoptButton.setEnabled(false);

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
                // Ignore an old download if the user has changed the picture.
                if (!imageUrl.equals(displayedImageUrl)) {
                    return;
                }

                try {
                    ImageIcon icon = get();
                    imageLabel.setIcon(icon);
                    imageLabel.setText("");
                    adoptButton.setEnabled(true);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showRequestError("Picture download was interrupted.");
                } catch (ExecutionException e) {
                    showRequestError(
                            "Could not display this picture. Try another one."
                    );
                }
            }
        }.execute();
    }



    @Override
    public void stateChanged() {
        AdoptPetState state = viewModel.getState();

        updatingFields = true;
        try {
            speciesInput.setSelectedItem(state.getSpecies());

            if (!nameInput.getText().equals(state.getPetName())) {
                nameInput.setText(state.getPetName());
            }
        } finally {
            updatingFields = false;
        }
        errorLabel.setText(state.getErrorMessage());

        speciesInput.setEnabled(!state.isLoading());
        anotherPictureButton.setEnabled(!state.isLoading());

        adoptButton.setEnabled(
                !state.isLoading() && !state.getImageUrl().isEmpty()
        );

        String imageUrl = state.getImageUrl();

        if (state.isLoading()) {
            displayedImageUrl = "";
            imageLabel.setIcon(null);
            imageLabel.setText("Loading picture...");
        } else if (imageUrl.isEmpty()) {
            displayedImageUrl = "";
            imageLabel.setIcon(null);
            imageLabel.setText("Choose a species, then request a picture.");
        } else if (!imageUrl.equals(displayedImageUrl)) {
            displayedImageUrl = imageUrl;
            loadPicture(imageUrl);
        }
    }
}