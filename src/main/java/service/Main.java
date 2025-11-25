package service;

public class Main {

    public static void main(String[] args) {
        Backend backend = new Backend();

        backend.fromJsonFiles(
                "users.json",
                "pets.json",
                "admins.json",
                "applications.json");

        javax.swing.SwingUtilities.invokeLater(() -> {
            Frontend frontend = new Frontend(backend);
            frontend.setVisible(true);
        });
    }
}
