package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChatView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "chat";
    private final ViewManagerModel viewManagerModel;

    // Components
    private final JLabel chatPartnerLabel; // Displays the name of the user you're chatting with
    private final JTextArea messageInputField;
    private final JButton sendButton;

    // Use this to display the initial prompt or history
    private final JPanel chatDisplayPanel;
    private final JLabel initialPrompt;

    public ChatView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout());

        // Top Bar (Chat Partner and Exit/Back Button)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left Side: Chat Partner Name (Placeholder)
        JPanel partnerInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Using the Profile icon again, but this time for the chat partner
        JLabel partnerIcon = new JLabel("👤");
        partnerIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        chatPartnerLabel = new JLabel("User"); // Placeholder
        chatPartnerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        partnerInfoPanel.add(partnerIcon);
        partnerInfoPanel.add(chatPartnerLabel);
        topBar.add(partnerInfoPanel, BorderLayout.WEST);

        // Right Side: Back/Exit Button (Go back to LoggedInView/Recent Chats)
        JButton backButton = new JButton("⬅");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 20));

        backButton.addActionListener(e -> {
            // Navigate back to the LoggedInView
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });

        topBar.add(backButton, BorderLayout.EAST);

        // Chat Display Area (Main Content)
        chatDisplayPanel = new JPanel();
        chatDisplayPanel.setLayout(new GridBagLayout()); // Using GridBagLayout for centering the message

        // Initial Prompt: "Send 'User' a message to start a chat!"
        initialPrompt = new JLabel("<html><div style='text-align: center;'>Send \"" + chatPartnerLabel.getText() +
                "\" a message to start a chat!</div></html>");
        initialPrompt.setFont(new Font("SansSerif", Font.ITALIC, 16));

        chatDisplayPanel.add(initialPrompt); // Adds the prompt centered by GridBagLayout

        JScrollPane chatScrollPane = new JScrollPane(chatDisplayPanel);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Input Panel (Text field and Send Button)
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Padding

        messageInputField = new JTextArea(3, 1);
        messageInputField.setLineWrap(true);
        messageInputField.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(messageInputField);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setPreferredSize(new Dimension(80, inputScrollPane.getPreferredSize().height));

        sendButton.addActionListener(this);

        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Assembly
        this.add(topBar, BorderLayout.NORTH);
        this.add(chatScrollPane, BorderLayout.CENTER);
        this.add(inputPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(sendButton)) {
            // Send message logic (currently does nothing)
            String message = messageInputField.getText();
            if (!message.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Message sent to " + chatPartnerLabel.getText() + ": " + message);
                messageInputField.setText(""); // Clear the input
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // This is where the view would update the chatPartnerLabel
        // and load the chat history when the state changes.
    }

    public String getViewName() {
        return viewName;
    }

    /**
     * Public method to set the chat partner's username and update the view.
     * @param username The username of the chat partner.
     */
    public void setChatPartner(String username) {
        this.chatPartnerLabel.setText(username);
        // Update the initial prompt to reflect the new user
        this.initialPrompt.setText("<html><div style='text-align: center;'>Send \"" + username +
                "\" a message to start a chat!</div></div>");
        this.revalidate();
        this.repaint();
    }
}