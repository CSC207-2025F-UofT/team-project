package interface_adapter.Battle;

import entity.Monster;
import entity.User;

/**
 * State object for the Battle View.
 *
 */
public class BattleState {
    private User user = null;
    private Monster monster = null;
    private double userHp = 0;
    private double monsterHP = 0;
    private String battleMessage = "";
    private boolean battleEnded = false;
    private boolean userWon = false;

    private boolean quizResult = false;
    private boolean justFinishedQuiz = false;

    // Monster getters and setters
    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
        if (monster != null) {
            this.monsterHP = monster.getHP();
        }
    }

    public double getMonsterHP() {
        return monsterHP;
    }

    public void setMonsterHP(double monsterHP) {
        this.monsterHP = monsterHP;
    }


    // User getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userHp = user.getHP();
        }
    }

    public double getUserHp() {
        return userHp;
    }

    public void setUserHP(double userHp) {
        this.userHp = userHp;
    }

    // Battle message
    public String getBattleMessage() {
        return battleMessage;
    }

    public void setBattleMessage(String battleMessage) {
        this.battleMessage = battleMessage;
    }

    // Battle status
    public boolean isBattleEnded() {
        return battleEnded;
    }

    public void setBattleEnded(boolean battleEnded) {
        this.battleEnded = battleEnded;
    }

    public boolean isUserWon() {
        return userWon;
    }

    public void setUserWon(boolean userWon) {
        this.userWon = userWon;
    }

    public String getMonsterName() {
        return monster != null ? monster.NAME : "Monster";
    }


    public boolean isQuizResult() {
        return quizResult;
    }

    public void setQuizResult(boolean quizResult) {
        this.quizResult = quizResult;
    }

    public boolean isJustFinishedQuiz() {
        return justFinishedQuiz;
    }

    public void setJustFinishedQuiz(boolean justFinishedQuiz) {
        this.justFinishedQuiz = justFinishedQuiz;
    }
}
