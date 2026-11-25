package view.journal;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Standalone preview with sample quests and session-only entries. */
public final class JournalPreview {
    private final CardLayout screenLayout = new CardLayout();
    private final JPanel screenContainer = new JPanel(screenLayout);
    private final Map<String, JournalEntryView.Contents> previewEntriesByQuestName = new HashMap<>();
    private final JournalView journalView = new JournalView(() -> screenLayout.show(screenContainer, "home"),
            this::showQuests);
    private JournalEntryView entryView;

    private JournalPreview() {
        screenContainer.add(createHomePanel(), "home");
        screenContainer.add(journalView, "journal");
        showQuests(JournalView.Tab.COMPLETED);
        screenLayout.show(screenContainer, "journal");
    }

    private JPanel createHomePanel() {
        final JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(JournalStyle.PAPER);
        final GridBagConstraints buttonLayout = new GridBagConstraints();
        buttonLayout.ipadx = 50;
        buttonLayout.ipady = 12;
        homePanel.add(JournalStyle.roundedButton("Journal", JournalStyle.PHOTO_BUTTON,
                () -> screenLayout.show(screenContainer, "journal")), buttonLayout);
        return homePanel;
    }

    private void showQuests(JournalView.Tab tab) {
        List<String> questNames;
        switch (tab) {
            case COMPLETED:
                questNames = Arrays.asList("Visit a new café", "Browse a local bookshop", "Take a sunset walk",
                        "Try a new recipe", "Explore a neighbourhood park", "Visit a farmers market",
                        "Try a new workout", "Sketch a street scene");
                break;
            case IN_PROGRESS:
                questNames = List.of("Find a new weekend trail", "Finish a library book");
                break;
            case CANCELLED:
                questNames = List.of("Visit the night market");
                break;
            default:
                throw new AssertionError(tab);
        }
        journalView.showQuests(questNames.stream()
                .map(questName -> new JournalView.QuestRow(questName, () -> openEntry(questName)))
                .collect(Collectors.toList()));
    }

    private void openEntry(String questName) {
        if (entryView != null) {
            screenContainer.remove(entryView);
        }
        entryView = new JournalEntryView(questName,
                previewEntriesByQuestName.getOrDefault(questName, new JournalEntryView.Contents("", "", null)),
                editedEntryContents -> {
                    previewEntriesByQuestName.put(questName, editedEntryContents);
                    return true;
                }, () -> screenLayout.show(screenContainer, "journal"));
        screenContainer.add(entryView, "entry");
        screenLayout.show(screenContainer, "entry");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JournalPreview preview = new JournalPreview();
            final JFrame frame = new JFrame("Linked Out · Journal Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            preview.screenContainer.setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
            frame.setContentPane(preview.screenContainer);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
