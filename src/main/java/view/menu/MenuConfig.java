package view.menu;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MenuConfig defines the menu items available for each user role.
 * 
 * This class provides a static method that returns a mapping:
 *
 * The returned menu preserves the order using LinkedHashMap.
 */
public class MenuConfig {

    /**
     * Returns a map defining menu items for different user roles
     *
     * @return LinkedHashMap mapping userType → String[] of menu labels
     */
    public static Map<String, String[]> getMenuItems() {
        Map<String, String[]> map = new LinkedHashMap<>();

        // Menu for Admin
        map.put("admin", new String[]{
                "My Profile",
                "Edit Profile",
                "My Applications",
                "Manage Applications",
                "View Pets",
                "Call API",
                "Add Pet",
                "Modify Pet",
                "Delete Pet",
                "Log Out"
        });

        // Menu for Staff
        map.put("staff", new String[]{
                "My Profile",
                "Edit Profile",
                "View Pets",
                "Call API",
                "Add Pet",
                "Modify Pet",
                "Delete Pet",
                "Log Out"
        });

        // Menu for Regular User
        map.put("user", new String[]{
                "My Profile",
                "Edit Profile",
                "My Applications",
                "View Pets",
                "Log Out"
        });

        return map;
    }
}
