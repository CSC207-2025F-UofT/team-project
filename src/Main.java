import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JPanel {

    // ---------- SIZE AND TIMING ----------

    private static final int WIDTH = 1080;
    private static final int HEIGHT = 640;

    private static final int HERO_HEIGHT = 92;
    private static final int ENEMY_HEIGHT = 115;

    private static final int MOVE_MS = 300;
    private static final int ENEMY_WAIT_MS = 350;

    private static final double ATTACK_SPEED = 1;

    // Damage occurs 35% through the attack.
    // Lower = earlier hit reaction.
    private static final double HIT_POINT = .35;

    // ---------- BACKGROUND ----------

    private static final double BACKGROUND_ZOOM = 1.0;
    private static final int BACKGROUND_X = 0;
    private static final int BACKGROUND_Y = -100;

    // ---------- CHARACTER POSITIONS ----------
    // {horizontal, vertical}
    // Increase horizontal to move right.
    // Decrease vertical to move higher.

    private static final double[][] HERO_POSITIONS = {
            {.57, .64}, // Rain
            {.66, .53}, // Lasswell
            {.75, .64}, // Fina
            {.84, .53}, // Nichol
            {.93, .64}  // Lid
    };

    private static final double[][] ENEMY_POSITIONS = {
            {.12, .64},
            {.24, .53},
            {.36, .64}
    };

    // Individual size multipliers.
    // 1 = normal, 1.2 = larger, .8 = smaller.
    private static final double[] HERO_SIZES = {
            1, 1, 1, 1, 1
    };

    private static final double[] ENEMY_SIZES = {
            1.15, 1.15, 1.15
    };

    // ---------- COLOURS ----------

    private static final Color TEXT =
            new Color(239, 248, 255);

    private static final Color ACCENT =
            new Color(105, 217, 242);

    private static final Color ENEMY =
            new Color(255, 126, 139);

    private static final Color HP =
            new Color(69, 229, 154);

    // Last number controls opacity: 0–255.
    private static final Color PANEL =
            new Color(13, 36, 51, 185);

    private static final String[] ACTIONS = {
            "Attack", "Defend", "Items", "Run"
    };

    @FunctionalInterface
    public interface Loader {
        GameAssets load() throws Exception;
    }

    private GameAssets assets;
    private Battle battle;

    private String message =
            "Loading characters and animations...";

    private boolean choosing;
    private boolean items;
    private boolean hitApplied;

    private Point mouse = new Point(-1, -1);
    private int pressed = -1;

    private final float[] hover = new float[4];

    private final Map<Battle.Fighter, Rectangle> bodies =
            new HashMap<>();

    private final Map<Battle.Fighter, Long> deaths =
            new HashMap<>();

    private Battle.Fighter target;
    private Battle.Fighter actor;
    private Battle.Fighter victim;

    // 0 ready, 1 approach, 2 strike, 3 return.
    private int phase;

    private long phaseStart;
    private long readyAt;
    private long hitAt;

    private final long openedAt =
            System.currentTimeMillis();

    private final Timer timer;

    public Main(Loader loader) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(10, 24, 34));

        MouseAdapter input = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                mouse = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouse = e.getPoint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouse = new Point(-1, -1);
                pressed = -1;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }

                mouse = e.getPoint();

                pressed = canAct()
                        ? actionAt(mouse)
                        : -1;

                click(mouse);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = -1;
            }
        };

        addMouseListener(input);
        addMouseMotionListener(input);

        timer = new Timer(16, e -> {
            update();
            updateHover();
            repaint();
        });

        timer.start();

        new SwingWorker<GameAssets, Void>() {

            @Override
            protected GameAssets doInBackground()
                    throws Exception {
                return loader.load();
            }

            @Override
            protected void done() {
                try {
                    assets = get();
                    battle = assets.battle;

                    for (Battle.Fighter f : battle.players) {
                        cacheBody(f);
                    }

                    for (Battle.Fighter f : battle.enemies) {
                        cacheBody(f);
                    }

                    message = "An enemy group approaches!\n"
                            + battle.prompt();

                    readyAt = System.currentTimeMillis();

                } catch (Exception e) {
                    Throwable cause = e.getCause() == null
                            ? e
                            : e.getCause();

                    message = "Could not load assets: "
                            + cause.getMessage();

                    repaint();
                }
            }
        }.execute();
    }

    private boolean canAct() {
        return battle != null
                && !battle.ended
                && phase == 0
                && battle.current() != null
                && battle.current().player;
    }

    // Remember sprite bounds once instead of scanning every frame.
    private void cacheBody(Battle.Fighter f) {
        bodies.put(
                f,
                GameAssets.bounds(
                        assets.art.get(f).idle.frames[0]
                )
        );
    }

    private int actionAt(Point p) {
        for (int i = 0; i < ACTIONS.length; i++) {
            if (actionBox(i).contains(p)) {
                return i;
            }
        }

        return -1;
    }

    // Gradually fades button highlights in and out.
    private void updateHover() {
        int over = canAct() ? actionAt(mouse) : -1;

        for (int i = 0; i < hover.length; i++) {
            float goal = i == over ? 1 : 0;
            hover[i] += (goal - hover[i]) * .22f;
        }

        boolean hand = canAct()
                && (over >= 0 || enemyAt(mouse) != null);

        setCursor(Cursor.getPredefinedCursor(
                hand
                        ? Cursor.HAND_CURSOR
                        : Cursor.DEFAULT_CURSOR
        ));
    }

    private void click(Point point) {
        if (!canAct()) {
            return;
        }

        int action = actionAt(point);

        if (action >= 0) {
            choosing = false;
            items = false;

            if (action == 0) {
                if (target != null && target.alive()) {
                    startAttack(target);
                } else {
                    choosing = true;

                    message = battle.current().name
                            + ": click an enemy to attack.";
                }

            } else if (action == 1) {
                target = null;
                message = battle.defend();
                finishTurn();

            } else if (action == 2) {
                items = true;

                message = "Inventory placeholder"
                        + " — no items connected yet.";

            } else {
                message = "Run is a placeholder for now.";
            }

            return;
        }

        Battle.Fighter selected = enemyAt(point);

        if (selected != null) {
            target = selected;

            if (choosing) {
                startAttack(selected);
            } else {
                items = false;

                message = "Target: "
                        + target.name
                        + ". Click Attack.";
            }
        }
    }

    // Click area follows the enemy's actual visible sprite.
    private Rectangle targetBox(Battle.Fighter f) {
        Point foot = home(f);
        Rectangle body = bodies.get(f);

        GameAssets.Art art = assets.art.get(f);

        double scale =
                spriteHeight(f) / art.idle.bodyHeight;

        double left = art.mirror
                ? art.idle.anchorX - body.getMaxX()
                : body.x - art.idle.anchorX;

        return new Rectangle(
                foot.x + (int) (left * scale) - 12,

                foot.y + (int) (
                        (body.y - art.idle.anchorY) * scale
                ) - 12,

                Math.max(
                        40,
                        (int) Math.ceil(body.width * scale)
                ) + 24,

                Math.max(
                        40,
                        (int) Math.ceil(body.height * scale)
                ) + 48
        );
    }

    private Battle.Fighter enemyAt(Point p) {
        if (battle == null) {
            return null;
        }

        Battle.Fighter best = null;
        double distance = Double.MAX_VALUE;

        for (Battle.Fighter f : battle.enemies) {
            Rectangle box = targetBox(f);

            if (!f.alive() || !box.contains(p)) {
                continue;
            }

            double d = p.distanceSq(
                    box.getCenterX(),
                    box.getCenterY()
            );

            if (d < distance) {
                best = f;
                distance = d;
            }
        }

        return best;
    }

    private void startAttack(Battle.Fighter chosen) {
        actor = battle.current();
        victim = chosen;
        target = chosen;

        choosing = false;
        items = false;
        hitApplied = false;

        phase = 1;
        phaseStart = System.currentTimeMillis();

        message = actor.name
                + " attacks " + victim.name + "!";
    }

    // Advances movement, strike and return stages.
    private void update() {
        if (battle == null || battle.ended) {
            return;
        }

        long now = System.currentTimeMillis();
        long elapsed = now - phaseStart;

        if (phase == 1 && elapsed >= MOVE_MS) {
            phase = 2;
            phaseStart = now;

        } else if (phase == 2) {
            long duration = attackDuration();

            if (!hitApplied
                    && elapsed >= duration * HIT_POINT) {

                hitApplied = true;

                message = battle.attack(victim);
                hitAt = now;

                if (!victim.alive()) {
                    deaths.put(victim, now);
                }
            }

            if (elapsed >= duration) {
                phase = 3;
                phaseStart = now;
            }

        } else if (phase == 3 && elapsed >= MOVE_MS) {
            phase = 0;
            actor = null;
            finishTurn();

        } else if (phase == 0
                && !battle.current().player
                && now - readyAt >= ENEMY_WAIT_MS) {

            List<Battle.Fighter> living =
                    new ArrayList<>();

            for (Battle.Fighter f : battle.players) {
                if (f.alive()) {
                    living.add(f);
                }
            }

            int index = java.util.concurrent
                    .ThreadLocalRandom.current()
                    .nextInt(living.size());

            startAttack(living.get(index));
        }
    }

    private long attackDuration() {
        return Math.max(
                250,
                (long) (
                        assets.art.get(actor).attack.duration
                                / ATTACK_SPEED
                )
        );
    }

    private void finishTurn() {
        battle.next();

        target = null;
        choosing = false;
        items = false;

        message += "\n" + battle.prompt();
        readyAt = System.currentTimeMillis();
    }

    private int sideIndex(Battle.Fighter f) {
        return (f.player
                ? battle.players
                : battle.enemies).indexOf(f);
    }

    private double spriteHeight(Battle.Fighter f) {
        double[] sizes = f.player
                ? HERO_SIZES
                : ENEMY_SIZES;

        int i = sideIndex(f);

        double multiplier =
                i < sizes.length ? sizes[i] : 1;

        return (f.player ? HERO_HEIGHT : ENEMY_HEIGHT)
                * multiplier;
    }

    private Point home(Battle.Fighter f) {
        double[][] positions = f.player
                ? HERO_POSITIONS
                : ENEMY_POSITIONS;

        double[] p = positions[sideIndex(f)];

        return new Point(
                (int) (getWidth() * p[0]),
                (int) (getHeight() * p[1])
        );
    }

    private Point contact() {
        Point p = home(victim);

        int gap = (int) (
                spriteHeight(actor) * .40
                        + spriteHeight(victim) * .35
        );

        p.x += actor.player ? gap : -gap;

        return p;
    }

    private Point position(Battle.Fighter f) {
        Point start = home(f);

        if (f != actor || phase == 0) {
            return start;
        }

        Point end = contact();

        double t = Math.min(
                1,
                (System.currentTimeMillis() - phaseStart)
                        / (double) MOVE_MS
        );

        if (phase == 2) {
            return end;
        }

        // Smooth acceleration and deceleration.
        t = t * t * (3 - 2 * t);

        if (phase == 3) {
            t = 1 - t;
        }

        return new Point(
                (int) (start.x + (end.x - start.x) * t),
                (int) (start.y + (end.y - start.y) * t)
        );
    }

    private Rectangle actionBox(int i) {
        return new Rectangle(
                (int) (getWidth() * .81),
                42 + i * 37,
                (int) (getWidth() * .17),
                32
        );
    }

    private Rectangle cardBox(int i) {
        int left = (int) (getWidth() * .39);
        int total = (int) (getWidth() * .59);
        int width = total / battle.players.size();

        return new Rectangle(
                left + i * width,
                getHeight() - 142,
                width - 6,
                126
        );
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g =
                (Graphics2D) graphics.create();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        if (assets != null) {
            BufferedImage bg = assets.background;

            double scale = Math.max(
                    getWidth() / (double) bg.getWidth(),
                    getHeight() / (double) bg.getHeight()
            ) * BACKGROUND_ZOOM;

            int w = (int) (bg.getWidth() * scale);
            int h = (int) (bg.getHeight() * scale);

            g.drawImage(
                    bg,
                    (getWidth() - w) / 2 + BACKGROUND_X,
                    (getHeight() - h) / 2 + BACKGROUND_Y,
                    w, h,
                    null
            );

            // Heroes are drawn over enemies.
            for (Battle.Fighter f : battle.enemies) {
                drawFighter(g, f);
            }

            for (Battle.Fighter f : battle.players) {
                drawFighter(g, f);
            }

            drawTracker(g);

            for (int i = 0;
                 i < battle.players.size();
                 i++) {

                drawCard(g, i);
            }
        }

        text(g, "ENCOUNTER", 24, 27, 17, TEXT);

        drawActions(g);

        Rectangle dialogue = new Rectangle(
                20,
                getHeight() - 142,
                (int) (getWidth() * .36),
                126
        );

        panel(g, dialogue, ACCENT);

        wrap(
                g,
                (items ? "ITEMS\n" : "") + message,
                dialogue
        );

        g.dispose();
    }

    private void drawActions(Graphics2D g) {
        boolean enabled = canAct();

        for (int i = 0; i < ACTIONS.length; i++) {
            Rectangle r = actionBox(i);
            float glow = hover[i];

            boolean selected =
                    enabled && i == 0 && choosing;

            Color top = new Color(
                    21 + (int) (glow * 19),
                    53 + (int) (glow * 24),
                    73 + (int) (glow * 28),
                    205
            );

            if (pressed == i && enabled) {
                top = new Color(35, 95, 115, 225);
            }

            g.setPaint(new GradientPaint(
                    r.x, r.y, top,
                    r.x, r.y + r.height, PANEL
            ));

            g.fillRoundRect(
                    r.x, r.y,
                    r.width, r.height,
                    8, 8
            );

            g.setColor(
                    selected || glow > .05
                            ? ACCENT
                            : new Color(65, 111, 135)
            );

            g.setStroke(
                    new BasicStroke(selected ? 2 : 1)
            );

            g.drawRoundRect(
                    r.x, r.y,
                    r.width, r.height,
                    8, 8
            );

            g.setColor(new Color(
                    105, 217, 242,
                    selected
                            ? 230
                            : (int) (glow * 210)
            ));

            g.fillRoundRect(
                    r.x,
                    r.y + 4,
                    3,
                    r.height - 8,
                    3, 3
            );

            text(
                    g,
                    selected
                            ? "Choose target..."
                            : ACTIONS[i],
                    r.x + 16 + (int) (glow * 4),
                    r.y + 22,
                    16,
                    enabled
                            ? TEXT
                            : new Color(130, 150, 163)
            );
        }
    }

    private void drawFighter(
            Graphics2D graphics,
            Battle.Fighter f) {

        Graphics2D g =
                (Graphics2D) graphics.create();

        Point p = position(f);

        GameAssets.Art art = assets.art.get(f);
        GameAssets.Clip clip = art.idle;

        long now = System.currentTimeMillis();
        long time = now - openedAt;

        boolean loop = true;
        boolean mirror = art.mirror;

        if (!f.alive()) {
            long since = now
                    - deaths.getOrDefault(f, now);

            if (art.dead != null) {
                clip = art.dead;
                time = since;
                loop = false;
            }

            float alpha = art.dead == null
                    ? Math.max(0, 1 - since / 350f)
                    : 1f;

            g.setComposite(
                    AlphaComposite.SrcOver.derive(alpha)
            );

        } else if (f == actor && phase > 0) {
            clip = phase == 2
                    ? art.attack
                    : art.move;

            time = now - phaseStart;

            if (phase == 2) {
                time = (long) (time * ATTACK_SPEED);
                loop = false;
            }

            if (phase == 3) {
                mirror = !mirror;
            }
        }

        if (canAct()
                && f.alive()
                && !f.player
                && (f == target
                || choosing
                || f == enemyAt(mouse))) {

            g.setColor(ACCENT);

            g.setStroke(
                    new BasicStroke(f == target ? 3 : 1)
            );

            g.drawOval(
                    p.x - 43,
                    p.y - 9,
                    86, 22
            );
        }

        if (f == victim
                && hitAt > 0
                && now - hitAt < 300) {

            p.x += now / 60 % 2 == 0 ? -3 : 3;
        }

        double scale =
                spriteHeight(f) / art.idle.bodyHeight;

        clip.draw(
                g,
                p.x, p.y,
                scale,
                mirror,
                time,
                loop
        );

        int nameWidth = g.getFontMetrics(
                new Font("Dialog", Font.BOLD, 14)
        ).stringWidth(f.name);

        text(
                g, f.name,
                p.x - nameWidth / 2,
                p.y + 26,
                14,
                TEXT
        );

        if (!f.player) {
            hpBar(
                    g,
                    p.x - 36,
                    p.y + 33,
                    72, 5,
                    f
            );
        }

        g.dispose();
    }

    private void drawTracker(Graphics2D g) {
        text(g, "TURN ORDER", 24, 58, 12, TEXT);

        List<Battle.Fighter> next =
                battle.preview(5);

        int width = Math.min(
                145,
                (int) (getWidth() * .72 / 5)
        );

        for (int i = 0; i < next.size(); i++) {
            Battle.Fighter f = next.get(i);

            Rectangle r = new Rectangle(
                    24 + i * (width + 6),
                    68,
                    width,
                    75
            );

            panel(
                    g, r,
                    f.player ? ACCENT : ENEMY
            );

            GameAssets.Art art = assets.art.get(f);

            art.idle.draw(
                    g,
                    r.x + 25,
                    r.y + 47,
                    34.0 / art.idle.bodyHeight,
                    art.mirror,
                    System.currentTimeMillis() - openedAt,
                    true
            );

            text(
                    g,
                    i == 0 ? "NOW" : "+" + i,
                    r.x + 53,
                    r.y + 22,
                    13,
                    f.player ? ACCENT : ENEMY
            );

            text(
                    g,
                    "SPD " + f.speed,
                    r.x + 53,
                    r.y + 41,
                    11,
                    TEXT
            );

            text(
                    g,
                    f.name,
                    r.x + 8,
                    r.y + 65,
                    12,
                    TEXT
            );
        }
    }

    private void drawCard(Graphics2D g, int index) {
        Battle.Fighter f =
                battle.players.get(index);

        Rectangle r = cardBox(index);

        panel(
                g,
                r,
                !battle.ended && f == battle.current()
                        ? ACCENT
                        : new Color(79, 111, 130)
        );

        BufferedImage portrait =
                assets.art.get(f).portrait;

        if (portrait != null) {
            double scale = Math.min(
                    45.0 / portrait.getWidth(),
                    35.0 / portrait.getHeight()
            );

            g.drawImage(
                    portrait,
                    r.x + 8,
                    r.y + 9,
                    (int) (portrait.getWidth() * scale),
                    (int) (portrait.getHeight() * scale),
                    null
            );
        }

        text(
                g, f.name,
                r.x + 54,
                r.y + 29,
                12,
                TEXT
        );

        hpBar(
                g,
                r.x + 9,
                r.y + 54,
                r.width - 18,
                7,
                f
        );

        text(
                g,
                f.hp + " / " + f.maxHp + " HP",
                r.x + 9,
                r.y + 81,
                12,
                TEXT
        );

        text(
                g,
                "ATK " + f.attack + "  DEF " + f.defense,
                r.x + 9,
                r.y + 100,
                10,
                TEXT
        );

        text(
                g,
                "SPD " + f.speed,
                r.x + 9,
                r.y + 116,
                10,
                TEXT
        );
    }

    private void hpBar(
            Graphics2D g,
            int x, int y,
            int w, int h,
            Battle.Fighter f) {

        g.setColor(new Color(7, 19, 27));
        g.fillRect(x, y, w, h);

        g.setColor(HP);

        g.fillRect(
                x, y,
                (int) (w * f.hp / (double) f.maxHp),
                h
        );
    }

    private void panel(
            Graphics2D g,
            Rectangle r,
            Color border) {

        g.setColor(PANEL);

        g.fillRect(
                r.x, r.y,
                r.width, r.height
        );

        g.setColor(border);
        g.setStroke(new BasicStroke(1.5f));

        g.drawRect(
                r.x, r.y,
                r.width, r.height
        );
    }

    private void text(
            Graphics2D g,
            String text,
            int x, int y,
            int size,
            Color color) {

        g.setFont(
                new Font("Dialog", Font.BOLD, size)
        );

        g.setColor(color);
        g.drawString(text, x, y);
    }

    private void wrap(
            Graphics2D graphics,
            String message,
            Rectangle r) {

        Graphics2D g =
                (Graphics2D) graphics.create();

        g.clipRect(
                r.x + 10,
                r.y + 8,
                r.width - 20,
                r.height - 16
        );

        g.setFont(
                new Font("Dialog", Font.PLAIN, 16)
        );

        g.setColor(TEXT);

        int x = r.x + 13;
        int y = r.y + 26;

        for (String paragraph : message.split("\n")) {
            String line = "";

            for (String word : paragraph.split(" ")) {
                if (g.getFontMetrics().stringWidth(
                        line + word) > r.width - 26) {

                    g.drawString(line, x, y);
                    y += 21;
                    line = "";
                }

                line += word + " ";
            }

            g.drawString(line, x, y);
            y += 21;
        }

        g.dispose();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main view = new Main(new FFBE_API()::load);

            JFrame window =
                    new JFrame("Battle Preview");

            window.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            window.setContentPane(view);

            window.setMinimumSize(
                    new Dimension(1120, 700)
            );

            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}