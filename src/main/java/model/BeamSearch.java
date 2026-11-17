package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/** 只管理 token 假设和得分；图像编码及批量解码由 vital_mini 负责。 */
final class BeamSearch {
    private static final double LENGTH_ALPHA = 0.7;
    private final int width;
    private final int maxLength;
    private int[][] tokens = {{vital_mini.BOS_ID}};
    private double[] scores = {0};
    private int[] bestFinished;
    private double bestFinishedScore = Double.NEGATIVE_INFINITY;
    private int finishedCount;
    private boolean done;

    BeamSearch(int width, int maxLength) {
        if (width < 1) throw new IllegalArgumentException("beam_width has to be >= 1");
        if (maxLength < 2 || maxLength > vital_mini.MAX_LEN) {
            throw new IllegalArgumentException("not available MAXLEN");
        }
        this.width = width;
        this.maxLength = maxLength;
    }

    int size() { return tokens.length; }
    int length() { return tokens[0].length; }
    boolean isDone() { return done; }

    long[] tokenIds() {
        long[] ids = new long[Math.multiplyExact(size(), length())];
        int offset = 0;
        for (int[] sequence : tokens) {
            for (int id : sequence) ids[offset++] = id;
        }
        return ids;
    }

    /** 输入每条活跃假设最后一位的 logits，按 [K,V] 展平。 */
    void advance(float[] logits) {
        if (done) throw new IllegalStateException("Beam Search performed");
        int vocab = vital_mini.VOCAB_LENGTH;
        if (logits.length != Math.multiplyExact(size(), vocab)) {
            throw new IllegalArgumentException("logits 必须为 [K,VOCAB_LENGTH]");
        }
        List<Candidate> candidates = new ArrayList<>();
        for (int parent = 0; parent < size(); parent++) {
            int offset = parent * vocab;
            double max = Double.NEGATIVE_INFINITY;
            for (int id = 0; id < vocab; id++) {
                float value = logits[offset + id];
                if (Float.isNaN(value) || value == Float.POSITIVE_INFINITY) {
                    throw new IllegalArgumentException("logits 包含 NaN 或正无穷");
                }
                max = Math.max(max, value);
            }
            if (max == Double.NEGATIVE_INFINITY) continue;
            double sum = 0;
            for (int id = 0; id < vocab; id++) sum += Math.exp(logits[offset + id] - max);
            double logSum = Math.log(sum);
            // 与 Python 一致：先 log_softmax，再禁止生成 BOS/PAD。
            for (int id = 0; id < vocab; id++) {
                if (id == vital_mini.BOS_ID || id == vital_mini.PAD_ID) continue;
                double score = scores[parent] + (logits[offset + id] - max) - logSum;
                if (Double.isFinite(score)) candidates.add(new Candidate(parent, id, score));
            }
        }
        // 相同得分时优先较大的 token ID，保留原贪心解码的平分规则。
        candidates.sort(Comparator.comparingDouble(Candidate::score).reversed()
                .thenComparing(Comparator.comparingInt(Candidate::token).reversed())
                .thenComparingInt(Candidate::parent));
        List<int[]> nextTokens = new ArrayList<>();
        List<Double> nextScores = new ArrayList<>();
        int count = (int) Math.min(2L * width, candidates.size());
        for (int rank = 0; rank < count; rank++) {
            Candidate candidate = candidates.get(rank);
            int[] prefix = tokens[candidate.parent()];
            if (candidate.token() == vital_mini.EOS_ID) {
                if (rank < width) {
                    // 不含 BOS，包含本次 EOS 的长度。
                    double normalized = normalize(candidate.score(), prefix.length);
                    if (bestFinished == null || normalized > bestFinishedScore) {
                        bestFinished = prefix.clone();
                        bestFinishedScore = normalized;
                    }
                    finishedCount++;
                    if (width == 1) {
                        done = true;
                        return;
                    }
                }
            } else {
                int[] sequence = Arrays.copyOf(prefix, prefix.length + 1);
                sequence[prefix.length] = candidate.token();
                nextTokens.add(sequence);
                nextScores.add(candidate.score());
                if (nextTokens.size() == width) break;
            }
        }
        if (nextTokens.isEmpty()) {
            if (bestFinished == null) throw new IllegalStateException("没有可生成的有效 token");
            done = true;
            return;
        }
        tokens = nextTokens.toArray(int[][]::new);
        scores = nextScores.stream().mapToDouble(Double::doubleValue).toArray();
        // 与 Python 相同的长度限制及启发式提前停止规则。
        done = length() >= maxLength || (finishedCount >= width
                && normalize(scores[0], length() - 1) <= bestFinishedScore);
    }

    /** 优先返回完成的序列；达到长度上限仍未生成 EOS 时返回最好的活跃假设。 */
    int[] best() {
        return (bestFinished == null ? tokens[0] : bestFinished).clone();
    }

    private static double normalize(double score, int length) {
        return score / Math.pow(Math.max(length, 1), LENGTH_ALPHA);
    }

    private record Candidate(int parent, int token, double score) { }
}
