package model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BeamSearchTest {
    private static final int BOS = vital_mini.BOS_ID;
    private static final int EOS = vital_mini.EOS_ID;
    private static final int A = 0, B = 1, C = 2;

    private static float[] logits(int rows) {
        float[] values = new float[rows * vital_mini.VOCAB_LENGTH];
        Arrays.fill(values, Float.NEGATIVE_INFINITY);
        return values;
    }

    private static void probability(float[] values, int row, int token, double probability) {
        values[row * vital_mini.VOCAB_LENGTH + token] = (float) Math.log(probability);
    }

    @Test
    void widerBeamRecoversBetterSequenceThanGreedy() {
        BeamSearch greedy = new BeamSearch(1, 10);
        BeamSearch beam = new BeamSearch(2, 10);
        float[] first = logits(1);
        probability(first, 0, A, 0.6);
        probability(first, 0, B, 0.4);
        greedy.advance(first);
        beam.advance(first);

        float[] second = logits(2);
        probability(second, 0, EOS, 0.4);
        probability(second, 0, C, 0.35);
        probability(second, 0, A, 0.25);
        probability(second, 1, EOS, 0.9);
        probability(second, 1, C, 0.1);
        greedy.advance(Arrays.copyOf(second, vital_mini.VOCAB_LENGTH));
        beam.advance(second);

        assertTrue(greedy.isDone());
        assertTrue(beam.isDone());
        assertArrayEquals(new int[]{BOS, A}, greedy.best());
        assertArrayEquals(new int[]{BOS, B}, beam.best()); // 0.4*0.9 > 0.6*0.4
    }

    @Test
    void greedyMasksSpecialTokensAndPreservesTieBreaking() {
        BeamSearch search = new BeamSearch(1, 10);
        float[] first = logits(1);
        first[BOS] = 1000;
        first[vital_mini.PAD_ID] = 1000;
        first[A] = first[B] = 999;
        search.advance(first);
        assertArrayEquals(new long[]{BOS, B}, search.tokenIds());
        float[] end = logits(1);
        end[EOS] = 1000;
        search.advance(end);
        assertTrue(search.isDone());
        assertArrayEquals(new int[]{BOS, B}, search.best());
    }

    @Test
    void lengthNormalizationCanFavorLongerFinishedSequence() {
        BeamSearch search = new BeamSearch(2, 10);
        float[] first = logits(1);
        probability(first, 0, EOS, 0.5);
        probability(first, 0, A, 0.5);
        search.advance(first);
        assertFalse(search.isDone());
        float[] second = logits(1);
        probability(second, 0, EOS, 0.8);
        probability(second, 0, B, 0.2);
        search.advance(second);
        assertTrue(search.isDone());
        assertArrayEquals(new int[]{BOS, A}, search.best());
    }

    @Test
    void lowRankEosDoesNotReplaceBetterActiveHypotheses() {
        BeamSearch search = new BeamSearch(2, 2);
        float[] first = logits(1);
        probability(first, 0, A, 0.6);
        probability(first, 0, B, 0.3);
        probability(first, 0, EOS, 0.1);
        search.advance(first);
        assertTrue(search.isDone());
        assertArrayEquals(new int[]{BOS, A}, search.best());
    }

    @Test
    void returnsBestActiveSequenceAtLengthLimitWithoutEos() {
        BeamSearch search = new BeamSearch(3, 3);
        float[] first = logits(1);
        first[A] = 0;
        search.advance(first);
        assertFalse(search.isDone());
        float[] second = logits(1);
        second[B] = 0;
        search.advance(second);
        assertTrue(search.isDone());
        assertArrayEquals(new int[]{BOS, A, B}, search.best());
    }

    @Test
    void eosOnlyCompletesWithoutExpandingSpecialOrImpossibleTokens() {
        BeamSearch search = new BeamSearch(3, 10);
        float[] first = logits(1);
        first[EOS] = 0;
        search.advance(first);
        assertTrue(search.isDone());
        assertArrayEquals(new int[]{BOS}, search.best());
    }

    @Test
    void comparesLogProbabilitiesAcrossParentsInsteadOfRawLogits() {
        BeamSearch search = new BeamSearch(2, 3);
        float[] first = logits(1);
        probability(first, 0, A, 0.6);
        probability(first, 0, B, 0.4);
        search.advance(first);
        float[] second = logits(2);
        for (int token = 0; token < 10; token++) second[token] = 1000;
        second[vital_mini.VOCAB_LENGTH + C] = 0;
        search.advance(second);
        assertArrayEquals(new int[]{BOS, B, C}, search.best());
    }

    @Test
    void rejectsInvalidWidthBeforeLoadingModel() {
        assertThrows(IllegalArgumentException.class, () -> new BeamSearch(0, 10));
        try (vital_mini model = new vital_mini()) {
            assertThrows(IllegalArgumentException.class, () -> model.generate(null, -1));
        }
    }
}
