package goc.chat.view;

import goc.chat.interfaceadapters.repo.InMemoryChatRepository;
import goc.chat.interfaceadapters.repo.InMemoryMessageRepository;
import goc.chat.interfaceadapters.repo.InMemoryUserRepository;
import goc.chat.usecase.messaging.ChatMessageDto;
import goc.chat.usecase.messaging.SendMessageInteractor;
import goc.chat.usecase.messaging.ViewChatHistoryInputData;
import goc.chat.usecase.messaging.ViewChatHistoryInteractor;
import goc.chat.usecase.messaging.ViewChatHistoryOutputBoundary;
import goc.chat.usecase.messaging.ViewChatHistoryOutputData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ChatWindowWeChatStyle extends JFrame implements ViewChatHistoryOutputBoundary {

    private final InMemoryChatRepository chatRepository;
    private final InMemoryMessageRepository messageRepository;
    private final InMemoryUserRepository userRepository;

    private final SendMessageInteractor sendMessageInteractor;
    private final ViewChatHistoryInteractor viewChatHistoryInteractor;

    private final JPanel chatPanel;
    private final JTextField inputField;
    private final JButton sendButton;

    private final String currentChatId = "chat-1";
    private final String currentUserId = "user-1";

    public ChatWindowWeChatStyle() {
        super("GoChat - Dark WeChat Style");

        chatRepository = new InMemoryChatRepository();
        messageRepository = new InMemoryMessageRepository();
        userRepository = new InMemoryUserRepository();

        sendMessageInteractor = new SendMessageInteractor(messageRepository);
        viewChatHistoryInteractor =
                new ViewChatHistoryInteractor(chatRepository, messageRepository, userRepository, this);

        // our background
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // send button
        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);

        // layout demo
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendAndRefresh());
        inputField.addActionListener(e -> sendAndRefresh());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 600);
        setLocationRelativeTo(null);

        loadChatHistory();
    }

    private void sendAndRefresh() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        sendMessageInteractor.execute(currentChatId, currentUserId, text);
        inputField.setText("");

        loadChatHistory();
    }

    private void loadChatHistory() {
        ViewChatHistoryInputData input = new ViewChatHistoryInputData(currentChatId);
        viewChatHistoryInteractor.execute(input);
    }

    @Override
    public void prepareSuccessView(ViewChatHistoryOutputData outputData) {
        chatPanel.removeAll();

        List<ChatMessageDto> messages = outputData.getMessages();

        for (ChatMessageDto dto : messages) {
            boolean isMe = dto.getSenderUserId().equals(currentUserId);

            JPanel bubble = new JPanel();
            bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
            bubble.setBorder(new EmptyBorder(5, 5, 5, 5));
            // receive ikons!!
            JLabel textLabel = new JLabel("<html>" + dto.getContent()
                    + (isMe ? "  ✓" : "") + "</html>");

            textLabel.setOpaque(true);
            textLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

            if (isMe) {
                bubble.setAlignmentX(Component.RIGHT_ALIGNMENT);
                textLabel.setBackground(new Color(0x95EC69));
            } else {
                bubble.setAlignmentX(Component.LEFT_ALIGNMENT);
                textLabel.setBackground(new Color(60, 60, 60));
                textLabel.setForeground(Color.WHITE);
            }

            bubble.add(textLabel);

            // timeline
            JLabel timeLabel = new JLabel(formatTimestamp(dto.getTimestamp()));
            timeLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
            timeLabel.setForeground(new Color(160, 160, 160));

            if (isMe) {
                timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            } else {
                timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            }

            bubble.add(Box.createVerticalStrut(3));
            bubble.add(timeLabel);

            chatPanel.add(bubble);
            chatPanel.add(Box.createVerticalStrut(8));
        }

        chatPanel.revalidate();
        chatPanel.repaint();
    }

    @Override
    public void prepareNoMessagesView(String chatId) {
        chatPanel.removeAll();
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatWindowWeChatStyle window = new ChatWindowWeChatStyle();
            window.setVisible(true);
        });
    }
}
