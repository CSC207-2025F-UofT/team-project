package view;

import javax.swing.*;
import interface_adapter.homepage.LoginChooseViewModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginChooseView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "loginChoose";
    private final LoginChooseViewModel loginChooseViewModel;

    public LoginChooseView(LoginChooseViewModel loginChooseViewModel) {
        this.loginChooseViewModel = loginChooseViewModel;
        this.loginChooseViewModel.addPropertyChangeListener(this);

        // background color
        setBackground(new Color(0xF5F7FB));
        setLayout(new GridBagLayout());

        // Panel
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(220,220,220), 1, true)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(350, 380));

        // title
        JLabel title = new JLabel("Pet Adoption System");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(60, 60, 75));

        // button
        JButton userLoginBtn = UIFactory.createPrimaryButton("User Login");
        JButton staffLoginBtn = UIFactory.createPrimaryButton("Staff Login");
        JButton adminLoginBtn = UIFactory.createPrimaryButton("Admin Login");
        JButton createAccountBtn = UIFactory.createSecondaryButton("Create New Account");

        card.add(Box.createVerticalStrut(20));
        card.add(title);

        card.add(Box.createVerticalStrut(30));
        card.add(userLoginBtn);
        card.add(Box.createVerticalStrut(15));
        card.add(staffLoginBtn);
        card.add(Box.createVerticalStrut(15));
        card.add(adminLoginBtn);

        card.add(Box.createVerticalStrut(25));
        card.add(createAccountBtn);
        card.add(Box.createVerticalStrut(20));

        add(card);

        // addActionListener
        userLoginBtn.addActionListener(e ->
                loginChooseViewModel.firePropertyChange("login", null,  "user"));
        staffLoginBtn.addActionListener(e ->
                loginChooseViewModel.firePropertyChange("login", null, "staff"));
        adminLoginBtn.addActionListener(e ->
                loginChooseViewModel.firePropertyChange("login", null, "admin"));
        createAccountBtn.addActionListener(e ->
                loginChooseViewModel.firePropertyChange("createAccount", null, null));
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(ActionEvent evt) { }

    @Override
    public void propertyChange(PropertyChangeEvent evt) { }
}
