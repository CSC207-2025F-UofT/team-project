package interface_adapter.ingame.InGameTestView;

import entity.card.Card;
import interface_adapter.ingame.CardImageProvider;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A mutable, presentation-only Swing UNO table. It displays {@link GameTestViewState} data and has
 * no dependencies on game rules, controllers, or use cases.
 */
public final class InGameTestView extends JPanel {
    private static final int DESIGN_WIDTH = 1080;
    private static final int DESIGN_HEIGHT = 610;
    private static final int CARD_WIDTH = 60;
    private static final int CARD_HEIGHT = 88;
    private static final int AVATAR_WIDTH = 46;
    private static final int AVATAR_HEIGHT = 44;
    private static final int MAX_VISIBLE_PILE_CARDS = 3;
    private CardImageProvider imgPathProvider = new CardImageProvider();

    private final JButton declareUnoButton = new JButton("Declare UNO");
    private final BufferedImage tableBackground = loadRequiredImage(
            "/backgrounds/inGame_background.png");
    private final BufferedImage playerAvatar = loadRequiredImage(
            "/player_avatar.png");
    private final BufferedImage cardBack = loadRequiredImage(
            "/cards/card_back.png");
    private final Map<String, BufferedImage> cardImages = new HashMap<>();
    private GameTestViewState currentGameState = new GameTestViewState();

    public InGameTestView() {
        setLayout(null);
        setOpaque(true);
        setPreferredSize(new Dimension(DESIGN_WIDTH, DESIGN_HEIGHT));
        setBackground(new Color(246, 241, 229));

        declareUnoButton.setFocusable(false);
        declareUnoButton.setBackground(new Color(225, 56, 45));
        declareUnoButton.setForeground(Color.WHITE);
        declareUnoButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        declareUnoButton.setToolTipText("Visual-only demo button");
        declareUnoButton.addActionListener(event -> declareUnoButton.setText("UNO declared!"));
        add(declareUnoButton);
    }

    /**
     * Replaces the displayed data and schedules a redraw. The supplied mutable state is copied,
     * so later changes become visible only after the caller invokes this method again.
     *
     * @param gameState presentation data to show
     */
    public void updateGameState(GameTestViewState gameState) {
        GameTestViewState stateSnapshot = new GameTestViewState(Objects.requireNonNull(gameState, "gameState"));
        if (SwingUtilities.isEventDispatchThread()) {
            applyGameState(stateSnapshot);
        } else {
            SwingUtilities.invokeLater(() -> applyGameState(stateSnapshot));
        }
    }

    @Override
    public void doLayout() {
        declareUnoButton.setBounds(
                boardX(43), boardY(496),
                Math.max(110, boardLength(140)), Math.max(42, boardLength(41)));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.translate(boardOffsetX(), boardOffsetY());
        g.scale(scale(), scale());

        g.drawImage(tableBackground, 0, 0, DESIGN_WIDTH, DESIGN_HEIGHT, null);
        drawOpponents(g);
        drawPiles(g);
        drawCurrentPlayerHand(g);
        drawCurrentPlayer(g);
        g.dispose();
    }

    private void applyGameState(GameTestViewState stateSnapshot) {
        currentGameState = stateSnapshot;
        preloadCardImages(stateSnapshot.getCurrentPlayerHand());
        if (stateSnapshot.getDiscardTopCard() != null) {
            cardImage(stateSnapshot.getDiscardTopCard());
        }
        revalidate();
        repaint();
    }

    private void drawOpponents(Graphics2D g) {
        drawAvatar(g, 517, 3);
        drawTitle(g, currentGameState.getTopOpponentName(), 540, 59);
        drawTopHand(g, currentGameState.getTopOpponentCardCount());

        drawAvatar(g, 16, 279);
        drawTitle(g, currentGameState.getLeftOpponentName(), 87, 333);
        drawSideHand(g, 79, 213, -90, currentGameState.getLeftOpponentCardCount());

        drawAvatar(g, 1018, 279);
        drawTitle(g, currentGameState.getRightOpponentName(), 994, 333);
        drawSideHand(g, 1001, 213, 90, currentGameState.getRightOpponentCardCount());
    }

    private void drawPiles(Graphics2D g) {
        drawBackPile(g, 373, 252, currentGameState.getDrawPileCardCount());
        drawCaption(g, "DRAW", 409, 370);

        drawDiscardPile(g, 585, 252, currentGameState.getDiscardTopCard());
        drawCaption(g, "DISCARD", 609, 370);
    }

    private void drawTopHand(Graphics2D g, int cardCount) {
        if (cardCount == 0) {
            return;
        }
        int spacing = compressedSpacing(cardCount, 46, 500, CARD_WIDTH);
        int handWidth = CARD_WIDTH + spacing * (cardCount - 1);
        int startX = (DESIGN_WIDTH - handWidth) / 2;
        for (int i = 0; i < cardCount; i++) {
            drawCard(g, cardBack, startX + i * spacing, 75, 0);
        }
    }

