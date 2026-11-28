package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import interface_adapter.ExternalListingViewData;
import interface_adapter.SwitchViewController;
import interface_adapter.view_pet_details.PetDetailState;
import interface_adapter.view_pet_details.PetDetailViewModel;
import interface_adapter.view_pet_details.PetDetailViewModelListener;

/**
 * The page for one cat: name, summary, photo placeholder, the full profile, and a Back button.
 */
public class PetDetailView extends JPanel implements PetDetailViewModelListener {
    private static final int GAP = 12;

    private final PetDetailViewModel viewModel;
    private final JLabel nameLabel = new JLabel();
    private final JLabel summaryLabel = new JLabel();
    private final JLabel photoLabel = new JLabel("No photo yet", SwingConstants.CENTER);
    private final JPanel linesPanel = new JPanel();
    private final JLabel errorLabel = new JLabel();
    private final JButton applyButton = new JButton("Apply to adopt");
    private final JPanel compatibilityHolder = new JPanel(new BorderLayout());
    private BiConsumer<String, String> applyHandler;
    private Consumer<String> petShownHandler;
    private Function<String, BufferedImage> externalPhotoLoader = url -> null;

    /**
     * Creates the detail page.
     *
     * @param viewModel the model whose state this page draws
     * @param switchViewController used by the Back button
     * @param backViewName the name of the screen the Back button returns to
     */
    public PetDetailView(PetDetailViewModel viewModel, SwitchViewController switchViewController,
                         String backViewName) {
        super(new BorderLayout());
        this.viewModel = viewModel;
        viewModel.addListener(this);
        getAccessibleContext().setAccessibleName("Pet details");

        final JButton backButton = new JButton("Back to gallery");
        backButton.setMnemonic(KeyEvent.VK_B);
        backButton.getAccessibleContext().setAccessibleName("Back to gallery");
        backButton.addActionListener(event -> switchViewController.switchTo(backViewName));
        final JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, 0, GAP));
        top.add(backButton, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);

        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));

        nameLabel.putClientProperty("html.disable", true);
        summaryLabel.putClientProperty("html.disable", true);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 24f));
        nameLabel.getAccessibleContext().setAccessibleName("Cat name");
        summaryLabel.getAccessibleContext().setAccessibleName("Cat summary");
        photoLabel.setPreferredSize(new Dimension(240, 160));
        photoLabel.setMaximumSize(new Dimension(240, 160));
        photoLabel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
        linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.Y_AXIS));
        errorLabel.getAccessibleContext().setAccessibleName("Error");

        content.add(nameLabel);
        content.add(summaryLabel);
        content.add(Box.createVerticalStrut(GAP));
        content.add(photoLabel);
        content.add(Box.createVerticalStrut(GAP));
        content.add(linesPanel);
        content.add(compatibilityHolder);
        content.add(errorLabel);
        content.add(Box.createVerticalStrut(GAP));
        applyButton.setMnemonic(KeyEvent.VK_A);
        applyButton.getAccessibleContext().setAccessibleName("Apply to adopt this cat");
        applyButton.addActionListener(event -> apply());
        applyButton.setVisible(false);
        content.add(applyButton);
        for (java.awt.Component component : content.getComponents()) {
            ((javax.swing.JComponent) component).setAlignmentX(LEFT_ALIGNMENT);
        }
        add(content, BorderLayout.CENTER);
    }

    /**
     * Sets what happens when the visitor presses Apply. Until this is set the Apply button stays hidden.
     *
     * @param applyHandler receives the cat's id and name
     */
    public void setApplyHandler(BiConsumer<String, String> applyHandler) {
        this.applyHandler = applyHandler;
        showApplyButtonIfPossible();
    }

    /**
     * Sets the loader used to retrieve a rescue listing photo in the background.
     * @param loader reads a photo from its URL
     */
    public void setExternalPhotoLoader(Function<String, BufferedImage> loader) {
        externalPhotoLoader = loader;
    }

    /**
     * Adds the panel that says how well the cat fits the adopter's home.
     *
     * @param panel the compatibility panel
     */
    public void setCompatibilityPanel(JPanel panel) {
        compatibilityHolder.removeAll();
        compatibilityHolder.add(panel, BorderLayout.CENTER);
    }

    /**
     * Sets what happens each time a cat is shown, so its compatibility can be checked.
     *
     * @param handler receives the id of the cat now shown
     */
    public void setPetShownHandler(Consumer<String> handler) {
        this.petShownHandler = handler;
    }

    private void apply() {
        final PetDetailState state = viewModel.getState();
        final ExternalListingViewData external = state.getExternalListing();
        if (external != null && !external.getAdoptionUrl().isEmpty()) {
            ExternalLink.open(this, external.getAdoptionUrl());
        } else if (external == null && applyHandler != null && state.getErrorMessage().isEmpty()) {
            applyHandler.accept(state.getPetId(), state.getName());
        }
    }

    private void showApplyButtonIfPossible() {
        final PetDetailState state = viewModel.getState();
        final ExternalListingViewData external = state.getExternalListing();
        final boolean canApply = external == null ? applyHandler != null : !external.getAdoptionUrl().isEmpty();
        applyButton.setText(external == null ? "Apply to adopt" : "Visit rescue listing");
        applyButton.setVisible(canApply && !state.getPetId().isEmpty()
                && state.getErrorMessage().isEmpty());
    }

    @Override
    public void stateChanged() {
        final PetDetailState state = viewModel.getState();
        nameLabel.setText(state.getName());
        summaryLabel.setText(state.getSummary());
        final String photoDescription = state.getName().isEmpty()
                ? "No photo" : "Photo of " + state.getName() + " is not available yet";
        photoLabel.getAccessibleContext().setAccessibleName("Cat photo");
        photoLabel.getAccessibleContext().setAccessibleDescription(photoDescription);
        photoLabel.setVisible(state.getErrorMessage().isEmpty());
        errorLabel.setText(state.getErrorMessage());

        linesPanel.removeAll();
        final ExternalListingViewData external = state.getExternalListing();
        if (external != null) {
            linesPanel.add(ExternalLink.organization(external.getOrganizationName(), external.getOrganizationUrl()));
        }
        for (String line : state.getDetailLines()) {
            final JLabel label = new JLabel();
            label.putClientProperty("html.disable", true);
            label.setText(line);
            label.getAccessibleContext().setAccessibleName(line);
            linesPanel.add(label);
        }
        linesPanel.revalidate();
        linesPanel.repaint();
        showApplyButtonIfPossible();
        applyButton.getAccessibleContext().setAccessibleName(applyButton.getText() + " for " + state.getName());
        compatibilityHolder.setVisible(state.getErrorMessage().isEmpty());
        if (petShownHandler != null && !state.getPetId().isEmpty() && state.getErrorMessage().isEmpty()) {
            petShownHandler.accept(state.getPetId());
        }
        photoLabel.setIcon(null);
        photoLabel.setText("No photo yet");
        if (external != null && !external.getPhotoUrl().isEmpty()) {
            showExternalPhoto(state, external.getPhotoUrl());
        }
    }

    private void showExternalPhoto(PetDetailState state, String url) {
        photoLabel.setText("Loading photo…");
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() {
                final BufferedImage image = externalPhotoLoader.apply(url);
                if (image == null) {
                    return null;
                }
                final double scale = Math.min(232.0 / image.getWidth(), 152.0 / image.getHeight());
                final int width = Math.max(1, (int) (image.getWidth() * scale));
                final int height = Math.max(1, (int) (image.getHeight() * scale));
                return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            }

            @Override
            protected void done() {
                if (viewModel.getState() == state) {
                    photoLabel.setText("Photo unavailable");
                    try {
                        final ImageIcon icon = get();
                        if (icon != null) {
                            photoLabel.setText("");
                            photoLabel.setIcon(icon);
                            photoLabel.getAccessibleContext().setAccessibleDescription("Photo of " + state.getName());
                        }
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException exception) {
                        // Photo loading is optional.
                    }
                }
            }
        }.execute();
    }
}
