package view;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.adopter_login.AdopterLoginViewModel;
import interface_adapter.admin_login.AdminLoginViewModel;
import interface_adapter.navigation.NavigationController;
import interface_adapter.role_select.RoleSelectViewModel;

/**
 * First screen: choose to continue as an adopter or as the admin.
 */
public class RoleSelectView extends JPanel {

    public RoleSelectView(NavigationController navigationController) {
        setLayout(new BorderLayout());
        setName(RoleSelectViewModel.VIEW_NAME);

        final JButton adopterButton = new JButton("Adopter");
        final JButton adminButton = new JButton("Admin");
        adopterButton.addActionListener(e -> navigationController.goTo(AdopterLoginViewModel.VIEW_NAME));
        adminButton.addActionListener(e -> navigationController.goTo(AdminLoginViewModel.VIEW_NAME));

        final JLabel or = new JLabel("or");
        or.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel content = FormUtils.verticalPanel();
        content.add(FormUtils.title("Pet Adoption"));
        content.add(Box.createVerticalStrut(20));
        content.add(FormUtils.centered(adopterButton));
        content.add(or);
        content.add(FormUtils.centered(adminButton));
        add(content, BorderLayout.CENTER);
    }
}
