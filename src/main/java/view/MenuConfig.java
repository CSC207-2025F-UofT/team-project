package view;

import java.util.LinkedHashMap;
import java.util.Map;

public class MenuConfig {

    // return different userType menu
    public static Map<String, String[]> getMenuItems() {
        Map<String, String[]> map = new LinkedHashMap<>();

        // Admin
        map.put("admin", new String[]{
                "Manage Applications",
                "View Pets",
                "Add Pet",
                "Modify Pet",
                "Delete Pet",
                "Log Out"
        });

        // Staff
        map.put("staff", new String[]{
                "View Pets",
                "Add Pet",
                "Modify Pet",
                "Log Out"
        });

        // user
        map.put("user", new String[]{
                "View Pets",
                "Log Out"
        });

        return map;
    }
}
