package app;

import java.awt.Component;
import java.awt.Container;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import data_access.FilePetDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.InMemoryPetDataAccess;
import entity.User.Role;
import entity.UserFactory;
import view.AdminSignupView;
import view.GalleryView;
import view.LoginView;
import view.ManagePetView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PetManagementFlowTest {
    private static final String PASSWORD = "password";
    private static final String PASSWORD_HASH = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
    private static final String ADMIN_EMAIL = "admin@example.test";
    private static final String ADOPTER_EMAIL = "adopter@example.test";
    private static final String INVITE = "test-invite";

    @TempDir
    private Path dataDirectory;

    @Test
    void onlyAuthenticatedAdminsReachManagementFromTheMainGallery() throws Exception {
        final UserFactory factory = new UserFactory();
        final FileUserDataAccessObject users =
                new FileUserDataAccessObject(dataDirectory.resolve("users.json"), factory);
        users.save(factory.create("Adopter", "adopter-1", ADOPTER_EMAIL, PASSWORD_HASH, Role.ADOPTER, null));
        final AppBuilder builder = AppBuilder.forDataDirectory(dataDirectory, dataDirectory, INVITE);

        SwingUtilities.invokeAndWait(() -> {
            final JPanel views = builder.buildViews();
            assertScreen(views, GalleryView.class);
            click(activeCard(views), "Shelter login");
            assertScreen(views, LoginView.class);
            login(views, ADOPTER_EMAIL, PASSWORD);
            assertScreen(views, GalleryView.class);

            click(activeCard(views), "Shelter login");
            click(activeCard(views), "Create shelter account");
            assertScreen(views, AdminSignupView.class);
            fill(activeCard(views), "Admin", "admin-1", ADMIN_EMAIL, PASSWORD, PASSWORD, INVITE);
            click(activeCard(views), "Sign up");
            assertScreen(views, LoginView.class);
            login(views, ADMIN_EMAIL, "wrong-password");
            assertScreen(views, LoginView.class);
            login(views, ADMIN_EMAIL, PASSWORD);
            assertScreen(views, ManagePetView.class);
            click(activeCard(views), "Sign out");
            assertScreen(views, GalleryView.class);
            click(activeCard(views), "Shelter login");
            assertTrue(findAll(activeCard(views), JTextField.class).stream()
                    .allMatch(field -> field.getDocument().getLength() == 0));
        });
    }

    @Test
    void mainGalleryLoadsPersistedListingsIncludingAnEmptyCatalog() throws Exception {
        final FilePetDataAccessObject previousPets = new FilePetDataAccessObject(dataDirectory.resolve("pets.json"));
        previousPets.add(InMemoryPetDataAccess.withSampleData().getAvailablePets().get(0));
        final Path profile = dataDirectory.resolve("profile");
        final AppBuilder populated = AppBuilder.forDataDirectory(profile, dataDirectory, null);
        final FilePetDataAccessObject savedPets =
                new FilePetDataAccessObject(profile.resolve("pets.json"), previousPets.getPets());
        savedPets.remove(savedPets.getPets().get(0).getId());
        final AppBuilder empty = AppBuilder.forDataDirectory(profile, dataDirectory, null);
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(List.of("Pet card: Miso"), cardNames(populated.buildViews()));
            assertEquals(List.of(), cardNames(empty.buildViews()));
        });
    }

    private static List<String> cardNames(JPanel views) {
        return findAll(activeCard(views), JPanel.class).stream()
                .map(panel -> panel.getAccessibleContext().getAccessibleName())
                .filter(name -> name != null && name.startsWith("Pet card: "))
                .toList();
    }

    private static void login(JPanel views, String email, String password) {
        fill(activeCard(views), email, password);
        click(activeCard(views), "log in");
    }

    private static void fill(Container card, String... values) {
        final List<JTextField> fields = findAll(card, JTextField.class);
        assertEquals(values.length, fields.size());
        for (int index = 0; index < values.length; index++) {
            fields.get(index).setText(values[index]);
        }
    }

    private static void click(Container card, String label) {
        findAll(card, JButton.class).stream()
                .filter(button -> label.equals(button.getText()))
                .findFirst().orElseThrow()
                .doClick(0);
    }

    private static void assertScreen(JPanel views, Class<? extends JPanel> type) {
        assertEquals(1, findAll(activeCard(views), type).size());
    }

    private static JPanel activeCard(JPanel views) {
        return (JPanel) Arrays.stream(views.getComponents()).filter(Component::isVisible).findFirst().orElseThrow();
    }

    private static <T extends Component> List<T> findAll(Component root, Class<T> type) {
        final List<T> matches = new ArrayList<>();
        if (type.isInstance(root)) {
            matches.add(type.cast(root));
        }
        if (root instanceof Container container) {
            for (Component child : container.getComponents()) {
                matches.addAll(findAll(child, type));
            }
        }
        return matches;
    }
}
