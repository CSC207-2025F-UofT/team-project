package app;

import data_access.SceneTerrainMapper;
import entity.Ball;
import entity.ObjectPreset;
import entity.Scene;
import entity.Terrain;
import entity.Vector3D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Terrain Physics Simulator");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setContentPane(createPreview());
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

    static JPanel createPreview() {
        return new AppBuilder(flatPreviewScene(), new SceneTerrainMapper())
                .addSimulationView()
                .addSaveSceneUseCase()
                .build();
    }

    static Scene flatPreviewScene() {
        Terrain terrain = new Terrain("Flat terrain", 2.0, new double[16][16]);
        Scene scene = new Scene(terrain);
        ObjectPreset preset = new ObjectPreset("Bowling ball", 1.4, 6, 0.28, 0.4, 0xb64732);
        double centerX = terrain.width() / 2;
        double centerY = terrain.depth() / 2;
        Vector3D position = new Vector3D(centerX, centerY, preset.getRadius());
        scene.addBall(new Ball(position, Vector3D.ZERO, preset));
        return scene;
    }
}
