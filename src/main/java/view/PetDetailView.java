package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import interface_adapter.SwitchViewController;
import interface_adapter.view_pet_details.PetDetailState;
import interface_adapter.view_pet_details.PetDetailViewModel;
import interface_adapter.view_pet_details.PetDetailViewModelListener;

/**
 * Shows one cat's photo, profile sections, and adoption actions.
 */
public class PetDetailView extends JPanel implements PetDetailViewModelListener {
    private static final int GAP = 12;
    private static final int PHOTO_WIDTH = 340;
    private static final int PHOTO_HEIGHT = 300;
    private static final int PHOTO_CAPTION_HEIGHT = 20;

    private final PetDetailViewModel viewModel;
    private final JLabel nameLabel = new JLabel();
    private final JLabel summaryLabel = new JLabel();
    private final JLabel photoLabel = new JLabel("No photo yet", SwingConstants.CENTER);
    private final JPanel linesPanel = new JPanel();
    private final JLabel errorLabel = new JLabel();
    private final JButton applyButton = new JButton("Apply to adopt");
    private final JPanel compatibilityHolder = new JPanel(new BorderLayout()) {
        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }
    };
    private BiConsumer<String, String> applyHandler;
    private Consumer<String> petShownHandler;

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

        // Photo on the left, everything else on the right, so the page uses its whole width.
        final JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 28f));
        nameLabel.getAccessibleContext().setAccessibleName("Cat name");
        summaryLabel.setFont(summaryLabel.getFont().deriveFont(15f));
        summaryLabel.getAccessibleContext().setAccessibleName("Cat summary");
        final Dimension photoSize = new Dimension(PHOTO_WIDTH, PHOTO_HEIGHT + PHOTO_CAPTION_HEIGHT);
        photoLabel.setPreferredSize(photoSize);
        photoLabel.setMinimumSize(photoSize);
        photoLabel.setMaximumSize(photoSize);
        photoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        photoLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        photoLabel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
        linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.Y_AXIS));
        errorLabel.getAccessibleContext().setAccessibleName("Error");

        details.add(nameLabel);
        details.add(summaryLabel);
        details.add(Box.createVerticalStrut(GAP));
        details.add(linesPanel);
        details.add(Box.createVerticalStrut(GAP));
        details.add(compatibilityHolder);
        details.add(errorLabel);
        details.add(Box.createVerticalStrut(GAP));
        applyButton.setMnemonic(KeyEvent.VK_A);
        applyButton.getAccessibleContext().setAccessibleName("Apply to adopt this cat");
        applyButton.addActionListener(event -> apply());
        applyButton.setVisible(false);
        details.add(applyButton);
        for (java.awt.Component component : details.getComponents()) {
            ((javax.swing.JComponent) component).setAlignmentX(LEFT_ALIGNMENT);
        }

        final JPanel photoColumn = new JPanel(new BorderLayout());
        photoColumn.add(photoLabel, BorderLayout.NORTH);
        final JPanel content = new JPanel(new BorderLayout(GAP * 2, 0));
        content.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        content.add(photoColumn, BorderLayout.WEST);
        content.add(details, BorderLayout.CENTER);
        final javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
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
        if (applyHandler != null && state.getErrorMessage().isEmpty()) {
            applyHandler.accept(state.getPetId(), state.getName());
        }
    }

    private void showApplyButtonIfPossible() {
        final PetDetailState state = viewModel.getState();
        applyButton.setVisible(applyHandler != null && !state.getPetId().isEmpty()
                && state.getErrorMessage().isEmpty());
    }

    @Override
    public void stateChanged() {
        final PetDetailState state = viewModel.getState();
        nameLabel.setText(state.getName());
        summaryLabel.setText(state.getSummary());
        photoLabel.getAccessibleContext().setAccessibleName("Cat photo");
        photoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        photoLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        ListingPhoto.show(photoLabel, state.getPhotoPath(), PHOTO_WIDTH - 2, PHOTO_HEIGHT);
        photoLabel.setVisible(state.getErrorMessage().isEmpty());
        errorLabel.setText(state.getErrorMessage());

        linesPanel.removeAll();
        addSection("Basic info", state.getBasicInfo());
        addSection("Details", state.getDetailLines());
        addSection("Health", state.getHealthLines());
        linesPanel.revalidate();
        linesPanel.repaint();
        showApplyButtonIfPossible();
        applyButton.getAccessibleContext().setAccessibleName("Apply to adopt " + state.getName());
        compatibilityHolder.setVisible(state.getErrorMessage().isEmpty());
        if (petShownHandler != null && !state.getPetId().isEmpty() && state.getErrorMessage().isEmpty()) {
            petShownHandler.accept(state.getPetId());
        }
    }

    private void addSection(String title, List<String> lines) {
        if (lines.isEmpty()) {
            return;
        }
        final JLabel heading = new JLabel(title);
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 18f));
        heading.setAlignmentX(LEFT_ALIGNMENT);
        linesPanel.add(heading);
        linesPanel.add(Box.createVerticalStrut(4));
        for (String line : lines) {
            final JLabel label = new JLabel(line);
            label.setFont(label.getFont().deriveFont(15f));
            label.getAccessibleContext().setAccessibleName(line);
            label.setAlignmentX(LEFT_ALIGNMENT);
            linesPanel.add(label);
        }
        linesPanel.add(Box.createVerticalStrut(GAP));
    }
}
