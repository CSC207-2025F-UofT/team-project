package view;

import adapters.viewmodel.GameViewModel;
import adapters.viewmodel.GameViewModel.CardViewData;
import adapters.viewmodel.GameViewModel.TileViewData;
import view.GameArtwork.Portrait;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Swing match screen. Create and update it on the Swing event dispatch thread. */
public final class GameView extends JPanel {
    private static final int CARD_BUTTON_PADDING = 12;
    private static final int SCROLLBAR_SPACE = 16;

    private final GameArtwork artwork = new GameArtwork();
    private final JLabel gameStatus = GameViewStyle.label("", GameViewStyle.TURN_FONT, GameViewStyle.TEXT);
    private final JLabel timer = GameViewStyle.label("", GameViewStyle.TIMER_FONT, GameViewStyle.GOLD);
    private final BoardPanel boardPanel = new BoardPanel((position, spellIndex) ->
            inspect(new TileInspection(position, spellIndex)));
    private Portrait previewPortrait;
    private final JLabel cardPreview = new JLabel() {
        @Override
        protected void paintComponent(Graphics graphics) {
            if (previewPortrait != null) {
                previewPortrait.paint(graphics, 0, 0, getWidth(), getHeight());
            }
        }
    };
    private final JPanel relatedCards = new JPanel(new FlowLayout(FlowLayout.CENTER, GameViewStyle.CARD_GAP, 0));
    private final JScrollPane relatedScroll = horizontalScroll(relatedCards);
    private final JPanel handCards = new JPanel();
    private final List<JButton> handButtons = new ArrayList<>();
    private final JScrollPane handScroll = horizontalScroll(handCards);
    private final JButton drawPile = cardButton();
    private final JButton discardPile = cardButton();
    private final JLabel drawTotal = GameViewStyle.label("", GameViewStyle.COUNT_FONT, GameViewStyle.MUTED);
    private final JLabel discardTotal = GameViewStyle.label("", GameViewStyle.COUNT_FONT, GameViewStyle.MUTED);
    private GameViewModel model;
    private Inspection inspection;

