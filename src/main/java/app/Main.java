package app;

import data_access.SchemaInitializer;
import data_access.SqliteConnectionProvider;
import data_access.SqliteGameDataAccessObject;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Starts the game. All wiring lives in AppBuilder. */
public class Main {
    private static final String TEMPORARY_USERNAME = "__story3_demo__";

    public static void main(String[] args) {
        SqliteConnectionProvider connections = new SqliteConnectionProvider();
        try {
            new SchemaInitializer(connections).initialize();
            createTemporaryProfile(connections);
        } catch (SQLException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Could not initialize the game database", e);
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Couldn't open the local game database.", "UofTears", JOptionPane.ERROR_MESSAGE));
            return;
        }
        SwingUtilities.invokeLater(() -> {
            JFrame application = new AppBuilder()
                    .addGameView()
                    .addDialogueView()
                    .addNewGameUseCase()
                    .addTalkToNpcUseCase()
                    .addPersistenceUseCases(new SqliteGameDataAccessObject(connections),
                            () -> TEMPORARY_USERNAME)
                    .build();
            application.setSize(480, 360);
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }

    /** Removed when Story 1 provides authenticated accounts. */
    private static void createTemporaryProfile(SqliteConnectionProvider connections) throws SQLException {
        try (var connection = connections.openConnection();
             var statement = connection.prepareStatement("""
                     INSERT INTO users (username, password_hash) VALUES (?, '')
                     ON CONFLICT(username) DO NOTHING
                     """)) {
            statement.setString(1, TEMPORARY_USERNAME);
            statement.executeUpdate();
        }
    }
}
