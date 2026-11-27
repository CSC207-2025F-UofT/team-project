package interface_adapter.ingame;

import entity.card.Card;
import entity.card.CardColor;
import entity.card.CardType;
import entity.card.ColoredCard;
import entity.card.NumberCard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public class CardImageProvider {
    public BufferedImage getImage(Card card) {
        Objects.requireNonNull(card, "Card cannot be null.");

        final String path = getImagePath(card);
        final URL imageUrl = CardImageProvider.class.getResource(path);
        if (imageUrl == null) {
            throw new IllegalStateException("Card image resource not found: " + path);
        }

        try {
            final BufferedImage image = ImageIO.read(imageUrl);
            if (image == null) {
                throw new IllegalStateException("Card image resource is not a supported image: " + path);
            }
            return image;
        }
        catch (IOException exception) {
            throw new IllegalStateException("Could not read card image resource: " + path, exception);
        }
    }

    // Returns the ImagePath of any Card in assets
     private String getImagePath(Card card) {
        final CardType cardType = card.getCardType();
        if (cardType == CardType.WILD) {
            return "/cards/wild/wild.png";
        }
        if (cardType == CardType.WILD_DRAW_FOUR) {
            return "/cards/wild/wild_draw_four.png";
        }
        if (!(card instanceof ColoredCard)) {
            throw new IllegalArgumentException("Unsupported card type: " + cardType);
        }

        final CardColor color = ((ColoredCard) card).getColor();
        final String colorName = Objects.requireNonNull(color, "Colored card color cannot be null.")
                .name().toLowerCase(Locale.ROOT);
        final String face = cardType == CardType.NUMBER
                ? Integer.toString(((NumberCard) card).getNumber())
                : cardType.name().toLowerCase(Locale.ROOT);
        return "/cards/" + colorName + "/" + colorName + "_" + face + ".png";
    }
}