    public GameView() {
        super(new BorderLayout(0, GameViewStyle.GAP));
        setBackground(GameViewStyle.BACKGROUND);
        int padding = GameViewStyle.PAGE_PADDING;
        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        add(createPlayArea(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
        render(GameViewModel.empty());
    }

    /** Renders a presentation snapshot on the Swing event dispatch thread. */
    public void render(GameViewModel snapshot) {
        GameViewModel previous = model;
        model = Objects.requireNonNull(snapshot);
        gameStatus.setText(model.gameStatus());
        int remaining = Math.max(0, model.remainingTurnSeconds());
        timer.setText(String.format(Locale.ROOT, "%02d:%02d", remaining / 60, remaining % 60));

        boolean boardChanged = previous == null || !previous.board().equals(model.board());
        boolean handChanged = previous == null || !previous.hand().equals(model.hand());
        if (boardChanged) {
            boardPanel.showBoard(model, artwork);
        }
        if (handChanged) {
            showHand(model.hand());
        }
        if ((boardChanged && inspection instanceof TileInspection)
                || (handChanged && inspection instanceof HandInspection)) {
            refreshInspection();
        }
        if (previous == null || previous.drawCount() != model.drawCount()
                || previous.discardCount() != model.discardCount()
                || !Objects.equals(previous.topDiscard(), model.topDiscard())) {
            showPiles(model.drawCount(), model.discardCount(), model.topDiscard());
        }
    }

    /** Displays an image across the board. Pass null to use the plain grid. */
    public void setBoardBackground(BufferedImage image) {
        boardPanel.setArtwork(image);
    }

    /** Replaces the range or movement highlights. Points use zero-based column and row. */
    public void setHighlightedTiles(Collection<Point> tiles) {
        boardPanel.setHighlightedTiles(tiles);
    }

    /** Clears the range or movement highlights. */
    public void clearHighlights() {
        setHighlightedTiles(List.of());
    }

    /** Draws a persistent arrow between two tile centers. Points use zero-based column and row. */
    public void drawArrow(Point from, Point to) {
        boardPanel.effects().drawArrow(from, to);
    }

    /** Clears every persistent arrow. */
    public void clearArrows() {
        boardPanel.effects().clearArrows();
    }

    /** Animates a projectile between two tile centers using the default duration. */
    public void animateTrajectory(Point from, Point to) {
        animateTrajectory(from, to, GameViewStyle.TRAJECTORY_MILLIS);
    }

    /** Animates a projectile between two tile centers over a positive duration in milliseconds. */
    public void animateTrajectory(Point from, Point to, int durationMillis) {
        boardPanel.effects().animateTrajectory(from, to, durationMillis);
    }

    /** Flashes the given tiles using the default duration. Points use zero-based column and row. */
    public void flashTiles(Collection<Point> tiles) {
        flashTiles(tiles, GameViewStyle.HIT_FLASH_MILLIS);
    }

    /** Flashes the given tiles and fades over a positive duration in milliseconds. */
    public void flashTiles(Collection<Point> tiles, int durationMillis) {
        boardPanel.effects().flashTiles(tiles, durationMillis);
    }

    private JPanel createPlayArea() {
        JPanel playArea = new JPanel(new BorderLayout(GameViewStyle.GAP, 0));
        playArea.setOpaque(false);

        JPanel boardFrame = GameViewStyle.section(new BorderLayout(0, GameViewStyle.CARD_GAP));
        JPanel statusDisplay = new JPanel();
        statusDisplay.setOpaque(false);
        statusDisplay.setLayout(new BoxLayout(statusDisplay, BoxLayout.X_AXIS));
        gameStatus.setMinimumSize(new Dimension(0, 0));
        statusDisplay.add(gameStatus);
        statusDisplay.add(Box.createHorizontalStrut(GameViewStyle.GAP));
        statusDisplay.add(timer);
        statusDisplay.add(Box.createHorizontalGlue());
        boardFrame.add(statusDisplay, BorderLayout.NORTH);
        boardFrame.add(boardPanel, BorderLayout.CENTER);
        playArea.add(boardFrame, BorderLayout.CENTER);

        JPanel preview = GameViewStyle.section(new BorderLayout(0, GameViewStyle.GAP));
        preview.getAccessibleContext().setAccessibleName("Card inspection");
        preview.setPreferredSize(new Dimension(GameViewStyle.PREVIEW_WIDTH, 0));
        preview.add(cardPreview, BorderLayout.CENTER);
        relatedCards.setOpaque(false);
        relatedScroll.setPreferredSize(new Dimension(0, GameViewStyle.SPELL_PREVIEW_HEIGHT + SCROLLBAR_SPACE));
        relatedScroll.setVisible(false);
        preview.add(relatedScroll, BorderLayout.SOUTH);
        playArea.add(preview, BorderLayout.EAST);

        return playArea;
    }

    private JPanel createFooter() {
        JPanel footer = GameViewStyle.section(new BorderLayout(GameViewStyle.GAP, 0));
        JPanel cards = new JPanel(new BorderLayout(GameViewStyle.GAP, 0));
        cards.setOpaque(false);

        handCards.setOpaque(false);
        handCards.setLayout(new BoxLayout(handCards, BoxLayout.X_AXIS));
        handScroll.setPreferredSize(new Dimension(0, drawPile.getPreferredSize().height + SCROLLBAR_SPACE));
        handScroll.getAccessibleContext().setAccessibleName("Your hand");
        cards.add(handScroll, BorderLayout.CENTER);

        JPanel piles = new JPanel(new FlowLayout(FlowLayout.LEFT, GameViewStyle.CARD_GAP, 0));
        piles.setOpaque(false);
        piles.add(pileSlot(drawPile, drawTotal));
        piles.add(pileSlot(discardPile, discardTotal));
        drawPile.addActionListener(event -> {
            // TODO: Route draw-pile clicks if the draw use case allows manual draws.
        });
        discardPile.addActionListener(event -> {
            // TODO: Open the discard-pile view when the discard controller is available.
        });
        cards.add(piles, BorderLayout.EAST);
        footer.add(cards, BorderLayout.CENTER);

        JButton pass = GameViewStyle.button("Pass", true);
        pass.setPreferredSize(new Dimension(88, 36));
        pass.addActionListener(event -> {
            // TODO: Forward Pass to the end-turn or cast-response controller for the current phase.
        });
        JPanel actions = new JPanel(new BorderLayout());
        actions.setOpaque(false);
        actions.add(pass, BorderLayout.SOUTH);
        footer.add(actions, BorderLayout.EAST);

        return footer;
    }

    private void showHand(List<CardViewData> cards) {
        while (handButtons.size() > cards.size()) {
            handCards.remove(handButtons.removeLast());
            if (!handButtons.isEmpty()) {
                handCards.remove(handCards.getComponentCount() - 1);
            }
        }
        for (int index = 0; index < cards.size(); index++) {
            JButton button = index < handButtons.size() ? handButtons.get(index) : addHandButton(index);
            CardViewData card = cards.get(index);
            Portrait portrait = artwork.card(card);
            button.setIcon(portrait.icon(GameViewStyle.HAND_CARD_HEIGHT));
            describeCard(button, card, portrait);
        }
        handCards.revalidate();
        handCards.repaint();
    }

    private JButton addHandButton(int handIndex) {
        JButton button = cardButton();
        GameViewStyle.inspectOnHover(button, () -> inspect(new HandInspection(handIndex)));
        button.addActionListener(event -> {
            // TODO: Forward the hand index to the play-card or cast-response controller.
        });
        if (!handButtons.isEmpty()) {
            handCards.add(Box.createHorizontalStrut(GameViewStyle.CARD_GAP));
        }
        handButtons.add(button);
        handCards.add(button);
        return button;
    }

    private void showPiles(int drawCount, int discardCount, CardViewData topDiscard) {
        Portrait draw = drawCount > 0 ? artwork.cardBack() : artwork.emptyPile();
        updatePile(drawPile, drawTotal, "Draw pile", drawCount, draw);
        Portrait discard = topDiscard == null ? artwork.emptyPile() : artwork.card(topDiscard);
        updatePile(discardPile, discardTotal, "Discard pile", discardCount, discard);
        if (topDiscard != null) {
            String description = discardPile.getAccessibleContext().getAccessibleName()
                    + " · " + topDiscard.accessibleDescription();
            discardPile.getAccessibleContext().setAccessibleName(description);
            if (discard.image() == null) {
                discardPile.setToolTipText(description);
            }
        }
    }

    private void inspect(Inspection target) {
        if (Objects.equals(inspection, target)) {
            return;
        }
        inspection = target;
        relatedScroll.getHorizontalScrollBar().setValue(0);
        refreshInspection();
    }

    private void refreshInspection() {
        switch (inspection) {
            case TileInspection(var position, var spellIndex) -> showTileInspection(position, spellIndex);
            case HandInspection(var index) -> {
                if (index < model.hand().size()) {
                    showInspection(model.hand().get(index), List.of());
                } else {
                    inspect(null);
                }
            }
            case null -> showInspection(null, List.of());
        }
    }

    private void showTileInspection(Point position, int previousSpell) {
        TileViewData tile = model.tileAt(position.x, position.y);
        int spellIndex = boardPanel.spellUnderPointer(position).orElse(previousSpell);
        if (spellIndex >= tile.spells().size()) {
            spellIndex = BoardPanel.NO_HOVERED_SPELL;
        }
        if (spellIndex != previousSpell) {
            inspection = new TileInspection(position, spellIndex);
            relatedScroll.getHorizontalScrollBar().setValue(0);
        }
        if (spellIndex == BoardPanel.NO_HOVERED_SPELL) {
            showInspection(tile.occupant(), tile.spells());
            return;
        }

        List<CardViewData> related = new ArrayList<>(tile.spells());
        CardViewData hovered = related.remove(spellIndex);
        if (tile.occupant() != null) {
            related.addFirst(tile.occupant());
        }
        showInspection(hovered, related);
    }

    private void showInspection(CardViewData card, List<CardViewData> related) {
        previewPortrait = card == null ? null : artwork.card(card);
        if (card == null) {
            cardPreview.getAccessibleContext().setAccessibleName(null);
            cardPreview.setToolTipText(null);
        } else {
            describeCard(cardPreview, card, previewPortrait);
        }

        relatedCards.removeAll();
        for (CardViewData relatedCard : related) {
            Portrait portrait = artwork.card(relatedCard);
            JLabel relatedPreview = new JLabel(portrait.icon(GameViewStyle.SPELL_PREVIEW_HEIGHT));
            describeCard(relatedPreview, relatedCard, portrait);
            relatedCards.add(relatedPreview);
        }
        relatedScroll.setVisible(!related.isEmpty());
        relatedCards.revalidate();
        relatedCards.repaint();
        cardPreview.repaint();
    }

    private static void describeCard(JComponent component, CardViewData card, Portrait portrait) {
        String description = card.accessibleDescription();
        component.getAccessibleContext().setAccessibleName(description);
        component.setToolTipText(portrait.image() == null ? description : null);
    }

    private static JPanel pileSlot(JButton button, JLabel count) {
        JPanel slot = new JPanel(new BorderLayout(0, 2));
        slot.setOpaque(false);
        count.setHorizontalAlignment(SwingConstants.CENTER);
        slot.add(button, BorderLayout.CENTER);
        slot.add(count, BorderLayout.SOUTH);
        return slot;
    }

    private static void updatePile(JButton button, JLabel count, String name, int total, Portrait portrait) {
        String description = name + " · " + total + (total == 1 ? " card" : " cards");
        button.setIcon(portrait.icon(GameViewStyle.HAND_CARD_HEIGHT));
        button.getAccessibleContext().setAccessibleName(description);
        button.setToolTipText(description);
        count.setText(Integer.toString(total));
    }

    private static JScrollPane horizontalScroll(JPanel content) {
        return GameViewStyle.scroll(content, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private static JButton cardButton() {
        JButton button = GameViewStyle.button("", false);
        button.setAlignmentY(Component.TOP_ALIGNMENT);
        int height = GameViewStyle.HAND_CARD_HEIGHT;
        Dimension size = new Dimension(Portrait.cardWidth(height) + CARD_BUTTON_PADDING, height + CARD_BUTTON_PADDING);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        return button;
    }

    private sealed interface Inspection permits TileInspection, HandInspection {
    }

    private record TileInspection(Point position, int spellIndex) implements Inspection {
    }

    private record HandInspection(int index) implements Inspection {
    }
}
