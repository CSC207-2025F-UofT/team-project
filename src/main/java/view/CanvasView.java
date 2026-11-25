package view;

import interface_adapter.CanvasController;
import interface_adapter.CanvasState;
import interface_adapter.CanvasViewModel;
import interface_adapter.CanvasViewModelListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CanvasView extends JPanel implements ActionListener, CanvasViewModelListener {

    private final CanvasViewModel canvasViewModel;
    private CanvasController canvasController = null;
    // private

    // Main panels
    private final JPanel topPanel = new JPanel();
    private final JPanel toolbarPanel = new JPanel();
    private final JPanel sidebarPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final JPanel canvasPanel = new JPanel();

    // Top bar
    private final JButton exitButton = new JButton("Exit");
    private final JLabel titleLabel = new JLabel();
    private final JButton undoButton = new JButton("Undo");
    private final JButton redoButton = new JButton("Redo");

    // Toolbar
    private final ButtonGroup toolGroup = new ButtonGroup();
    private final JButton importButton = new JButton("Import");
    private final JButton searchButton = new JButton("Search");
    private final JToggleButton selectButton = new JToggleButton("Select");
    private final JToggleButton drawButton = new JToggleButton("Draw");
    private final JToggleButton shapeButton = new JToggleButton("Shape");
    private final JToggleButton eraserButton = new JToggleButton("Eraser");

    // Sidebar
    private final DefaultListModel<String> layerModel = new DefaultListModel<>();
    private final JList<String> layerList = new JList<>(layerModel);
    private final JPanel paletteColoursPanel = new JPanel();
    private final List<JButton> paletteButtons = new ArrayList<>();
    private final JButton createPaletteButton = new JButton("Create Colour Palette");

    // Bottom bar
    private final JButton exportButton = new JButton("Export");
    private final JButton saveButton = new JButton("Save");
    private final JButton publishButton = new JButton("Publish");


    public CanvasView(CanvasViewModel canvasViewModel) {
        this.canvasViewModel = canvasViewModel;
        canvasViewModel.addListener(this);

        setLayout(new BorderLayout());

        createCenterPanel();
        createTopPanel();
        createToolbarPanel();
        createSidebarPanel();
        createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(toolbarPanel, BorderLayout.WEST);
        JScrollPane canvasScrollPane = new JScrollPane(canvasPanel);
        add(canvasScrollPane, BorderLayout.CENTER);
        add(sidebarPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        initializeActionListeners();
    }

    private void createCenterPanel() {
        canvasPanel.setBackground(canvasViewModel.getCanvasColour());
        canvasPanel.setPreferredSize(new Dimension(
                canvasViewModel.getCanvasWidth(),
                canvasViewModel.getCanvasHeight()));
    }

    private void createTopPanel() {
        topPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(exitButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(undoButton);
        rightPanel.add(redoButton);

        titleLabel.setText(canvasViewModel.getTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void createToolbarPanel() {
        toolGroup.add(selectButton);
        toolGroup.add(drawButton);
        toolGroup.add(shapeButton);
        toolGroup.add(eraserButton);
        selectButton.setSelected(true);

        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.Y_AXIS));
        toolbarPanel.add(selectButton);
        toolbarPanel.add(drawButton);
        toolbarPanel.add(shapeButton);
        toolbarPanel.add(eraserButton);
        toolbarPanel.add(importButton);
        toolbarPanel.add(searchButton);
    }

    private void createBottomPanel() {
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        bottomPanel.add(exportButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(publishButton);
    }

    private void createSidebarPanel() {
        sidebarPanel.setLayout(new BorderLayout(0, 15));
        sidebarPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JPanel layersPanel = new JPanel(new BorderLayout());
        JLabel layersLabel = new JLabel("Layers");
        layerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        layersPanel.add(layersLabel, BorderLayout.NORTH);
        layersPanel.add(new JScrollPane(layerList), BorderLayout.CENTER);


        JPanel coloursPanel = new JPanel(new BorderLayout(0, 10));
        JLabel paletteLabel = new JLabel("Colour Palette");
        paletteColoursPanel.setLayout(new GridLayout(0, 4, 5, 5));
        for (int i = 0; i < 12; i++) {
            JButton colourButton = new JButton();

            colourButton.setPreferredSize(new Dimension(35, 35));
            colourButton.setBackground(Color.WHITE);
            colourButton.setOpaque(true);
            colourButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            paletteButtons.add(colourButton);
            paletteColoursPanel.add(colourButton);
        }

        coloursPanel.add(paletteLabel, BorderLayout.NORTH);
        coloursPanel.add(paletteColoursPanel, BorderLayout.CENTER);
        coloursPanel.add(createPaletteButton, BorderLayout.SOUTH);

        sidebarPanel.add(layersPanel, BorderLayout.CENTER);
        sidebarPanel.add(coloursPanel, BorderLayout.SOUTH);
    }


    private void initializeActionListeners() {
        exitButton.addActionListener(this);
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);
        importButton.addActionListener(this);
        searchButton.addActionListener(this);
        selectButton.addActionListener(this);
        drawButton.addActionListener(this);
        shapeButton.addActionListener(this);
        eraserButton.addActionListener(this);
        exportButton.addActionListener(this);
        saveButton.addActionListener(this);
        publishButton.addActionListener(this);
        createPaletteButton.addActionListener(this);

        for (JButton colourButton : paletteButtons) {
            colourButton.addActionListener(this);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == exitButton) {
            // Exit canvas, return to Home view
        } else if (source == undoButton) {
            // Undo last action
        } else if (source == redoButton) {
            // Redo last action

        } else if (source == importButton) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                final CanvasState currentState = canvasViewModel.getState();
                canvasController.importImage(selectedFile, currentState.getProjectId());
            }

        } else if (source == searchButton) {
            // Open image search
        } else if (source == selectButton) {
            // Switch to select tool
        } else if (source == drawButton) {
            // Switch to brush tool
        } else if (source == shapeButton) {
            // Switch to shape tool
        } else if (source == eraserButton) {
            // Switch to eraser tool
        } else if (source == createPaletteButton) {
            // Open colour palette dialog
        } else if (source == saveButton) {
            // Save project
        } else if (source == exportButton) {
            // Export project
        } else if (source == publishButton) {
            // Publish project
        }
    }

    public void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
    }

    @Override
    public void stateChanged() {
        // Add logic
    }
}
