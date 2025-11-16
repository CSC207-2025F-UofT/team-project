package view;

import interface_adapter.repo.InMemoryChatRepository;
import interface_adapter.repo.InMemoryMessageRepository;
import interface_adapter.repo.InMemoryUserRepository;

import use_case.messaging.ChatMessageDto;
import use_case.messaging.SendMessageInteractor;
import use_case.messaging.ViewChatHistoryInputData;
import use_case.messaging.ViewChatHistoryInteractor;
import use_case.messaging.ViewChatHistoryOutputBoundary;
import use_case.messaging.ViewChatHistoryOutputData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Swing chat demo:
 * - Sent (you) on the right, Received (bot) on the left
 * - Auto-replies to simulate incoming messages
 * - Auto-scroll and compact, even spacing
 * - Seeds a chat + two users; uses the actual saved IDs
 */
public class ChatBox extends JFrame implements ViewChatHistoryOutputBoundary {

    // Repositories (in-memory)
    private final InMemoryChatRepository chatRepository;
    private final InMemoryMessageRepository messageRepository;
    private final InMemoryUserRepository userRepository;

    // Use cases
    private final SendMessageInteractor sendMessageInteractor;
    private final ViewChatHistoryInteractor viewChatHistoryInteractor;

    // UI
    private final JPanel chatPanel;
    private final JScrollPane chatScrollPane;
    private final JTextField inputField;
    private final JButton sendButton;

    // IDs (resolved at runtime after seeding)
    private String currentChatId;
    private String currentUserId;
    private String botUserId;

    public ChatBox() {
        super("GoChat Demo");

        // --- repositories
        chatRepository = new InMemoryChatRepository();
        messageRepository = new InMemoryMessageRepository();
        userRepository = new InMemoryUserRepository();

        // --- seed a current user and capture the actual saved id
        goc.chat.entity.User me = new goc.chat.entity.User("user-1", "demo", "demo@example.com", "hash");
        me = userRepository.save(me);             // ensure stored & id resolved
        currentUserId = me.getId();

        // --- seed a chat and make sure current user is a participant
        entity.Chat c = new entity.Chat("chat-1"); // if your Chat auto-ids, that's fine
        c.addParticipant(currentUserId);
        c = chatRepository.save(c);
        currentChatId = c.getId();                // always use id from saved entity

        // --- seed a "bot" user and add it to the same chat
        goc.chat.entity.User bot = new goc.chat.entity.User("user-2", "bot", "bot@example.com", "hash");
        bot = userRepository.save(bot);
        botUserId = bot.getId();
        c.addParticipant(botUserId);
        chatRepository.save(c);                   // re-save with both participants

        // --- use case wiring
        sendMessageInteractor = new SendMessageInteractor(messageRepository);
        viewChatHistoryInteractor =
                new ViewChatHistoryInteractor(chatRepository, messageRepository, userRepository, this);

        // --- UI: chat area
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(30, 30, 30));

        chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setBorder(null);
        chatScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // --- UI: input row
        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(chatScrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // actions
        sendButton.addActionListener(e -> sendAndRefresh());
        inputField.addActionListener(e -> sendAndRefresh());

        // frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 640);
        setLocationRelativeTo(null);

        // initial load
        loadChatHistory();
    }

    // ===================== sending & loading =====================

    private void sendAndRefresh() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        // send my message
        sendMessageInteractor.execute(currentChatId, currentUserId, text);
        inputField.setText("");

        loadChatHistory();
        scrollToBottomSoon();

        // simulate an incoming reply after 600ms
        javax.swing.Timer t = new javax.swing.Timer(600, ev -> {
            ((javax.swing.Timer) ev.getSource()).stop();
            String reply = autoReply(text);
            sendMessageInteractor.execute(currentChatId, botUserId, reply);
            loadChatHistory();
            scrollToBottomSoon();
        });
        t.setRepeats(false);
        t.start();
    }

    private void loadChatHistory() {
        viewChatHistoryInteractor.execute(new ViewChatHistoryInputData(currentChatId));
    }

    // ===================== presenter callbacks =====================

    @Override
    public void prepareSuccessView(ViewChatHistoryOutputData outputData) {
        chatPanel.removeAll();

        List<ChatMessageDto> messages = outputData.getMessages();

        for (ChatMessageDto dto : messages) {
            boolean isMe = dto.getSenderUserId().equals(currentUserId);

            // bubble = message + timestamp stacked vertically
            JPanel bubble = new JPanel();
            bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
            bubble.setOpaque(false);
            bubble.setBorder(new EmptyBorder(6, 10, 4, 10)); // inner padding

            String text = dto.getContent() + (isMe ? "  ✓" : "");
            JLabel msgLabel = new JLabel("<html>" + text + "</html>");
            msgLabel.setOpaque(true);
            msgLabel.setBorder(new EmptyBorder(8, 10, 8, 10));

            if (isMe) {
                msgLabel.setBackground(new Color(0x95EC69)); // sent = green
                msgLabel.setForeground(Color.BLACK);
            } else {
                msgLabel.setBackground(new Color(60, 60, 60)); // received = dark gray
                msgLabel.setForeground(Color.WHITE);
            }
            bubble.add(msgLabel);

            JLabel timeLabel = new JLabel(formatTimestamp(dto.getTimestamp()));
            timeLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
            timeLabel.setForeground(new Color(160, 160, 160));
            bubble.add(Box.createVerticalStrut(2));
            bubble.add(timeLabel);

            // wrapper controls left/right alignment and prevents vertical stretch
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBorder(new EmptyBorder(2, 10, 2, 10)); // small outer padding

            if (isMe) {
                wrapper.add(bubble, BorderLayout.EAST);  // right
            } else {
                wrapper.add(bubble, BorderLayout.WEST);  // left
            }

            // cap row height so BoxLayout won't create huge gaps
            int rowH = bubble.getPreferredSize().height + 4;
            wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowH));
            wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

            chatPanel.add(wrapper);
            chatPanel.add(Box.createVerticalStrut(4));       // compact, consistent gap
        }

        chatPanel.revalidate();
        chatPanel.repaint();
        scrollToBottomSoon(); // always land at latest message
    }

    @Override
    public void prepareNoMessagesView(String chatId) {
        chatPanel.removeAll();
        chatPanel.add(new JLabel("No messages yet."));
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ===================== helpers =====================

    private void scrollToBottomSoon() {
        SwingUtilities.invokeLater(() -> {
            chatPanel.revalidate();
            chatPanel.repaint();
            JScrollBar vb = chatScrollPane.getVerticalScrollBar();
            vb.setValue(vb.getMaximum());
        });
    }

    private String formatTimestamp(Instant timestamp) {
        ZonedDateTime time = timestamp.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return formatter.format(time);
    }

    private String autoReply(String last) {
        String l = last.toLowerCase();
        if (l.contains("hello") || l.contains("hi")) return "hey!";
        if (l.contains("how are")) return "doing great, you?";
        if (l.endsWith("?")) return "good question!";
        return "nice ✓";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatBox window = new ChatBox();
            window.setVisible(true);
        });
    }
}
