package use_case;

import entity.Battle;
import entity.Fighter;
import view.BattleViewModel;


// ======================================================
// COMBAT USE CASE
// ======================================================

public class Combat {

    // ---------------- INPUT BOUNDARY ----------------

    public interface InputBoundary {
        void attack(Fighter target);
        void defend();
        void skill(Fighter target);
    }


    // ---------------- OUTPUT BOUNDARY ----------------

    public interface OutputBoundary {
        void success(String message);
        void fail(String message);
    }


    // ---------------- ABSTRACT COMBAT ACTION ----------------

    public static abstract class Action {

        protected final Battle battle;
        protected final OutputBoundary presenter;

        protected Action(
                Battle battle,
                OutputBoundary presenter) {

            this.battle = battle;
            this.presenter = presenter;
        }

        public abstract void execute(Fighter target);
    }


    // ---------------- ATTACK ACTION ----------------

    public static class Attack extends Action {

        public Attack(
                Battle battle,
                OutputBoundary presenter) {

            super(battle, presenter);
        }

        @Override
        public void execute(Fighter target) {

            Fighter attacker = battle.current();

            if (attacker == null) {
                presenter.fail("No active fighter.");
                return;
            }

            if (target == null || !target.alive()) {
                presenter.fail("Choose a living target.");
                return;
            }

            String result = battle.attack(target);

            presenter.success(result);
        }
    }


    // ---------------- DEFEND ACTION ----------------

    public static class Defend extends Action {

        public Defend(
                Battle battle,
                OutputBoundary presenter) {

            super(battle, presenter);
        }

        @Override
        public void execute(Fighter target) {

            if (battle.current() == null) {
                presenter.fail("No active fighter.");
                return;
            }

            String result = battle.defend();

            presenter.success(result);
        }
    }

    public static class Skill extends Action {

        public Skill(Battle battle,
                     OutputBoundary presenter) {
            super(battle, presenter);
        }

        @Override
        public void execute(Fighter target) {
            // skill logic
            presenter.success("Skill used!");
        }
    }


    // ---------------- INTERACTOR ----------------

    public static class Interactor
            implements InputBoundary {

        private final Action attack;
        private final Action defend;
        private final Action skill;

        public Interactor(
                Action attack,
                Action defend,
                Action skill) {

            this.attack = attack;
            this.defend = defend;
            this.skill = skill;
        }

        @Override
        public void attack(Fighter target) {
            attack.execute(target);
        }

        @Override
        public void defend() {
            defend.execute(null);
        }

        @Override
        public void skill(Fighter target) {
            skill.execute(target);
        }
    }


    // ---------------- CONTROLLER ----------------

    public static class Controller {

        private final InputBoundary inputBoundary;

        public Controller(InputBoundary inputBoundary) {
            this.inputBoundary = inputBoundary;
        }

        public void attack(Fighter target) {
            inputBoundary.attack(target);
        }

        public void defend() {
            inputBoundary.defend();
        }

        public void skill(Fighter target) {
            inputBoundary.skill(target);
        }
    }


    // ---------------- PRESENTER ----------------

    public static class Presenter
            implements OutputBoundary {

        private final BattleViewModel viewModel;

        public Presenter(BattleViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        public void success(String message) {
            viewModel.setMessage(message);
        }

        @Override
        public void fail(String message) {
            viewModel.setMessage(message);
        }
    }
}