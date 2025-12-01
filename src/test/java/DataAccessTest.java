import entity.User;
import data_access.FileUserDAO;
import data_access.UserDAO;

class DataAccessTest {

    public static void main(String[] args) {
        // Create DAO
        UserDAO userDAO = new FileUserDAO();

        // Create a user (with empty lists for now)
        User user = new User();

        // Save user
        System.out.println("Saving user...");
        userDAO.saveUser(user);
        System.out.println("✓ User saved to userdata.json");

        // Load user back
        System.out.println("\nLoading user...");
        User loadedUser = userDAO.loadUser();

        if (loadedUser != null) {
            System.out.println("✓ User loaded successfully");
            // System.out.println("  - Inventory list: " + (loadedUser.getInventory() != null));
        } else {
            System.out.println("✗ Failed to load user");
        }

        // Check if user exists
        System.out.println("\nUser exists: " + userDAO.userExists());
    }
}
