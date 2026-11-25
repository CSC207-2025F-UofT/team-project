package use_case;

import entity.Battle;
import entity.Fighter;
import view.BattleViewModel;

public class Combat {

    public interface InputBoundary {
        void execute(Action action, Fighter target);
    }

    public interface OutputBoundary {
        void success(String message);
        void fail(String message);
    }

    public enum AnimationType {
        ATTACK,
        SKILL,
        DEFEND
    }

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

        public abstract AnimationType animationType();
    }

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

        @Override
        public AnimationType animationType() {
            return AnimationType.ATTACK;
        }
    }

    public static class Skill extends Action {

        public Skill(
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

        @Override
        public AnimationType animationType() {
            return AnimationType.SKILL;
        }
    }

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
        @Override
        public AnimationType animationType() {
            return AnimationType.DEFEND;
        }
    }

    public static class Interactor
            implements InputBoundary {

        @Override
        public void execute(
                Action action,
                Fighter target) {

            if (action != null) {
                action.execute(target);
            }
        }
    }

    public static class Controller {

        private final InputBoundary inputBoundary;

        public Controller(InputBoundary inputBoundary) {
            this.inputBoundary = inputBoundary;
        }

        public void execute(
                Action action,
                Fighter target) {

            inputBoundary.execute(action, target);
        }
    }

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