package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.selectedplace.SelectedPlaceController;
import interface_adapter.selectedplace.SelectedPlaceState;
import interface_adapter.selectedplace.SelectedPlaceViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SelectedPlaceView extends JPanel implements PropertyChangeListener {

    private final String viewName = "selected place";
    private final SelectedPlaceViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    private SelectedPlaceController controller;

    private JLabel usernameLabel;
    private JLabel landmarkNameLabel;
    private JTextArea descriptionArea;
    private JLabel addressLabel;
    private JLabel hoursLabel;
    private JLabel imageLabel;

    // NEW: keep reference so we can style/disable it
    private JButton checkInButton;

    public SelectedPlaceView(SelectedPlaceViewModel viewModel,
                             ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === TOP BAR ===
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        userPanel.setOpaque(false);
        usernameLabel = new JLabel("Username1!");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(new Color(0, 102, 204));
        userPanel.add(usernameLabel);

        JLabel logoutLabel = new JLabel("Logout");
        logoutLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutLabel.setForeground(new Color(0, 102, 204));
        logoutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topBar.add(userPanel, BorderLayout.WEST);
        topBar.add(logoutLabel, BorderLayout.EAST);

        // === BOTTOM BAR ===
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton backButton = new JButton("back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setForeground(new Color(0, 102, 204));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            viewManagerModel.setState("browse landmarks");
            viewManagerModel.firePropertyChange();
        });

        bottomBar.add(backButton);

        // === CENTER: left buttons + right content ===
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.setBackground(Color.WHITE);

        // LEFT SIDE — centered buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 20));

        checkInButton = createBigButton("Check In");
        JButton notesButton = createBigButton("Notes");

        // Check In: call controller once, then turn green + disable
        checkInButton.addActionListener(e -> {
            if (!checkInButton.isEnabled()) {
                return; // already clicked
            }

            if (controller != null) {
                controller.checkIn();
            }

            // visual change to green & disabled
            checkInButton.setBackground(new Color(0, 180, 0));
            checkInButton.setForeground(Color.WHITE);
            checkInButton.setBorder(BorderFactory.createLineBorder(new Color(0, 140, 0), 2));
            checkInButton.setEnabled(false);
        });

        // Notes: still just delegates to controller
        notesButton.addActionListener(e -> {
            if (controller != null) controller.notes();
        });

        // center vertically
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(checkInButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(notesButton);
        leftPanel.add(Box.createVerticalGlue());

        // RIGHT SIDE — landmark info section
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 60));

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(600, 300));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ImageIcon img = new ImageIcon("src/main/resources/placeholder_landmark.jpg");
        Image scaled = img.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));

        landmarkNameLabel = new JLabel("Landmark Name");
        landmarkNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        landmarkNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setOpaque(false);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        addressLabel = new JLabel();
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        hoursLabel = new JLabel();
        hoursLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        hoursLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(imageLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(landmarkNameLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(descriptionArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(addressLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(hoursLabel);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        add(topBar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }

    public void setSelectedPlaceController(SelectedPlaceController controller) {
        this.controller = controller;
    }

    private JButton createBigButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        btn.setPreferredSize(new Dimension(200, 70));
        btn.setMaximumSize(new Dimension(200, 70));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0, 102, 204));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) return;

        SelectedPlaceState state = (SelectedPlaceState) evt.getNewValue();
        usernameLabel.setText(state.getUsername());
        landmarkNameLabel.setText(state.getLandmarkName());
        descriptionArea.setText(state.getDescription());
        addressLabel.setText(state.getAddress());
        hoursLabel.setText("Hours: " + state.getOpenHours());

        // whenever a new place is loaded, reset Check In button appearance
        if (checkInButton != null) {
            checkInButton.setEnabled(true);
            checkInButton.setBackground(Color.WHITE);
            checkInButton.setForeground(new Color(0, 102, 204));
            checkInButton.setBorder(
                    BorderFactory.createLineBorder(new Color(0, 102, 204), 2)
            );
        }
    }

    public String getViewName() {
        return viewName;
    }
}
