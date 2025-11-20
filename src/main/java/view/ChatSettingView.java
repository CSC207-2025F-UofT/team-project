package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.groupchat.ChangeGroupNameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatSettingView extends JPanel implements ActionListener {

    public final String viewName = "chat setting";  // Changed from "chat settings" to match ChatView

    private final ViewManagerModel viewManagerModel;

    // Buttons
    private final JButton changeGroupNameButton;
    private final JButton addUserButton;
    private final JButton removeUserButton;

    private ChangeGroupNameController changeGroupNameController;  // Removed 'final'

    public ChatSettingView(ViewManagerModel viewManagerModel) {  // Removed controller from constructor
        this.viewManagerModel = viewManagerModel;

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Top Bar with Back Button =====
        JPanel topBar = new JPanel(new BorderLayout());

        JButton backButton = new JButton("⬅");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        backButton.addActionListener(e -> {
            viewManagerModel.setState("chat");
            viewManagerModel.firePropertyChange();
        });
        topBar.add(backButton, BorderLayout.WEST);

        JLabel title = new JLabel("Chat Settings", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        topBar.add(title, BorderLayout.CENTER);

        this.add(topBar, BorderLayout.NORTH);

        // ===== Main Button Panel =====
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        changeGroupNameButton = new JButton("Change Group Name");
        addUserButton = new JButton("Add User");
        removeUserButton = new JButton("Remove User");

        changeGroupNameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        changeGroupNameButton.addActionListener(this);
        addUserButton.addActionListener(this);
        removeUserButton.addActionListener(this);

        contentPanel.add(changeGroupNameButton);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(addUserButton);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(removeUserButton);

        this.add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(changeGroupNameButton)) {
            if (changeGroupNameController != null) {  // Add null check
                String newName = JOptionPane.showInputDialog(this, "Enter new group name:");
                if (newName != null && !newName.trim().isEmpty()) {
                    System.out.println("Rename group to: " + newName);
                    // TODO: replace "group123" with your actual selected group ID
                    changeGroupNameController.execute("chat-1", newName);  // Use actual chat ID
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Controller not initialized",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
//        else if (evt.getSource().equals(addUserButton)) {
//            String user = JOptionPane.showInputDialog(this, "Enter username to add:");
//            if (user != null && !user.trim().isEmpty()) {
//                System.out.println("Add user: " + user);
//                // TODO: call controller
//            }
//        }
//        else if (evt.getSource().equals(removeUserButton)) {
//            String user = JOptionPane.showInputDialog(this, "Enter username to remove:");
//            if (user != null && !user.trim().isEmpty()) {
//                System.out.println("Remove user: " + user);
//                // TODO: call controller
//            }
//        }
    }

    public String getViewName() {
        return viewName;
    }

    // Add this setter method
    public void setChangeGroupNameController(ChangeGroupNameController controller) {
        this.changeGroupNameController = controller;
    }
}