package view.journal;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/** Displays quest rows supplied for the selected journal tab. */
public final class JournalView extends JPanel {
    public enum Tab {
        COMPLETED("Completed"), IN_PROGRESS("In Progress"), CANCELLED("Cancelled");

        private final String label;

        Tab(String label) {
            this.label = label;
        }
    }

    public static final class QuestRow {
        private final String questName;
        private final Runnable onViewEntry;

        public QuestRow(String questName, Runnable onViewEntry) {
            this.questName = questName;
            this.onViewEntry = onViewEntry;
        }
    }

    private final JPanel questCardsPanel = new JPanel();
    private final JScrollPane questScrollPane = new JScrollPane(questCardsPanel);
    private double pendingScrollPixels;

    public JournalView(final Runnable returnToHome, final Consumer<Tab> selectJournalTab) {
        setLayout(null);
        setBackground(JournalStyle.PAPER);
        setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
        JournalStyle.addPageHeader(this, "Journal", returnToHome);
        add(createStatusTabStrip(selectJournalTab));
        configureQuestScrollPane();
        add(questScrollPane);
    }

    private static JLabel createStatusTabStrip(final Consumer<Tab> selectJournalTab) {
        final JLabel statusTabStrip = new JLabel(JournalStyle.loadScaledIcon("tabs", JournalStyle.WIDTH, 40));
        statusTabStrip.setLayout(new GridLayout(1, Tab.values().length));
        statusTabStrip.setBounds(0, 72, JournalStyle.WIDTH, 40);
        final ButtonGroup statusButtonGroup = new ButtonGroup();
        for (Tab journalTab : Tab.values()) {
            final JToggleButton statusTabButton = new JToggleButton(journalTab.label);
            statusTabButton.setContentAreaFilled(false);
            statusTabButton.setBorderPainted(false);
            statusTabButton.setFocusPainted(false);
            statusTabButton.setSelected(journalTab == Tab.COMPLETED);
            statusTabButton.setFont(JournalStyle.UI_FONT
                    .deriveFont(statusTabButton.isSelected() ? Font.BOLD : Font.PLAIN, 12f));
            statusTabButton.addItemListener(selectionChange -> statusTabButton.setFont(statusTabButton.getFont()
                    .deriveFont(statusTabButton.isSelected() ? Font.BOLD : Font.PLAIN)));
            statusTabButton.addActionListener(tabActivation -> selectJournalTab.accept(journalTab));
            statusButtonGroup.add(statusTabButton);
            statusTabStrip.add(statusTabButton);
        }
        return statusTabStrip;
    }

    private void configureQuestScrollPane() {
        questCardsPanel.setLayout(new BoxLayout(questCardsPanel, BoxLayout.Y_AXIS));
        questCardsPanel.setBackground(JournalStyle.PAPER);
        questCardsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        // Keep the scrollbar in its own gutter even on platforms with overlay scrollbars.
        questScrollPane.setUI(new BasicScrollPaneUI());
        questScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI());
        questScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(16, 0));
        questScrollPane.setBorder(null);
        questScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        questScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        questScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        questScrollPane.setFocusable(true);
        questScrollPane.getAccessibleContext().setAccessibleName("Journal quest list");
        InputMap scrollKeyBindings = questScrollPane.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        scrollKeyBindings.put(KeyStroke.getKeyStroke("UP"), "unitScrollUp");
        scrollKeyBindings.put(KeyStroke.getKeyStroke("DOWN"), "unitScrollDown");
        scrollKeyBindings.put(KeyStroke.getKeyStroke("PAGE_UP"), "scrollUp");
        scrollKeyBindings.put(KeyStroke.getKeyStroke("PAGE_DOWN"), "scrollDown");
        scrollKeyBindings.put(KeyStroke.getKeyStroke("HOME"), "scrollHome");
        scrollKeyBindings.put(KeyStroke.getKeyStroke("END"), "scrollEnd");
        // Swing's default wheel handler uses whole notches, losing small trackpad movements.
        questScrollPane.setWheelScrollingEnabled(false);
        questScrollPane.addMouseWheelListener(this::scrollWithWheel);
    }

    private void scrollWithWheel(MouseWheelEvent event) {
        double wheelRotation = event.getPreciseWheelRotation();
        if (wheelRotation == 0 || event.isShiftDown() || event.isControlDown() || event.isMetaDown()) {
            return;
        }
        JScrollBar verticalScrollBar = questScrollPane.getVerticalScrollBar();
        int direction = wheelRotation < 0 ? -1 : 1;
        int pixelsPerWheelNotch = event.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL
                ? verticalScrollBar.getBlockIncrement(direction)
                : event.getScrollAmount() * verticalScrollBar.getUnitIncrement(direction);
        pendingScrollPixels += wheelRotation * pixelsPerWheelNotch;
        int wholePixelsToScroll = (int) pendingScrollPixels;
        pendingScrollPixels -= wholePixelsToScroll;
        verticalScrollBar.setValue(verticalScrollBar.getValue() + wholePixelsToScroll);
        event.consume();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        questScrollPane.setBounds(0, 120, getWidth(), Math.max(0, getHeight() - 120));
    }

    public void showQuests(List<QuestRow> quests) {
        pendingScrollPixels = 0;
        questCardsPanel.removeAll();
        if (quests.isEmpty()) {
            JLabel emptyStateLabel = JournalStyle.label("No quests in this category yet.", JournalStyle.UI_FONT.deriveFont(14f));
            emptyStateLabel.setAlignmentX(CENTER_ALIGNMENT);
            questCardsPanel.add(Box.createVerticalStrut(24));
            questCardsPanel.add(emptyStateLabel);
        }
        for (int questIndex = 0; questIndex < quests.size(); questIndex++) {
            if (questIndex > 0) {
                questCardsPanel.add(Box.createVerticalStrut(12));
            }
            questCardsPanel.add(createQuestCard(quests.get(questIndex)));
        }
        questScrollPane.getViewport().setViewPosition(new Point());
        questCardsPanel.revalidate();
        questCardsPanel.repaint();
    }

    private JPanel createQuestCard(QuestRow quest) {
        JPanel questCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics graphics) {
                JournalStyle.paintRounded(graphics, JournalStyle.CARD_GREEN, getWidth(), getHeight(), 36);
            }
        };
        questCard.setOpaque(false);
        questCard.setAlignmentX(CENTER_ALIGNMENT);
        Dimension cardSize = new Dimension(378, 98);
        questCard.setMinimumSize(cardSize);
        questCard.setPreferredSize(cardSize);
        questCard.setMaximumSize(cardSize);

        JLabel questNameLabel = JournalStyle.label(quest.questName, JournalStyle.UI_FONT.deriveFont(14f));
        questNameLabel.setToolTipText(quest.questName);
        questNameLabel.setBounds(12, 12, 354, 39);
        questCard.add(questNameLabel);

        JButton viewEntryButton = JournalStyle.roundedButton("View Entry", JournalStyle.PAPER, quest.onViewEntry);
        viewEntryButton.setBounds(145, 64, 88, 24);
        questCard.add(viewEntryButton);
        return questCard;
    }
}
