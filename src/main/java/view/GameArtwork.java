package view;

import adapters.viewmodel.GameViewModel.CardType;
import adapters.viewmodel.GameViewModel.CardViewData;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/** Loads the shipped board and cards as classpath resources. */
final class GameArtwork {
    private static final String IMAGE_ROOT = "/images/";
    private static final double CARD_ASPECT_RATIO = 744.0 / 1039;
    private static final Pattern WORD_START = Pattern.compile("(^|_)(\\p{L})");
    private final Map<CardKey, Portrait> cards = new HashMap<>();
    private final Portrait cardBack = new Portrait(read("cards/back/Back.png", "cards/back.png"));
    private final Portrait emptyPile = new Portrait(null);

    static BufferedImage boardBackground() {
        return read("board/bg.png", "board/background.png");
    }

    Portrait card(CardViewData card) {
        Portrait portrait = cards.computeIfAbsent(new CardKey(card.type(), card.name()), GameArtwork::loadCard);
        if (card.type() == CardType.CHARACTER && card.currentHp() != null) {
            return new Portrait(portrait.image(), card.currentHp());
        }
        return portrait;
    }

    Portrait cardBack() {
        return cardBack;
    }

    Portrait emptyPile() {
        return emptyPile;
    }

    private static Portrait loadCard(CardKey card) {
        String folder = "cards/" + switch (card.type()) {
            case CHARACTER -> "characters/";
            case SPELL -> "spells/";
            case SORCERY -> "sorceries/";
            case OTHER -> "";
        };
        String stem = card.name().replaceAll("[^\\p{L}\\p{N}]+", "_").replaceAll("^_|_$", "");
        String lowercase = stem.toLowerCase(Locale.ROOT);
        // These shipped names use spellings outside the usual underscore-separated filenames.
        String titleCase = switch (lowercase.replace("_", "")) {
            case "confusion" -> "Confusion_";
            case "kiagaminguwu" -> "KiaGamingUWU";
            default -> WORD_START.matcher(lowercase)
                    .replaceAll(match -> match.group(1) + match.group(2).toUpperCase(Locale.ROOT));
        };
        return new Portrait(read(folder + stem + ".png", folder + titleCase + ".png",
                folder + lowercase.replace('_', '-') + ".png"));
    }

    private static BufferedImage read(String... paths) {
        for (String path : paths) {
            URL resource = GameArtwork.class.getResource(IMAGE_ROOT + path);
            if (resource != null) {
                try {
                    BufferedImage image = ImageIO.read(resource);
                    if (image != null) {
                        return image;
                    }
                } catch (IOException ignored) {
                    // Try the next supported filename.
                }
            }
        }
        return null;
    }

    private record CardKey(CardType type, String name) {
    }

    record Portrait(BufferedImage image, Integer currentHp) {
        Portrait(BufferedImage image) {
            this(image, null);
        }

        static int cardWidth(int height) {
            return (int) Math.round(height * CARD_ASPECT_RATIO);
        }

        Icon icon(int height) {
            return new Icon() {
                @Override
                public int getIconWidth() {
                    return cardWidth(height);
                }

                @Override
                public int getIconHeight() {
                    return height;
                }

                @Override
                public void paintIcon(Component component, Graphics graphics, int x, int y) {
                    paint(graphics, x, y, getIconWidth(), getIconHeight());
                }
            };
        }

        void paint(Graphics graphics, int x, int y, int availableWidth, int availableHeight) {
            double ratio = image == null ? CARD_ASPECT_RATIO : (double) image.getWidth() / image.getHeight();
            int height = Math.min(availableHeight, (int) Math.round(availableWidth / ratio));
            int width = Math.min(availableWidth, (int) Math.round(height * ratio));
            if (width <= 0 || height <= 0) {
                return;
            }
            int left = x + (availableWidth - width) / 2;
            int top = y + (availableHeight - height) / 2;
            Graphics2D painter = (Graphics2D) graphics.create();
            painter.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (image == null) {
                painter.setColor(GameViewStyle.CARD);
                painter.fillRoundRect(left, top, width, height, 8, 8);
                painter.setColor(GameViewStyle.LINE);
                painter.drawRoundRect(left, top, width - 1, height - 1, 8, 8);
            } else {
                painter.drawImage(image, left, top, width, height, null);
            }
            if (currentHp != null) {
                paintHp(painter, left, top, width, height);
            }
            painter.dispose();
        }

        private void paintHp(Graphics2D painter, int left, int top, int width, int height) {
            // The HP box shares the same position on all of the shipped character cards.
            String hp = currentHp.toString();
            Font baseFont = GameViewStyle.HP_FONT;
            float scale = (float) height / GameViewStyle.HAND_CARD_HEIGHT;
            Font font = baseFont.deriveFont(baseFont.getSize2D() * scale);
            painter.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            painter.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            double textWidth = font.getStringBounds(hp, painter.getFontRenderContext()).getWidth();
            double maxWidth = width * 0.19;
            if (textWidth > maxWidth) {
                font = font.deriveFont((float) (font.getSize2D() * maxWidth / textWidth));
            }
            painter.setFont(font);
            painter.setColor(GameViewStyle.HP_TEXT);
            FontMetrics metrics = painter.getFontMetrics();
            float x = left + width * 0.13f - metrics.stringWidth(hp) / 2f;
            float y = top + height * 0.063f + (metrics.getAscent() - metrics.getDescent()) / 2f;
            painter.drawString(hp, x, y);
        }
    }
}
