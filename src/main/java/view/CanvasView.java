package view;

import interface_adapter.CanvasController;
import interface_adapter.CanvasViewModel;
import interface_adapter.CanvasViewModelListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CanvasView extends JPanel implements ActionListener, CanvasViewModelListener {

    private final CanvasViewModel canvasViewModel;
    private final CanvasController controller = null;

    // Main panels
    private final JPanel topPanel = new JPanel();
    private final JPanel toolbarPanel = new JPanel();
    private final JPanel sidebarPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();

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
    private final JToggleButton brushButton = new JToggleButton("Brush");
    private final JToggleButton shapeButton = new JToggleButton("Shape");
    private final JToggleButton eraserButton = new JToggleButton("Eraser");

    // Canvas
    private final JPanel canvasPanel = new JPanel();
    private final JScrollPane canvasScrollPane = new JScrollPane(canvasPanel);

    // Sidebar
    private final DefaultListModel<String> layerModel = new DefaultListModel<>();
    private final JList<String> layerList = new JList<>(layerModel);

    // Bottom bar
    private final JButton exportButton = new JButton("Export");
    private final JButton saveButton = new JButton("Save");
    private final JButton publishButton = new JButton("Publish");


    public CanvasView(CanvasViewModel canvasViewModel) {
        this.canvasViewModel = canvasViewModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
