package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// FightableActor functionality is tested here
public class MinionTest {

    private static final int XP_REWARD_PER_MINION_LVL = 20;

    private static final Player dummyPlayer = new Player();

    private Minion testMinion = new Minion(1);

    @Before
    public void setUp() {
        dummyPlayer.setStats(100, 100, 40, 10);
        testMinion.setStats(100, 10, 50, 10);
    }

    @Test
    public void testDealDamageBaseCase() {
        testMinion.dealDamage(dummyPlayer);
        assertEquals(dummyPlayer.health + dummyPlayer.defense - testMinion.attack, dummyPlayer.currentHealth);
    }

    @Test
    public void testTakeDamageBaseCase() {
        testMinion.takeDamage(dummyPlayer.attack);
        assertEquals(testMinion.health + testMinion.defense - dummyPlayer.attack, testMinion.currentHealth);
    }

    @Test
    public void testTakeDamageOverflow() {
        testMinion.takeDamage(120);
        assertEquals(0,testMinion.currentHealth);
    }

    @Test
    public void testHealOverflow() {
        testMinion.heal(10);
        assertEquals(testMinion.health,testMinion.currentHealth);
    }

    @Test
    public void testReplenishOverflow() {
        testMinion.replenish(10);
        assertEquals(testMinion.mana,testMinion.currentMana);
    }

    @Test
    public void testDrainManaOverflow() {
        testMinion.drainMana(testMinion.currentMana+1);
        assertEquals(0,testMinion.currentMana);
    }

    @Test
    public void testXPReward() {
        assertEquals(XP_REWARD_PER_MINION_LVL,testMinion.XPReward());
        Minion level5Minion = new Minion(5);
        assertEquals(XP_REWARD_PER_MINION_LVL*5,level5Minion.XPReward());
    }
}
