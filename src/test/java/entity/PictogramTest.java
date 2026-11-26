package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PictogramTest {

    @Test
    void typeIsStored() {
        Pictogram play = new Pictogram(1, "play", "play.png", WordType.ACTION);

        assertEquals(WordType.ACTION, play.getType());
        assertTrue(play.isAction());
        assertFalse(play.isThing());
    }

    @Test
    void oldConstructorCreatesThing() {
        Pictogram water = new Pictogram(2, "water", "water.png");

        assertEquals(WordType.THING, water.getType());
    }

    @Test
    void typeCannotBeNull() {
        assertThrows(NullPointerException.class,
                () -> new Pictogram(1, "play", "play.png", null));
    }


    @Test
    void samePictogramIdMeansEqualAndSameHashCode() {
        Pictogram first = new Pictogram(7, "ball", "ball.png", WordType.THING);
        Pictogram second = new Pictogram(7, "ball", "ball.png", WordType.THING);

        assertNotSame(first, second);
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}