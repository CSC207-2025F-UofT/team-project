package view.answer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// zhang-suen thinning: filled pixels -> 1px wide centre line, then corner spurs pointed
final class Skeleton {
    private Skeleton() {
    }

    // row-major w*h grid, true = ink; returns a new grid
    static boolean[] thin(boolean[] grid, int w, int h) {
        return thin(grid, w, h, 2);
    }

    // lu-wang: min 3 neighbours keeps 2px "/" diagonals that plain zhang-suen wipes out (tex x arms)
    static boolean[] thinKeepingDiagonals(boolean[] grid, int w, int h) {
        return thin(grid, w, h, 3);
    }

    private static boolean[] thin(boolean[] grid, int w, int h, int minNeighbours) {
        boolean[] on = grid.clone();
        boolean[] kill = new boolean[on.length];
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int pass = 0; pass < 2; pass++) {
                boolean any = false;
                for (int y = 1; y < h - 1; y++) {
                    for (int x = 1; x < w - 1; x++) {
                        if (on[y * w + x] && removable(on, w, x, y, pass == 0, minNeighbours)) {
                            kill[y * w + x] = true;
                            any = true;
                        }
                    }
                }
                if (any) {
                    for (int i = 0; i < on.length; i++) {
                        if (kill[i]) {
                            on[i] = false;
                            kill[i] = false;
                        }
                    }
                    changed = true;
                }
            }
        }
        return on;
    }

    // corner spurs (middle of a 3, foot of an n): arms run straight into the tip, like a pen going in and back
    static boolean[] sharpen(boolean[] grid, int w, int h, int maxLen) {
        boolean[] on = grid.clone();
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                if (on[y * w + x] && around(on, w, x, y)[0] == 1) {
                    pointUp(on, w, h, y * w + x, maxLen);
                }
            }
        }
        return on;
    }

    private static void pointUp(boolean[] on, int w, int h, int tip, int maxLen) {
        List<Integer> spur = twig(on, w, h, tip, maxLen);
        if (spur.isEmpty()) {
            return;
        }
        int fork = spur.remove(spur.size() - 1);
        if (spur.size() <= NOISE) {
            for (int i : spur) {
                on[i] = false;  // px noise, way under a pen width
            }
            return;
        }
        List<List<Integer>> arms = arms(on, w, h, fork, spur);
        if (arms.size() != 2) {
            return;
        }
        for (List<Integer> arm : arms) {
            if (arm.isEmpty() || !awayFrom(w, fork, tip, arm.get(arm.size() - 1))) {
                return;  // sideways overshoot (bar of an H), real ink
            }
        }
        List<Integer> gone = new ArrayList<>(spur.subList(1, spur.size()));
        gone.add(fork);
        for (List<Integer> arm : arms) {
            gone.addAll(arm.subList(0, arm.size() - 1));
        }
        for (int i : gone) {
            on[i] = false;
        }
        for (List<Integer> arm : arms) {
            draw(on, w, tip, arm.get(arm.size() - 1));
        }
        for (int i : gone) {
            sweep(on, w, i);  // corner px the walk skipped
        }
    }

    // pixels from an end up to a fork, fork last; empty if no fork within maxLen
    private static List<Integer> twig(boolean[] on, int w, int h, int start, int maxLen) {
        List<Integer> path = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        int prev = -1;
        int cur = start;
        while (path.size() <= maxLen) {
            if (!inside(cur, w, h)) {
                return List.of();
            }
            if (cur != start && around(on, w, cur % w, cur / w)[1] >= 3) {
                path.add(cur);
                return path;
            }
            path.add(cur);
            seen.add(cur);
            int next = step(on, w, cur, prev, seen);
            if (next < 0) {
                return List.of();  // a short stroke on its own, not a twig
            }
            prev = cur;
            cur = next;
        }
        return List.of();
    }

    // the other branches off the fork, each walked as far as the spur is long
    private static List<List<Integer>> arms(boolean[] on, int w, int h, int fork, List<Integer> spur) {
        Set<Integer> seen = new HashSet<>(spur);
        seen.add(fork);
        List<Integer> starts = new ArrayList<>();
        boolean wasOn = ringOn(on, w, fork, RING.length - 1, seen);
        for (int i = 0; i < RING.length; i++) {
            int n = fork + RING[i][1] * w + RING[i][0];
            boolean isOn = ringOn(on, w, fork, i, seen);
            if (isOn && !wasOn) {
                starts.add(n);  // first px of each run round the fork
            }
            wasOn = isOn;
        }
        for (int i = 0; i < RING.length; i++) {
            seen.add(fork + RING[i][1] * w + RING[i][0]);
        }
        List<List<Integer>> arms = new ArrayList<>();
        for (int start : starts) {
            List<Integer> arm = new ArrayList<>();
            int prev = fork;
            int cur = start;
            while (arm.size() < spur.size() && cur >= 0 && inside(cur, w, h)) {
                arm.add(cur);
                if (around(on, w, cur % w, cur / w)[1] >= 3) {
                    break;  // next fork, stop there
                }
                seen.add(cur);
                int next = step(on, w, cur, prev, seen);
                prev = cur;
                cur = next;
            }
            arms.add(arm);
        }
        return arms;
    }

    private static boolean ringOn(boolean[] on, int w, int c, int i, Set<Integer> skip) {
        int n = c + RING[i][1] * w + RING[i][0];
        return on[n] && !skip.contains(n);
    }

    // arm heads back the other way from the tip
    private static boolean awayFrom(int w, int fork, int tip, int end) {
        double tx = tip % w - fork % w;
        double ty = tip / w - fork / w;
        double ex = end % w - fork % w;
        double ey = end / w - fork / w;
        return tx * ex + ty * ey < AWAY * Math.hypot(tx, ty) * Math.hypot(ex, ey);
    }

    private static void draw(boolean[] on, int w, int from, int to) {
        int x1 = from % w;
        int y1 = from / w;
        int dx = to % w - x1;
        int dy = to / w - y1;
        int n = Math.max(Math.abs(dx), Math.abs(dy));
        for (int i = 0; i <= n; i++) {
            on[(y1 + Math.round((float) dy * i / n)) * w + x1 + Math.round((float) dx * i / n)] = true;
        }
    }

    // drop lone px next to i
    private static void sweep(boolean[] on, int w, int i) {
        for (int[] d : RING) {
            int n = i + d[1] * w + d[0];
            if (on[n] && around(on, w, n % w, n / w)[0] == 0) {
                on[n] = false;
            }
        }
    }

    private static boolean inside(int i, int w, int h) {
        int x = i % w;
        int y = i / w;
        return x >= 1 && y >= 1 && x < w - 1 && y < h - 1;
    }

    // next pixel along, straight before diagonal, skips ones that just hug the last step
    private static int step(boolean[] on, int w, int cur, int prev, Set<Integer> seen) {
        int fallback = -1;
        for (int[] d : DIRS) {
            int n = cur + d[1] * w + d[0];
            if (!on[n] || seen.contains(n)) {
                continue;
            }
            if (prev >= 0 && touching(n, prev, w)) {
                fallback = fallback < 0 ? n : fallback;
                continue;
            }
            return n;
        }
        return fallback;
    }

    private static boolean touching(int a, int b, int w) {
        return Math.abs(a % w - b % w) <= 1 && Math.abs(a / w - b / w) <= 1;
    }

    // {neighbours on, off->on flips going round}
    private static int[] around(boolean[] on, int w, int x, int y) {
        int count = 0;
        int flips = 0;
        for (int i = 0; i < 8; i++) {
            int[] a = RING[i];
            int[] b = RING[(i + 1) % 8];
            boolean pa = on[(y + a[1]) * w + x + a[0]];
            boolean pb = on[(y + b[1]) * w + x + b[0]];
            count += pa ? 1 : 0;
            flips += !pa && pb ? 1 : 0;
        }
        return new int[] {count, flips};
    }

    private static final int NOISE = 2;
    private static final double AWAY = -0.34;  // cos ~110 deg

    // clockwise from north
    private static final int[][] RING = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
    private static final int[][] DIRS = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}, {1, -1}, {1, 1}, {-1, 1}, {-1, -1}};

    private static boolean removable(boolean[] on, int w, int x, int y, boolean first, int minNeighbours) {
        // p2..p9 clockwise from north
        boolean[] p = {
            on[(y - 1) * w + x], on[(y - 1) * w + x + 1], on[y * w + x + 1], on[(y + 1) * w + x + 1],
            on[(y + 1) * w + x], on[(y + 1) * w + x - 1], on[y * w + x - 1], on[(y - 1) * w + x - 1],
        };
        int count = 0;
        int flips = 0;
        for (int i = 0; i < 8; i++) {
            count += p[i] ? 1 : 0;
            flips += !p[i] && p[(i + 1) % 8] ? 1 : 0;
        }
        if (count < minNeighbours || count > 6 || flips != 1) {
            return false;
        }
        boolean n = p[0];
        boolean e = p[2];
        boolean s = p[4];
        boolean wst = p[6];
        return first ? !(n && e && s) && !(e && s && wst) : !(n && e && wst) && !(n && s && wst);
    }
}
