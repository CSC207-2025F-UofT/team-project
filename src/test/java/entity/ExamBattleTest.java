package entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ExamBattleTest {
    @Test
    void answersDamageTheRightSide() {
        ExamBattle battle = new ExamBattle();
        battle.submitAnswer(true);
        assertEquals(40, battle.getBossHp());
        assertEquals(60, battle.getPlayerHp());

        battle.submitAnswer(false);
        assertEquals(40, battle.getBossHp());
        assertEquals(40, battle.getPlayerHp());
        assertEquals(2, battle.getAnsweredCount());
        assertEquals(1, battle.getCorrectCount());
        assertFalse(battle.isFinished());
        assertFalse(battle.hasWon());
    }

    @ParameterizedTest
    @CsvSource({"true, 0, 60, 3", "false, 60, 0, 0"})
    void threeHitsEndBattle(boolean correct, int bossHp, int playerHp, int correctCount) {
        ExamBattle battle = new ExamBattle();
        for (int i = 0; i < 3; i++) {
            battle.submitAnswer(correct);
        }

        assertEquals(bossHp, battle.getBossHp());
        assertEquals(playerHp, battle.getPlayerHp());
        assertEquals(correctCount, battle.getCorrectCount());
        assertEquals(correct, battle.hasWon());
        assertTrue(battle.isFinished());
        assertThrows(IllegalStateException.class, () -> battle.submitAnswer(true));
        assertEquals(3, battle.getAnsweredCount());
        assertEquals(bossHp, battle.getBossHp());
        assertEquals(playerHp, battle.getPlayerHp());
    }

    @ParameterizedTest
    @CsvSource({"true, 0, 20, 3", "false, 20, 0, 2"})
    void fifthAnswerDecidesBattle(boolean correct, int bossHp, int playerHp, int correctCount) {
        ExamBattle battle = new ExamBattle();
        battle.submitAnswer(true);
        battle.submitAnswer(false);
        battle.submitAnswer(true);
        battle.submitAnswer(false);
        assertFalse(battle.isFinished());

        battle.submitAnswer(correct);
        assertEquals(5, battle.getAnsweredCount());
        assertEquals(correctCount, battle.getCorrectCount());
        assertEquals(bossHp, battle.getBossHp());
        assertEquals(playerHp, battle.getPlayerHp());
        assertTrue(battle.isFinished());
        assertEquals(correct, battle.hasWon());
        assertThrows(IllegalStateException.class, () -> battle.submitAnswer(false));
        assertEquals(5, battle.getAnsweredCount());
    }
}
