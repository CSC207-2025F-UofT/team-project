import java.util.*;

public class Battle {

    public static class Fighter {
        public final String name;
        public final boolean player;
        public final int maxHp, attack, defense, speed;

        public int hp;
        public boolean guarding;

        public Fighter(String name, boolean player, int hp,
                       int attack, int defense, int speed) {
            this.name = name;
            this.player = player;
            this.hp = this.maxHp = hp;
            this.attack = attack;
            this.defense = defense;
            this.speed = speed;
        }

        public boolean alive() {
            return hp > 0;
        }
    }

    public final List<Fighter> players;
    public final List<Fighter> enemies;

    private final List<Fighter> order = new ArrayList<>();
    private int cursor;

    public boolean ended;

    public Battle(List<Fighter> players, List<Fighter> enemies) {
        this.players = players;
        this.enemies = enemies;

        order.addAll(players);
        order.addAll(enemies);

        Collections.shuffle(order);

        order.sort(Comparator.comparingInt(
                (Fighter f) -> f.speed).reversed());
    }

    public Fighter current() {
        for (int i = 0; i < order.size(); i++) {
            Fighter f = order.get(cursor);

            if (f.alive()) {
                return f;
            }

            cursor = (cursor + 1) % order.size();
        }

        return null;
    }

    public List<Fighter> preview(int count) {
        List<Fighter> result = new ArrayList<>();

        if (ended || current() == null) {
            return result;
        }

        for (int i = 0; result.size() < count; i++) {
            Fighter f = order.get((cursor + i) % order.size());

            if (f.alive()) {
                result.add(f);
            }
        }

        return result;
    }

    public String attack(Fighter target) {
        Fighter actor = current();

        if (ended || actor == null || target == null
                || !target.alive()
                || actor.player == target.player
                || !order.contains(target)) {
            throw new IllegalStateException("Invalid attack target");
        }

        double guard = target.guarding ? 0.5 : 1;

        int damage = Math.max(1, (int) Math.round(
                (8 + 16.0 * actor.attack
                        / Math.max(1, target.defense)) * guard));

        damage = Math.min(damage, target.hp);

        target.hp -= damage;
        target.guarding = false;

        return actor.name + " hits " + target.name
                + " for " + damage + "."
                + (target.alive()
                ? ""
                : " " + target.name + " is defeated!");
    }

    public String defend() {
        Fighter actor = current();
        actor.guarding = true;

        return actor.name + " braces for the next hit.";
    }

    public void next() {
        ended = players.stream().noneMatch(Fighter::alive)
                || enemies.stream().noneMatch(Fighter::alive);

        if (!ended) {
            cursor = (cursor + 1) % order.size();
            current().guarding = false;
        }
    }

    public String prompt() {
        if (ended) {
            return players.stream().anyMatch(Fighter::alive)
                    ? "Victory!"
                    : "Your party was defeated.";
        }

        Fighter f = current();

        return f.name + "'s turn."
                + (f.player ? " Choose Attack or Defend." : "");
    }
}