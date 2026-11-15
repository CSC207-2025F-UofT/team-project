package view.answer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

// tiny gaps aa leaves inside ink at sharp notches (foot of n, u), thinning loops round them
final class Pinholes {
    private Pinholes() {
    }

    // fill gaps up to maxArea px the edge can't reach
    static boolean[] fill(boolean[] grid, int w, int h, int maxArea) {
        boolean[] on = grid.clone();
        boolean[] seen = new boolean[on.length];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int start = 0; start < on.length; start++) {
            if (on[start] || seen[start]) {
                continue;
            }
            List<Integer> gap = new ArrayList<>();
            boolean open = false;
            seen[start] = true;
            queue.add(start);
            while (!queue.isEmpty()) {
                int i = queue.poll();
                gap.add(i);
                int x = i % w;
                int y = i / w;
                open |= x == 0 || y == 0 || x == w - 1 || y == h - 1;
                int[] next = {x > 0 ? i - 1 : -1, x < w - 1 ? i + 1 : -1, y > 0 ? i - w : -1, y < h - 1 ? i + w : -1};
                for (int n : next) {
                    if (n >= 0 && !on[n] && !seen[n]) {
                        seen[n] = true;
                        queue.add(n);
                    }
                }
            }
            if (!open && gap.size() <= maxArea) {
                for (int i : gap) {
                    on[i] = true;
                }
            }
        }
        return on;
    }
}