    private void drawSideHand(Graphics2D g, int x, int y, int degrees, int cardCount) {
        if (cardCount == 0) {
            return;
        }
        int spacing = compressedSpacing(cardCount, 35, 250, CARD_WIDTH);
        for (int i = 0; i < cardCount; i++) {
            drawCard(g, cardBack, x, y + i * spacing, degrees);
        }
    }

    private void drawBackPile(Graphics2D g, int x, int y, int cardCount) {
        int visibleCardCount = Math.min(cardCount, MAX_VISIBLE_PILE_CARDS);
        for (int i = visibleCardCount - 1; i >= 0; i--) {
            drawCard(g, cardBack, x - i * 5, y - i * 4, -4);
        }
    }

    private void drawDiscardPile(Graphics2D g, int x, int y, Card card) {
        BufferedImage cardImg = cardImage(card);
        if (cardImg != null) {
            drawCard(g, cardImg, x, y, -4);
        }
    }

    private void drawCurrentPlayerHand(Graphics2D g) {
        List<Card> hand = currentGameState.getCurrentPlayerHand();
        int cardCount = hand.size();
        if (cardCount == 0) {
            return;
        }
        int spacing = compressedSpacing(cardCount, 64, 720, CARD_WIDTH);
        int handWidth = CARD_WIDTH + spacing * (cardCount - 1);
        int startX = (DESIGN_WIDTH - handWidth) / 2;
        int selectedCardIndex = cardCount / 2;
        for (int i = 0; i < cardCount; i++) {
            BufferedImage card = imgPathProvider.getImage(hand.get(i));
            if (card != null) {
                int lift = i == selectedCardIndex ? 14 : 0;
                drawCard(g, card, startX + i * spacing, 486 - lift, 0);
            }
        }
    }

    private void drawCurrentPlayer(Graphics2D g) {
        drawAvatar(g, 927, 496);
        drawTitle(g, currentGameState.getCurrentPlayerName(), 950, 565);
    }

    private void drawCard(Graphics2D g, Image cardImage, int x, int y, int degrees) {
        Graphics2D card = (Graphics2D) g.create();
        card.translate(x + CARD_WIDTH / 2.0, y + CARD_HEIGHT / 2.0);
        card.rotate(Math.toRadians(degrees));
        card.translate(-CARD_WIDTH / 2.0, -CARD_HEIGHT / 2.0);
        card.drawImage(cardImage, 0, 0, CARD_WIDTH, CARD_HEIGHT, null);
        card.dispose();
    }

    private void drawAvatar(Graphics2D g, int x, int y) {
        g.drawImage(playerAvatar, x, y, AVATAR_WIDTH, AVATAR_HEIGHT, null);
    }

    private void drawTitle(Graphics2D g, String text, int x, int y) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        int width = g.getFontMetrics().stringWidth(text);
        g.setColor(Color.WHITE);
        g.drawString(text, x - width / 2, y);
    }

    private void drawCaption(Graphics2D g, String text, int x, int y) {
        g.setColor(new Color(20, 59, 106));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, x - width / 2, y);
    }

    private void preloadCardImages(List<Card> cards) {
        for (Card card : cards) {
            // TODO: fix this
//            cardImage(card);
        }
    }

    private BufferedImage cardImage(Card card) {
        return imgPathProvider.getImage(card);
    }

    private static BufferedImage loadRequiredImage(String resourcePath) {
        BufferedImage image = loadOptionalImage(resourcePath);
        if (image == null) {
            throw new IllegalStateException("Required UI image is missing: " + resourcePath);
        }
        return image;
    }

    private static BufferedImage loadOptionalImage(String resourcePath) {
        try (InputStream stream = InGameTestView.class.getResourceAsStream(resourcePath)) {
            return stream == null ? null : ImageIO.read(stream);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load UI image: " + resourcePath, exception);
        }
    }

    private static int compressedSpacing(int cardCount, int preferredSpacing, int maximumSpan, int cardWidth) {
        if (cardCount < 2) {
            return preferredSpacing;
        }
        return Math.max(10, Math.min(preferredSpacing, (maximumSpan - cardWidth) / (cardCount - 1)));
    }

    private double scale() {
        return Math.min((double) getWidth() / DESIGN_WIDTH, (double) getHeight() / DESIGN_HEIGHT);
    }

    private int boardOffsetX() {
        return (getWidth() - (int) Math.round(DESIGN_WIDTH * scale())) / 2;
    }

    private int boardOffsetY() {
        return (getHeight() - (int) Math.round(DESIGN_HEIGHT * scale())) / 2;
    }

    private int boardX(int designX) {
        return boardOffsetX() + boardLength(designX);
    }

    private int boardY(int designY) {
        return boardOffsetY() + boardLength(designY);
    }

    private int boardLength(int designLength) {
        return (int) Math.round(designLength * scale());
    }
}
