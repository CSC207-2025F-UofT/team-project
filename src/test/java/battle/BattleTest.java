package battle;

import entity.*;
import use_case.Battle.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {

    // ==================== Mock Classes ====================

    /**
     * Mock implementation of BattleOutputBoundary (Presenter)
     */
    private static class MockBattlePresenter implements BattleOutputBoundary {
        private boolean winViewPrepared = false;
        private boolean lossViewPrepared = false;
        private boolean quizViewPrepared = false;
        private int updateUserTurnStateCalled = 0;
        private int updateMonsterTurnStateCalled = 0;
        private BattleOutputData lastOutputData = null;

        @Override
        public void prepareWinView(BattleOutputData outputData) {
            winViewPrepared = true;
            lastOutputData = outputData;
        }

        @Override
        public void prepareLossView(BattleOutputData outputData) {
            lossViewPrepared = true;
            lastOutputData = outputData;
        }

        @Override
        public void prepareQuizView() {
            quizViewPrepared = true;
        }

        @Override
        public void updateUserTurnState(BattleOutputData outputData) {
            updateUserTurnStateCalled++;
            lastOutputData = outputData;
        }

        @Override
        public void updateMonsterTurnState(BattleOutputData outputData) {
            updateMonsterTurnStateCalled++;
            lastOutputData = outputData;
        }

        public boolean isWinViewPrepared() { return winViewPrepared; }
        public boolean isLossViewPrepared() { return lossViewPrepared; }
        public boolean isQuizViewPrepared() { return quizViewPrepared; }
        public int getUpdateUserTurnStateCallCount() { return updateUserTurnStateCalled; }
        public BattleOutputData getLastOutputData() { return lastOutputData; }
    }

    /**
     * Mock implementation of BattleUserDataAccessInterface
     */
    private static class MockBattleDataAccess implements BattleUserDataAccessInterface {
        private AdventureGame game;
        private boolean gameSaved = false;
        private boolean gameDataLoaded = false;

        public MockBattleDataAccess(AdventureGame game) {
            this.game = game;
        }

        @Override
        public AdventureGame getGame() {
            return game;
        }

        @Override
        public void saveGame(AdventureGame game) {
            this.game = game;
            gameSaved = true;
        }

        @Override
        public void loadGameData() {
            gameDataLoaded = true;
        }

        public boolean isGameSaved() { return gameSaved; }
        public boolean isGameDataLoaded() { return gameDataLoaded; }
    }

    /**
     * Test-friendly Monster that avoids API calls
     */
    private static class TestMonster extends Monster {
        private double hp;
        private Spells[] spells;

        public TestMonster(double hp, Spells[] spells, String name) {
            this.hp = hp;
            this.spells = spells;
            this.NAME = name;
        }

        @Override
        public double getHP() { return hp; }

        @Override
        public void HPDecrease(double DMG) {
            hp -= DMG;
            if (hp < 0) hp = 0;
        }

        @Override
        public Spells[] getSpells() { return spells; }

        @Override
        public Spells chooseSpell() { return spells[0]; }

        @Override
        public boolean isAlive() { return hp > 0; }
    }

    // ==================== Test Fields ====================

    private MockBattlePresenter mockPresenter;
    private MockBattleDataAccess mockDataAccess;
    private BattleInteractor interactor;

    @BeforeEach
    void setUp() {
        // 创建 User
        User user = new User();

        // 创建 Location（monster 可以为 null）
        Location location = new Location("TestLocation", 0.0, 0.0, null);

        // 创建 Location 列表
        List<Location> locations = new ArrayList<>();
        locations.add(location);

        // 创建 GameMap
        GameMap gameMap = new GameMap(locations, 0);

        // 创建 AdventureGame
        AdventureGame game = new AdventureGame(user, gameMap);

        // 创建 mocks 和 interactor
        mockPresenter = new MockBattlePresenter();
        mockDataAccess = new MockBattleDataAccess(game);
        interactor = new BattleInteractor(mockDataAccess, mockPresenter);
    }

    // ==================== Tests ====================

    @Test
    void testSwitchToQuizView() {
        interactor.switchToQuizView();

        assertTrue(mockPresenter.isQuizViewPrepared(),
                "prepareQuizView should be called");
    }

    @Test
    void testExecute_UserWins_WhenMonsterDies() {
        // Arrange
        Spells[] monsterSpells = {new Spells("Fireball", 5)};
        TestMonster weakMonster = new TestMonster(5.0, monsterSpells, "WeakMonster");
        User user = new User();

        BattleInputData inputData = new BattleInputData(user, weakMonster, true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(weakMonster.isAlive(), "Monster should be dead");
        assertTrue(mockPresenter.isWinViewPrepared(), "Win view should be prepared");
        assertFalse(mockPresenter.isLossViewPrepared(), "Loss view should NOT be prepared");
        assertTrue(mockDataAccess.isGameSaved(), "Game should be saved after victory");
    }

    @Test
    void testExecute_UserLoses_WhenUserDies() {
        // Arrange
        Spells[] monsterSpells = {new Spells("DeathBlow", 100)};
        TestMonster strongMonster = new TestMonster(100.0, monsterSpells, "StrongMonster");
        User user = new User();

        BattleInputData inputData = new BattleInputData(user, strongMonster, true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(strongMonster.isAlive(), "Monster should still be alive");
        assertFalse(user.isAlive(), "User should be dead");
        assertTrue(mockPresenter.isLossViewPrepared(), "Loss view should be prepared");
        assertTrue(mockDataAccess.isGameDataLoaded(), "Game data should be reloaded after loss");
    }

    @Test
    void testExecute_NoDamageDealt_WhenQuizFailed() {
        // Arrange
        Spells[] monsterSpells = {new Spells("Scratch", 1)};
        TestMonster monster = new TestMonster(20.0, monsterSpells, "Monster");
        double initialMonsterHP = monster.getHP();
        User user = new User();

        BattleInputData inputData = new BattleInputData(user, monster, false);

        // Act
        interactor.execute(inputData);

        // Assert
        assertEquals(initialMonsterHP, monster.getHP(),
                "Monster HP should be unchanged when quiz failed");
    }

    @Test
    void testExecute_BattleContinues_WhenBothAlive() {
        // Arrange
        Spells[] monsterSpells = {new Spells("Poke", 1)};
        TestMonster monster = new TestMonster(50.0, monsterSpells, "Monster");
        User user = new User();

        BattleInputData inputData = new BattleInputData(user, monster, true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(monster.isAlive(), "Monster should still be alive");
        assertTrue(user.isAlive(), "User should still be alive");
        assertFalse(mockPresenter.isWinViewPrepared(), "Win view should NOT be prepared");
        assertFalse(mockPresenter.isLossViewPrepared(), "Loss view should NOT be prepared");
        assertEquals(2, mockPresenter.getUpdateUserTurnStateCallCount(),
                "updateUserTurnState should be called twice");
    }

    @Test
    void testExecute_CorrectDamageDealt() {
        // Arrange
        Spells[] monsterSpells = {new Spells("Tap", 1)};
        TestMonster monster = new TestMonster(50.0, monsterSpells, "Monster");
        User user = new User(); // 默认 DMG = 8

        BattleInputData inputData = new BattleInputData(user, monster, true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertEquals(42.0, monster.getHP(), 0.01,
                "Monster should take 8 damage (50 - 8 = 42)");
    }

    @Test
    void testExecute_DefenseReducesDamage() {
        // Arrange
        Spells[] monsterSpells = {new Spells("Hit", 10)};
        TestMonster monster = new TestMonster(100.0, monsterSpells, "Monster");
        User user = new User();
        user.addDEF(5);
        double initialHP = user.getHP();

        BattleInputData inputData = new BattleInputData(user, monster, true);

        // Act
        interactor.execute(inputData);

        // Assert: damage = 10 * (1 - 0.08 * 5) = 6
        double expectedDamage = 10 * (1 - 0.08 * 5);
        double actualDamage = initialHP - user.getHP();
        assertEquals(expectedDamage, actualDamage, 0.01,
                "Defense should reduce damage taken");
    }

    @Test
    void testExecute_BonusHPAbsorbsDamage() {
        // Arrange
        Spells[] monsterSpells = {new Spells("Slap", 5)};
        TestMonster monster = new TestMonster(100.0, monsterSpells, "Monster");
        User user = new User();
        double initialHP = user.getHP();
        user.addBonusHP(10);

        BattleInputData inputData = new BattleInputData(user, monster, true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertEquals(initialHP, user.getHP(), 0.01,
                "Main HP should be unchanged when bonus HP absorbs damage");
        assertTrue(user.getBonusHP() < 10,
                "Bonus HP should be reduced");
    }
}