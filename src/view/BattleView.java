package view;

import data_access.GameAssets;
import entity.Battle;
import entity.Fighter;
import use_case.Combat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleView extends JPanel {

    // ---------- SIZE AND TIMING ----------

    private static final int WIDTH = 1080;
    private static final int HEIGHT = 640;

    private static final int HERO_HEIGHT = 92;
    private static final int ENEMY_HEIGHT = 115;

    private static final int MOVE_MS = 300;
    private static final int ENEMY_WAIT_MS = 350;

    private static final double ATTACK_SPEED = 1;

    // Damage occurs 35% through the attack.
    private static final double HIT_POINT = .35;

    // ---------- BACKGROUND ----------

    private static final double BACKGROUND_ZOOM = 1.0;
    private static final int BACKGROUND_X = 0;
    private static final int BACKGROUND_Y = -100;

    // ---------- CHARACTER POSITIONS ----------

    private static final double[][] HERO_POSITIONS = {
            {.57, .64},
            {.66, .53},
            {.75, .64},
            {.84, .53},
            {.93, .64}
    };

    private static final double[][] ENEMY_POSITIONS = {
            {.12, .64},
            {.24, .53},
            {.36, .64}
    };

    private static final double[] HERO_SIZES = {
            1, 1, 1, 1, 1
    };

    private static final double[] ENEMY_SIZES = {
            .75, .75, .75
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

    private static final Color PANEL =
            new Color(13, 36, 51, 185);

    private static final String[] ACTIONS = {
            "Attack", "Defend", "Skill", "Items"
    };

    // ---------- DEPENDENCIES ----------

    private final GameAssets assets;
    private final Battle battle;

    private final Combat.Controller combatController;
    private final BattleViewModel viewModel;

    // ---------- UI STATE ----------

    private boolean choosing;
    private boolean items;
    private boolean hitApplied;
    private boolean usingSkill;

    private Point mouse = new Point(-1, -1);
    private int pressed = -1;

    private final float[] hover = new float[4];

    private final Map<Fighter, Rectangle> bodies =
            new HashMap<>();

    private final Map<Fighter, Long> deaths =
            new HashMap<>();

    private Fighter target;
    private Fighter actor;
    private Fighter victim;

    // 0 ready, 1 approach, 2 strike, 3 return.
    private int phase;

    private long phaseStart;
    private long readyAt;
    private long hitAt;

    private final long openedAt =
            System.currentTimeMillis();

    private final Timer timer;

    // ---------- CONSTRUCTOR ----------

    public BattleView(
            GameAssets assets,
            Combat.Controller combatController,
            BattleViewModel viewModel) {

        this.assets = assets;
        this.battle = assets.battle;
        this.combatController = combatController;
        this.viewModel = viewModel;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(10, 24, 34));

        setupInput();

        for (Fighter fighter : battle.players) {
            cacheBody(fighter);
        }

        for (Fighter fighter : battle.enemies) {
            cacheBody(fighter);
        }

        viewModel.setMessage(
                "An enemy group approaches!\n"
                        + battle.prompt()
        );

        readyAt = System.currentTimeMillis();

        timer = new Timer(16, e -> {
            update();
            updateHover();
            repaint();
        });

        timer.start();
    }

    // ---------- INPUT ----------

    private void setupInput() {

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
    }

    private boolean canAct() {
        return !battle.ended
                && phase == 0
                && battle.current() != null
                && battle.current().player;
    }

    private void cacheBody(Fighter fighter) {
        bodies.put(
                fighter,
                GameAssets.bounds(
                        assets.art.get(fighter).idle.frames[0]
                )
        );
    }

    private int actionAt(Point point) {
        for (int i = 0; i < ACTIONS.length; i++) {
            if (actionBox(i).contains(point)) {
                return i;
            }
        }

        return -1;
    }

    private void updateHover() {
        int over = canAct()
                ? actionAt(mouse)
                : -1;

        for (int i = 0; i < hover.length; i++) {
            float goal = i == over ? 1 : 0;

            hover[i] +=
                    (goal - hover[i]) * .22f;
        }

        boolean hand = canAct()
                && (over >= 0
                || enemyAt(mouse) != null);

        setCursor(
                Cursor.getPredefinedCursor(
                        hand
                                ? Cursor.HAND_CURSOR
                                : Cursor.DEFAULT_CURSOR
                )
        );
    }

    // ---------- CLICK HANDLING ----------

    private void click(Point point) {
        if (!canAct()) {
            return;
        }

        int action = actionAt(point);

        if (action >= 0) {
            choosing = false;
            items = false;

            // Attack
            if (action == 0) {
                usingSkill = false;

                if (target != null && target.alive()) {
                    startAttack(target);
                } else {
                    choosing = true;

                    viewModel.setMessage(
                            battle.current().name
                                    + ": click an enemy to attack."
                    );
                }

                // Defend
            } else if (action == 1) {
                target = null;
                usingSkill = false;

                combatController.defend();
                finishTurn();

                // Skill
            } else if (action == 2) {
                usingSkill = true;

                if (target != null && target.alive()) {
                    startAttack(target);
                } else {
                    choosing = true;

                    viewModel.setMessage(
                            battle.current().name
                                    + ": click an enemy to use Skill."
                    );
                }

                // Items
            } else if (action == 3) {
                items = true;
                usingSkill = false;

                viewModel.setMessage(
                        "Inventory placeholder"
                                + " — no items connected yet."
                );
            }

            return;
        }

        Fighter selected = enemyAt(point);

        if (selected != null) {
            target = selected;

            if (choosing) {
                startAttack(selected);
            } else {
                items = false;

                viewModel.setMessage(
                        "Target: "
                                + target.name
                                + ". Choose Attack or Skill."
                );
            }
        }
    }

    // ---------- TARGETING ----------

    private Rectangle targetBox(Fighter fighter) {
        Point foot = home(fighter);
        Rectangle body = bodies.get(fighter);

        GameAssets.Art art =
                assets.art.get(fighter);

        double scale =
                spriteHeight(fighter)
                        / art.idle.bodyHeight;

        double left = art.mirror
                ? art.idle.anchorX - body.getMaxX()
                : body.x - art.idle.anchorX;

        return new Rectangle(
                foot.x
                        + (int) (left * scale)
                        - 12,

                foot.y
                        + (int) (
                        (body.y - art.idle.anchorY)
                                * scale
                )
                        - 12,

                Math.max(
                        40,
                        (int) Math.ceil(
                                body.width * scale
                        )
                ) + 24,

                Math.max(
                        40,
                        (int) Math.ceil(
                                body.height * scale
                        )
                ) + 48
        );
    }

    private Fighter enemyAt(Point point) {

        Fighter best = null;
        double distance = Double.MAX_VALUE;

        for (Fighter fighter : battle.enemies) {

            Rectangle box =
                    targetBox(fighter);

            if (!fighter.alive()
                    || !box.contains(point)) {

                continue;
            }

            double currentDistance =
                    point.distanceSq(
                            box.getCenterX(),
                            box.getCenterY()
                    );

            if (currentDistance < distance) {
                best = fighter;
                distance = currentDistance;
            }
        }

        return best;
    }

    // ---------- COMBAT ANIMATION ----------

    private void startAttack(Fighter chosen) {

        actor = battle.current();
        victim = chosen;
        target = chosen;

        choosing = false;
        items = false;
        hitApplied = false;

        phase = 1;
        phaseStart =
                System.currentTimeMillis();

        if (usingSkill) {
            viewModel.setMessage(
                    actor.name
                            + " uses Skill on "
                            + victim.name
                            + "!"
            );
        } else {
            viewModel.setMessage(
                    actor.name
                            + " attacks "
                            + victim.name
                            + "!"
            );
        }
    }

    private void update() {

        if (battle.ended) {
            return;
        }

        long now =
                System.currentTimeMillis();

        long elapsed =
                now - phaseStart;

        // Approach target.
        if (phase == 1
                && elapsed >= MOVE_MS) {

            phase = 2;
            phaseStart = now;

            // Attack/skill animation.
        } else if (phase == 2) {

            long duration =
                    attackDuration();

            // Damage occurs 35% through animation.
            if (!hitApplied
                    && elapsed >= duration * HIT_POINT) {

                hitApplied = true;

                if (usingSkill) {
                    combatController.skill(victim);
                } else {
                    combatController.attack(victim);
                }

                hitAt = now;

                if (!victim.alive()) {
                    deaths.put(victim, now);
                }
            }

            if (elapsed >= duration) {
                phase = 3;
                phaseStart = now;
            }

            // Return home.
        } else if (phase == 3
                && elapsed >= MOVE_MS) {

            phase = 0;
            actor = null;

            finishTurn();

            // Enemy AI.
        } else if (phase == 0
                && battle.current() != null
                && !battle.current().player
                && now - readyAt >= ENEMY_WAIT_MS) {

            List<Fighter> living =
                    new ArrayList<>();

            for (Fighter fighter : battle.players) {
                if (fighter.alive()) {
                    living.add(fighter);
                }
            }

            if (!living.isEmpty()) {

                int index =
                        java.util.concurrent
                                .ThreadLocalRandom
                                .current()
                                .nextInt(living.size());

                // Enemy attacks should never inherit player skill state.
                usingSkill = false;

                startAttack(
                        living.get(index)
                );
            }
        }
    }

    private long attackDuration() {

        return Math.max(
                250,
                (long) (
                        assets.art
                                .get(actor)
                                .attack
                                .duration
                                / ATTACK_SPEED
                )
        );
    }

    private void finishTurn() {

        battle.next();

        target = null;
        choosing = false;
        items = false;
        usingSkill = false;

        viewModel.setMessage(
                viewModel.getMessage()
                        + "\n"
                        + battle.prompt()
        );

        readyAt =
                System.currentTimeMillis();
    }

    // ---------- POSITIONING ----------

    private int sideIndex(Fighter fighter) {

        return (
                fighter.player
                        ? battle.players
                        : battle.enemies
        ).indexOf(fighter);
    }

    private double spriteHeight(
            Fighter fighter) {

        double[] sizes =
                fighter.player
                        ? HERO_SIZES
                        : ENEMY_SIZES;

        int index =
                sideIndex(fighter);

        double multiplier =
                index < sizes.length
                        ? sizes[index]
                        : 1;

        return (
                fighter.player
                        ? HERO_HEIGHT
                        : ENEMY_HEIGHT
        ) * multiplier;
    }

    private Point home(Fighter fighter) {

        double[][] positions =
                fighter.player
                        ? HERO_POSITIONS
                        : ENEMY_POSITIONS;

        double[] position =
                positions[
                        sideIndex(fighter)
                        ];

        return new Point(
                (int) (
                        getWidth()
                                * position[0]
                ),
                (int) (
                        getHeight()
                                * position[1]
                )
        );
    }

    private Point contact() {

        Point point =
                home(victim);

        int gap = (int) (
                spriteHeight(actor) * .40
                        + spriteHeight(victim) * .35
        );

        point.x +=
                actor.player
                        ? gap
                        : -gap;

        return point;
    }

    private Point position(
            Fighter fighter) {

        Point start =
                home(fighter);

        if (fighter != actor
                || phase == 0) {

            return start;
        }

        Point end =
                contact();

        double t =
                Math.min(
                        1,
                        (System.currentTimeMillis()
                                - phaseStart)
                                / (double) MOVE_MS
                );

        if (phase == 2) {
            return end;
        }

        t = t * t * (3 - 2 * t);

        if (phase == 3) {
            t = 1 - t;
        }

        return new Point(
                (int) (
                        start.x
                                + (end.x - start.x)
                                * t
                ),
                (int) (
                        start.y
                                + (end.y - start.y)
                                * t
                )
        );
    }

    // ---------- UI BOXES ----------

    private Rectangle actionBox(int index) {

        return new Rectangle(
                (int) (getWidth() * .81),
                42 + index * 37,
                (int) (getWidth() * .17),
                32
        );
    }

    private Rectangle cardBox(int index) {

        int left =
                (int) (getWidth() * .39);

        int total =
                (int) (getWidth() * .59);

        int width =
                total / battle.players.size();

        return new Rectangle(
                left + index * width,
                getHeight() - 142,
                width - 6,
                126
        );
    }

    // ---------- MAIN DRAW ----------

    @Override
    protected void paintComponent(
            Graphics graphics) {

        super.paintComponent(graphics);

        Graphics2D g =
                (Graphics2D)
                        graphics.create();

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints
                        .VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        BufferedImage background =
                assets.background;

        double scale =
                Math.max(
                        getWidth()
                                / (double)
                                background.getWidth(),

                        getHeight()
                                / (double)
                                background.getHeight()
                ) * BACKGROUND_ZOOM;

        int width =
                (int) (
                        background.getWidth()
                                * scale
                );

        int height =
                (int) (
                        background.getHeight()
                                * scale
                );

        g.drawImage(
                background,
                (getWidth() - width) / 2
                        + BACKGROUND_X,
                (getHeight() - height) / 2
                        + BACKGROUND_Y,
                width,
                height,
                null
        );

        // Enemies first.
        for (Fighter fighter :
                battle.enemies) {

            drawFighter(g, fighter);
        }

        // Heroes over enemies.
        for (Fighter fighter :
                battle.players) {

            drawFighter(g, fighter);
        }

        drawTracker(g);

        for (int i = 0;
             i < battle.players.size();
             i++) {

            drawCard(g, i);
        }

        text(
                g,
                "ENCOUNTER",
                24,
                27,
                17,
                TEXT
        );

        drawActions(g);

        Rectangle dialogue =
                new Rectangle(
                        20,
                        getHeight() - 142,
                        (int) (
                                getWidth()
                                        * .36
                        ),
                        126
                );

        panel(
                g,
                dialogue,
                ACCENT
        );

        wrap(
                g,
                (items
                        ? "ITEMS\n"
                        : "")
                        + viewModel.getMessage(),
                dialogue
        );

        g.dispose();
    }

    // ---------- ACTION BUTTONS ----------

    private void drawActions(
            Graphics2D g) {

        boolean enabled =
                canAct();

        for (int i = 0;
             i < ACTIONS.length;
             i++) {

            Rectangle rectangle =
                    actionBox(i);

            float glow =
                    hover[i];

            boolean selected =
                    enabled
                            && choosing
                            && ((i == 0 && !usingSkill)
                            || (i == 2 && usingSkill));

            Color top =
                    new Color(
                            21
                                    + (int)
                                    (glow * 19),
                            53
                                    + (int)
                                    (glow * 24),
                            73
                                    + (int)
                                    (glow * 28),
                            205
                    );

            if (pressed == i
                    && enabled) {

                top = new Color(
                        35,
                        95,
                        115,
                        225
                );
            }

            g.setPaint(
                    new GradientPaint(
                            rectangle.x,
                            rectangle.y,
                            top,

                            rectangle.x,
                            rectangle.y
                                    + rectangle.height,
                            PANEL
                    )
            );

            g.fillRoundRect(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height,
                    8,
                    8
            );

            g.setColor(
                    selected
                            || glow > .05
                            ? ACCENT
                            : new Color(
                            65,
                            111,
                            135
                    )
            );

            g.setStroke(
                    new BasicStroke(
                            selected
                                    ? 2
                                    : 1
                    )
            );

            g.drawRoundRect(
                    rectangle.x,
                    rectangle.y,
                    rectangle.width,
                    rectangle.height,
                    8,
                    8
            );

            g.setColor(
                    new Color(
                            105,
                            217,
                            242,
                            selected
                                    ? 230
                                    : (int)
                                    (glow * 210)
                    )
            );

            g.fillRoundRect(
                    rectangle.x,
                    rectangle.y + 4,
                    3,
                    rectangle.height - 8,
                    3,
                    3
            );

            text(
                    g,
                    selected
                            ? "Choose target..."
                            : ACTIONS[i],

                    rectangle.x
                            + 16
                            + (int)
                            (glow * 4),

                    rectangle.y + 22,
                    16,

                    enabled
                            ? TEXT
                            : new Color(
                            130,
                            150,
                            163
                    )
            );
        }
    }

    // ---------- FIGHTERS ----------

    private void drawFighter(
            Graphics2D graphics,
            Fighter fighter) {

        Graphics2D g =
                (Graphics2D)
                        graphics.create();

        Point point =
                position(fighter);

        GameAssets.Art art =
                assets.art.get(fighter);

        GameAssets.Clip clip =
                art.idle;

        long now =
                System.currentTimeMillis();

        long time =
                now - openedAt;

        boolean loop = true;
        boolean mirror =
                art.mirror;

        if (!fighter.alive()) {

            long since =
                    now - deaths.getOrDefault(
                            fighter,
                            now
                    );

            if (art.dead != null) {
                clip = art.dead;
                time = since;
                loop = false;
            }

            float alpha =
                    art.dead == null
                            ? Math.max(
                            0,
                            1 - since / 350f
                    )
                            : 1f;

            g.setComposite(
                    AlphaComposite
                            .SrcOver
                            .derive(alpha)
            );

        } else if (
                fighter == actor
                        && phase > 0) {

            clip = phase == 2
                    ? art.attack
                    : art.move;

            time =
                    now - phaseStart;

            if (phase == 2) {

                time =
                        (long) (
                                time
                                        * ATTACK_SPEED
                        );

                loop = false;
            }

            if (phase == 3) {
                mirror = !mirror;
            }
        }

        if (canAct()
                && fighter.alive()
                && !fighter.player
                && (
                fighter == target
                        || choosing
                        || fighter
                        == enemyAt(mouse)
        )) {

            g.setColor(ACCENT);

            g.setStroke(
                    new BasicStroke(
                            fighter == target
                                    ? 3
                                    : 1
                    )
            );

            g.drawOval(
                    point.x - 43,
                    point.y - 9,
                    86,
                    22
            );
        }

        if (fighter == victim
                && hitAt > 0
                && now - hitAt < 300) {

            point.x +=
                    now / 60 % 2 == 0
                            ? -3
                            : 3;
        }

        double scale =
                spriteHeight(fighter)
                        / art.idle.bodyHeight;

        clip.draw(
                g,
                point.x,
                point.y,
                scale,
                mirror,
                time,
                loop
        );

        int nameWidth =
                g.getFontMetrics(
                                new Font(
                                        "Dialog",
                                        Font.BOLD,
                                        14
                                )
                        )
                        .stringWidth(
                                fighter.name
                        );

        text(
                g,
                fighter.name,
                point.x
                        - nameWidth / 2,
                point.y + 26,
                14,
                TEXT
        );

        if (!fighter.player) {

            hpBar(
                    g,
                    point.x - 36,
                    point.y + 33,
                    72,
                    5,
                    fighter
            );
        }

        g.dispose();
    }

    // ---------- TURN TRACKER ----------

    private void drawTracker(
            Graphics2D g) {

        text(
                g,
                "TURN ORDER",
                24,
                58,
                12,
                TEXT
        );

        List<Fighter> next =
                battle.preview(5);

        int width =
                Math.min(
                        145,
                        (int) (
                                getWidth()
                                        * .72
                                        / 5
                        )
                );

        for (int i = 0;
             i < next.size();
             i++) {

            Fighter fighter =
                    next.get(i);

            Rectangle rectangle =
                    new Rectangle(
                            24
                                    + i
                                    * (width + 6),
                            68,
                            width,
                            75
                    );

            panel(
                    g,
                    rectangle,
                    fighter.player
                            ? ACCENT
                            : ENEMY
            );

            GameAssets.Art art =
                    assets.art.get(fighter);

            art.idle.draw(
                    g,
                    rectangle.x + 25,
                    rectangle.y + 47,
                    34.0
                            / art.idle.bodyHeight,
                    art.mirror,
                    System.currentTimeMillis()
                            - openedAt,
                    true
            );

            text(
                    g,
                    i == 0
                            ? "NOW"
                            : "+" + i,
                    rectangle.x + 53,
                    rectangle.y + 22,
                    13,
                    fighter.player
                            ? ACCENT
                            : ENEMY
            );

            text(
                    g,
                    "SPD " + fighter.speed,
                    rectangle.x + 53,
                    rectangle.y + 41,
                    11,
                    TEXT
            );

            text(
                    g,
                    fighter.name,
                    rectangle.x + 8,
                    rectangle.y + 65,
                    12,
                    TEXT
            );
        }
    }

    // ---------- PLAYER CARDS ----------

    private void drawCard(
            Graphics2D g,
            int index) {

        Fighter fighter =
                battle.players.get(index);

        Rectangle rectangle =
                cardBox(index);

        panel(
                g,
                rectangle,

                !battle.ended
                        && fighter
                        == battle.current()

                        ? ACCENT
                        : new Color(
                        79,
                        111,
                        130
                )
        );

        BufferedImage portrait =
                assets.art
                        .get(fighter)
                        .portrait;

        if (portrait != null) {

            double scale =
                    Math.min(
                            45.0
                                    / portrait
                                    .getWidth(),

                            35.0
                                    / portrait
                                    .getHeight()
                    );

            g.drawImage(
                    portrait,
                    rectangle.x + 8,
                    rectangle.y + 9,

                    (int) (
                            portrait
                                    .getWidth()
                                    * scale
                    ),

                    (int) (
                            portrait
                                    .getHeight()
                                    * scale
                    ),

                    null
            );
        }

        text(
                g,
                fighter.name,
                rectangle.x + 54,
                rectangle.y + 29,
                12,
                TEXT
        );

        hpBar(
                g,
                rectangle.x + 9,
                rectangle.y + 54,
                rectangle.width - 18,
                7,
                fighter
        );

        text(
                g,
                fighter.hp
                        + " / "
                        + fighter.maxHp
                        + " HP",

                rectangle.x + 9,
                rectangle.y + 81,
                12,
                TEXT
        );

        text(
                g,
                "ATK "
                        + fighter.attack
                        + "  DEF "
                        + fighter.defense,

                rectangle.x + 9,
                rectangle.y + 100,
                10,
                TEXT
        );

        text(
                g,
                "SPD "
                        + fighter.speed,

                rectangle.x + 9,
                rectangle.y + 116,
                10,
                TEXT
        );
    }

    // ---------- DRAWING HELPERS ----------

    private void hpBar(
            Graphics2D g,
            int x,
            int y,
            int width,
            int height,
            Fighter fighter) {

        g.setColor(
                new Color(
                        7,
                        19,
                        27
                )
        );

        g.fillRect(
                x,
                y,
                width,
                height
        );

        g.setColor(HP);

        g.fillRect(
                x,
                y,

                (int) (
                        width
                                * fighter.hp
                                / (double)
                                fighter.maxHp
                ),

                height
        );
    }

    private void panel(
            Graphics2D g,
            Rectangle rectangle,
            Color border) {

        g.setColor(PANEL);

        g.fillRect(
                rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height
        );

        g.setColor(border);

        g.setStroke(
                new BasicStroke(1.5f)
        );

        g.drawRect(
                rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height
        );
    }

    private void text(
            Graphics2D g,
            String text,
            int x,
            int y,
            int size,
            Color color) {

        g.setFont(
                new Font(
                        "Dialog",
                        Font.BOLD,
                        size
                )
        );

        g.setColor(color);

        g.drawString(
                text,
                x,
                y
        );
    }

    private void wrap(
            Graphics2D graphics,
            String message,
            Rectangle rectangle) {

        Graphics2D g =
                (Graphics2D)
                        graphics.create();

        g.clipRect(
                rectangle.x + 10,
                rectangle.y + 8,
                rectangle.width - 20,
                rectangle.height - 16
        );

        g.setFont(
                new Font(
                        "Dialog",
                        Font.PLAIN,
                        16
                )
        );

        g.setColor(TEXT);

        int x =
                rectangle.x + 13;

        int y =
                rectangle.y + 26;

        for (String paragraph :
                message.split("\n")) {

            String line = "";

            for (String word :
                    paragraph.split(" ")) {

                if (g.getFontMetrics()
                        .stringWidth(
                                line + word
                        )
                        > rectangle.width - 26) {

                    g.drawString(
                            line,
                            x,
                            y
                    );

                    y += 21;
                    line = "";
                }

                line += word + " ";
            }

            g.drawString(
                    line,
                    x,
                    y
            );

            y += 21;
        }

        g.dispose();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }
}