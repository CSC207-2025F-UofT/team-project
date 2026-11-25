package app;

import data_access.GameAssets;
import data_access.GameDataAccessInterface;
import data_access.GameDataAccessObject;

import entity.Battle;

import use_case.Combat;

import view.BattleView;
import view.BattleViewModel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                GameDataAccessInterface dataAccess =
                        new GameDataAccessObject();

                GameAssets assets = dataAccess.load();

                Battle battle = assets.battle;

                BattleViewModel viewModel =
                        new BattleViewModel();

                Combat.OutputBoundary presenter =
                        new Combat.Presenter(viewModel);

                Combat.Action attack =
                        new Combat.Attack(
                                battle,
                                presenter
                        );

                Combat.Action defend =
                        new Combat.Defend(
                                battle,
                                presenter
                        );

                Combat.Action skill =
                        new Combat.Skill(
                                battle,
                                presenter
                        );

                Map<String, Combat.Action> actions =
                        new LinkedHashMap<>();

                actions.put("Attack", attack);
                actions.put("Defend", defend);
                actions.put("Skill", skill);

                Combat.InputBoundary interactor =
                        new Combat.Interactor();

                Combat.Controller controller =
                        new Combat.Controller(
                                interactor
                        );

                BattleView view =
                        new BattleView(
                                assets,
                                controller,
                                viewModel,
                                actions
                        );


                JFrame window =
                        new JFrame("RPG Battle");

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

            } catch (Exception exception) {

                exception.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "Could not load game:\n"
                                + exception.getMessage(),
                        "Loading Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}