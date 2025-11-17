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

    // Temporary UI-only store: messageId -> emoji
    private final java.util.Map<String, String> localReactions = new java.util.HashMap<>();
    // The emojis for reaction
    private static final String[] EMOJIS = new String[] {
            "👍", "\uD83E\uDE77", "😂", "😮", "😢", "🔥", "👏", "🎉", "🤔", "😡"
    };

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

            // bubble (message + timestamp)
            JPanel bubble = new JPanel();
            bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
            bubble.setOpaque(false);
            bubble.setBorder(new EmptyBorder(6, 10, 4, 10));

            String text = dto.getContent() + (isMe ? "  ✓" : "");
            JLabel msgLabel = new JLabel("<html>" + text + "</html>");
            msgLabel.setOpaque(true);
            msgLabel.setBorder(new EmptyBorder(8, 10, 8, 10));

            if (isMe) {
                msgLabel.setBackground(new Color(0x95EC69));    // sent
                msgLabel.setForeground(Color.BLACK);
            } else {
                msgLabel.setBackground(new Color(60, 60, 60));  // received
                msgLabel.setForeground(Color.WHITE);
            }
            // row: message label in CENTER, optional reaction on the RIGHT
            JPanel msgRow = new JPanel(new BorderLayout());
            msgRow.setOpaque(false);
            msgRow.add(msgLabel, BorderLayout.CENTER);

            // Only show reactions on OTHER people's messages
            if (!isMe) {
                String r = localReactions.get(getMessageId(dto));
                if (r != null) {
                    JLabel reaction = new JLabel(r);
                    reaction.setBorder(new EmptyBorder(0, 6, 0, 0)); // tiny left pad
                    msgRow.add(reaction, BorderLayout.EAST);
                }
            }

            bubble.add(msgRow);

            JLabel timeLabel = new JLabel(formatTimestamp(dto.getTimestamp()));
            timeLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
            timeLabel.setForeground(new Color(160, 160, 160));
            bubble.add(Box.createVerticalStrut(2));
            bubble.add(timeLabel);

            // action button + popup
            JButton actionBtn = makeActionButton();
            JPopupMenu menu = buildActionMenu(isMe, dto, actionBtn);
            actionBtn.addActionListener(e -> menu.show(actionBtn, 0, actionBtn.getHeight()));

            // side-by-side panel holding (button,bubble) or (bubble,button)
            JPanel side = new JPanel();
            side.setOpaque(false);
            side.setLayout(new BoxLayout(side, BoxLayout.X_AXIS));

            if (isMe) {
                side.add(bubble);
                side.add(Box.createHorizontalStrut(6));
                side.add(actionBtn);            // your messages: bubble on right, button to its right
            } else {
                side.add(actionBtn);            // others' messages: button then bubble (left side)
                side.add(Box.createHorizontalStrut(6));
                side.add(bubble);
            }

            // wrapper: keeps left/right alignment and prevents tall stretching
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBorder(new EmptyBorder(2, 10, 2, 10));

            if (isMe) wrapper.add(side, BorderLayout.EAST);
            else      wrapper.add(side, BorderLayout.WEST);

            // cap row height for even vertical spacing
            int rowH = side.getPreferredSize().height + 4;
            wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowH));
            wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

            chatPanel.add(wrapper);
            chatPanel.add(Box.createVerticalStrut(4));

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

    // Small, neutral button used next to each bubble
    private JButton makeActionButton() {
        JButton b = new JButton("⋯");
        b.setMargin(new Insets(2, 8, 2, 8));
        b.setFocusable(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        b.setBackground(new Color(50, 50, 50));
        b.setForeground(new Color(220, 220, 220));
        return b;
    }

    // Build the popup menu; for now handlers are UI-only placeholders
    private JPopupMenu buildActionMenu(boolean isMe, ChatMessageDto dto, JButton anchor) {
        JPopupMenu menu = new JPopupMenu();

        if (isMe) {
            JMenuItem delete = new JMenuItem("Delete");
            delete.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Delete (UI only for now)"));
            menu.add(delete);

            JMenuItem reply = new JMenuItem("Reply");
            reply.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Reply (UI only for now)"));
            menu.add(reply);
        } else {
            JMenuItem reply = new JMenuItem("Reply");
            reply.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Reply (UI only for now)"));
            menu.add(reply);

            JMenuItem report = new JMenuItem("Report");
            report.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "Report (UI only for now)"));
            menu.add(report);

            JMenuItem react = new JMenuItem("React");
            react.addActionListener(e -> showReactionChooser(dto, anchor)); // ⭐ open emoji picker
            menu.add(react);

            JMenuItem clear = new JMenuItem("Clear reaction");
            clear.addActionListener(e -> {
                localReactions.remove(getMessageId(dto));
                loadChatHistory();
                scrollToBottomSoon();
            });
            menu.add(clear);
        }

        menu.addSeparator();
        JMenuItem cancel = new JMenuItem("Cancel");
        cancel.addActionListener(e -> menu.setVisible(false));
        menu.add(cancel);

        return menu;
    }

    private void showReactionChooser(ChatMessageDto dto, Component anchor) {
        JPopupMenu menu = new JPopupMenu();
        for (String e : EMOJIS) {
            JMenuItem it = new JMenuItem(e);
            it.addActionListener(a -> {
                String msgId = getMessageId(dto);
                localReactions.put(msgId, e);     // save UI-only reaction
                // re-render messages so the emoji appears in-bubble
                loadChatHistory();
                scrollToBottomSoon();
            });
            menu.add(it);
        }
        menu.addSeparator();
        JMenuItem cancel = new JMenuItem("Cancel");
        cancel.addActionListener(a -> menu.setVisible(false));
        menu.add(cancel);

        menu.show(anchor, 0, anchor.getHeight());
    }

    // utility to read the dto id (rename if your DTO uses a different getter)
    private String getMessageId(ChatMessageDto dto) {
        // return dto.getId();  // ← if your dto uses getId()
        return dto.getMessageId(); // ← if your dto uses getMessageId()
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatBox window = new ChatBox();
            window.setVisible(true);
        });
    }
}
