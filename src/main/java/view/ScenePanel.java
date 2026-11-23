package view;

import interface_adapter.simulation.CameraPose;
import interface_adapter.simulation.ScenePresenter;
import interface_adapter.simulation.SceneView;
import interface_adapter.simulation.SimulationState;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Path;

/** Swing implementation of the passive scene view. */
public final class ScenePanel extends JPanel implements SceneView {
    private final JPanel fileControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 7));
    private final JButton saveButton = new JButton("Save Scene");
    private final JLabel status = new JLabel();
    private final JFileChooser chooser = new JFileChooser();
    private final TerrainView3D terrain;

    public ScenePanel(ScenePresenter presenter) {
        super(new BorderLayout());
        terrain = new TerrainView3D(presenter);
        chooser.setFileFilter(new FileNameExtensionFilter("JSON scene files", "json"));
        JButton loadButton = new JButton("Load Scene");
        saveButton.addActionListener(event -> presenter.saveRequested());
        loadButton.addActionListener(event -> presenter.loadRequested());
        fileControls.add(saveButton);
        fileControls.add(loadButton);
        fileControls.add(status);
    }

    public void addSimulationView() {
        add(terrain, BorderLayout.CENTER);
    }

    public void addFileControls() {
        add(fileControls, BorderLayout.NORTH);
    }

    @Override
    public Path chooseSaveFile() {
        return chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile().toPath() : null;
    }

    @Override
    public Path chooseLoadFile() {
        return chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile().toPath() : null;
    }

    @Override
    public boolean confirmOverwrite(String fileName) {
        return JOptionPane.showConfirmDialog(this, "Replace " + fileName + "?", "Save Scene",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public void display(SimulationState state, CameraPose camera) {
        status.setText(state.getStatusText());
        saveButton.setEnabled(state.hasTerrain());
        terrain.display(state, camera);
    }
}
