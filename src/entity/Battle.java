package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Battle {

    public final List<Fighter> players;
    public final List<Fighter> enemies;

    private final List<Fighter> turnOrder;

    private int turnIndex;

    public boolean ended;


    public Battle(
            List<Fighter> players,
            List<Fighter> enemies) {

        this.players = players;
        this.enemies = enemies;

        this.turnOrder = new ArrayList<>();

        turnOrder.addAll(players);
        turnOrder.addAll(enemies);

        // Faster fighters act first.
        turnOrder.sort(
                Comparator.comparingInt(
                        (Fighter fighter) -> fighter.speed
                ).reversed()
        );

        turnIndex = 0;
        ended = false;

        skipDeadFighters();
    }


    // --------------------------------------------------
    // CURRENT TURN
    // --------------------------------------------------

    public Fighter current() {

        if (ended || turnOrder.isEmpty()) {
            return null;
        }

        return turnOrder.get(turnIndex);
    }


    // --------------------------------------------------
    // ATTACK
    // --------------------------------------------------

    public String attack(Fighter target) {

        Fighter attacker = current();

        if (attacker == null) {
            return "Nobody can attack.";
        }

        if (target == null || !target.alive()) {
            return "Invalid target.";
        }

        // Basic damage calculation.
        int damage = Math.max(
                1,
                attacker.attack - target.defense / 2
        );

        // Defending reduces damage.
        if (target.guarding) {
            damage = Math.max(1, damage / 2);
            target.stopDefending();
        }

        target.takeDamage(damage);

        String message =
                attacker.name
                        + " dealt "
                        + damage
                        + " damage to "
                        + target.name
                        + ".";

        if (!target.alive()) {
            message += "\n"
                    + target.name
                    + " was defeated!";
        }

        updateBattleState();

        return message;
    }


    // --------------------------------------------------
    // DEFEND
    // --------------------------------------------------

    public String defend() {

        Fighter fighter = current();

        if (fighter == null) {
            return "Nobody can defend.";
        }

        fighter.defend();

        return fighter.name
                + " is defending!";
    }


    // --------------------------------------------------
    // NEXT TURN
    // --------------------------------------------------

    public void next() {

        if (ended || turnOrder.isEmpty()) {
            return;
        }

        updateBattleState();

        if (ended) {
            return;
        }

        turnIndex =
                (turnIndex + 1) % turnOrder.size();

        skipDeadFighters();
    }


    private void skipDeadFighters() {

        if (turnOrder.isEmpty()) {
            return;
        }

        int checked = 0;

        while (!turnOrder.get(turnIndex).alive()
                && checked < turnOrder.size()) {

            turnIndex =
                    (turnIndex + 1) % turnOrder.size();

            checked++;
        }
    }


    // --------------------------------------------------
    // TURN PREVIEW
    // --------------------------------------------------

    public List<Fighter> preview(int amount) {

        List<Fighter> result =
                new ArrayList<>();

        if (turnOrder.isEmpty()) {
            return result;
        }

        int index = turnIndex;

        while (result.size() < amount) {

            Fighter fighter =
                    turnOrder.get(index);

            if (fighter.alive()) {
                result.add(fighter);
            }

            index =
                    (index + 1) % turnOrder.size();

            // Avoid infinite loop if everyone is dead.
            if (index == turnIndex
                    && result.isEmpty()) {
                break;
            }
        }

        return result;
    }


    // --------------------------------------------------
    // BATTLE STATE
    // --------------------------------------------------

    private void updateBattleState() {

        boolean playersAlive =
                players.stream().anyMatch(Fighter::alive);

        boolean enemiesAlive =
                enemies.stream().anyMatch(Fighter::alive);

        ended = !playersAlive || !enemiesAlive;
    }


    // --------------------------------------------------
    // MESSAGE FOR UI
    // --------------------------------------------------

    public String prompt() {

        if (players.stream().noneMatch(Fighter::alive)) {
            return "Your party was defeated.";
        }

        if (enemies.stream().noneMatch(Fighter::alive)) {
            return "Enemy party defeated!";
        }

        Fighter fighter = current();

        if (fighter == null) {
            return "";
        }

        if (fighter.player) {
            return fighter.name + "'s turn.";
        }

        return fighter.name + " is preparing to attack.";
    }
}