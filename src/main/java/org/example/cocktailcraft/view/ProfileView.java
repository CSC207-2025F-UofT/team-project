package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.NavigationController;
import org.example.cocktailcraft.interface_adapter.logout.LogoutController;
import org.example.cocktailcraft.interface_adapter.logout.LogoutViewModel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Preview navigation grants no authentication or access to personal data. */
public class ProfileView extends JPanel {
    public static final String VIEW_NAME = "profile";

    public ProfileView(LogoutController logout, LogoutViewModel model,
                       NavigationController navigation) {
        super(new BorderLayout());
        JPanel content = ViewSupport.column();
        content.add(new JLabel("Personal Profile - guest preview (not logged in)"));
        content.add(ViewSupport.button("Login", () -> navigation.show(LoginView.VIEW_NAME)));
        content.add(ViewSupport.button("Register", () -> navigation.show(SignupView.VIEW_NAME)));
        content.add(new JLabel("Favourites - TODO(Wenqian): connect the user's saved collection."));
        content.add(new JLabel("My Custom Cocktails - TODO(Enhao): connect saved recipes and editing."));
        content.add(ViewSupport.button("Logout (scaffold)", logout::execute));
        content.add(ViewSupport.status(model));
        content.add(ViewSupport.button("Home", () -> navigation.show(MainPageView.VIEW_NAME)));
        add(content, BorderLayout.CENTER);
    }
}
