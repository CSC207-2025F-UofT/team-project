package use_case.Battle;

import entity.AdventureGame;
import entity.Location;
import entity.Monster;
import entity.Spells;
import entity.User;

public class BattleInteractor implements BattleInputBoundary {
    private final BattleUserDataAccessInterface userDataAccessObject;
    private final BattleOutputBoundary battlePresenter;

    public BattleInteractor(BattleUserDataAccessInterface userDataAccessObject, BattleOutputBoundary battleOutputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        this.battlePresenter = battleOutputBoundary;
    }

    public void switchToQuizView(){
        battlePresenter.prepareQuizView();
    }

    /**
     * This is the final method that would be called in ca, if the user choose "FIGHT" then this method will lead to the
     * fight method.
     */
    @Override
    public void execute(BattleInputData inputData) {
        final User user = inputData.getUser();
        final Monster monster = inputData.getMonster();
        // TODO:Not sure if I need to create boolean or just use a method to get the boolean from DAO,
        //  since I don't have the DAO rn, I will just use the boolean for now.
        final boolean resultOfQuiz = inputData.getResultOfQuiz();

        // User's turn
        UserTurn(user, monster, resultOfQuiz);
        // Prepare final output
        BattleOutputData output = new BattleOutputData(user, monster);
        // Check if monster is defeated
        if (!monster.isAlive()) {
            // Remove the defeated monster from the current location
            AdventureGame game = userDataAccessObject.getGame();
            Location currentLocation = game.getGameMap().getCurrentLocation();
            currentLocation.setMonster(null);

            // Save the game state so defeated monsters don't respawn
            userDataAccessObject.saveGame(game);

            battlePresenter.prepareWinView(output);
            return;
        }
        // Monster's turn
        MonsterTurn(user, monster);
        // Prepare final output
        output = new BattleOutputData(user, monster);
        // Present final result
        if (!user.isAlive()) {
            battlePresenter.prepareLossView(output);
        }
    }

    /**
     * Monster's turn to attack the user, monster will randomly choose a spell and then attack.
     */
    private void MonsterTurn(User user, Monster monster) {
        Spells spell = monster.chooseSpell();
        double DMG = monster.attack(spell);
        user.HPDecrease(DMG);
        // notify presenter to update the view (After the User attack)
        BattleOutputData turnOutput = new BattleOutputData(user, monster);
        battlePresenter.updateUserTurnState(turnOutput);
    }

    /**
     * User's turn to attack the monster.
     */
    private void UserTurn(User user, Monster monster, boolean resultOfQuiz) {
        double DMG = 0;
        if(resultOfQuiz){
            DMG = user.getDMG();
        }
        monster.HPDecrease(DMG);
        // notify presenter to update the view (After the User attack)
        BattleOutputData turnOutput = new BattleOutputData(user, monster);
        battlePresenter.updateUserTurnState(turnOutput);
    }
}
