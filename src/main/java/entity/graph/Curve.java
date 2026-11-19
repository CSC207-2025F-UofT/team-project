package entity.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// a sampled curve, split into unbroken pieces wherever f is undefined or jumps (1/x, tan x)
public final class Curve {
    private final List<Piece> pieces;

    public Curve(List<Piece> pieces) {
        this.pieces = Collections.unmodifiableList(new ArrayList<>(pieces));
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean isEmpty() {
        return pieces.isEmpty();
    }

    // one connected run of points
    public static final class Piece {
        private final double[] xs;
        private final double[] ys;

        public Piece(double[] xs, double[] ys) {
            if (xs.length != ys.length) {
                throw new IllegalArgumentException("xs and ys must line up");
            }
            this.xs = xs.clone();
            this.ys = ys.clone();
        }

        public double[] getXs() {
            return xs.clone();
        }

        public double[] getYs() {
            return ys.clone();
        }

        public int size() {
            return xs.length;
        }
    }
}
