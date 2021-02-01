package bg.sofia.uni.fmi.mjt.dungeons.fight;

import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Minion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Weapon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArenaTest {

    private Minion testMinion;
    private Player testPlayer1;
    private Player testPlayer2;

    @Before
    public void setUp() {
        testMinion = new Minion(1); // Initial stats are 75 0 25 10
        testPlayer1 = new Player(1); // Initial stats are 170 170 55 15
        testPlayer2 = new Player(2);
    }

    @Test
    public void testPlayerVsMinion() {
        FightResult fightResult = Arena.makeActorsFight(testPlayer1,testMinion);
        assertEquals(testPlayer1,fightResult.winner());
        assertEquals(testMinion,fightResult.loser());
    }

    @Test
    public void testPlayerVsPlayerEqualStats() {
        FightResult fightResult = Arena.makeActorsFight(testPlayer1,testPlayer2);
        assertEquals(testPlayer1,fightResult.winner());
        assertEquals(testPlayer2,fightResult.loser());
    }

    @Test
    public void testPlayerVsPlayerStrongerSubject() {
        testPlayer2.addItemToInventory(new Weapon(1,50));
        testPlayer2.useItemFromInventory(1);
        FightResult fightResult = Arena.makeActorsFight(testPlayer1,testPlayer2);
        assertEquals(testPlayer2,fightResult.winner());
        assertEquals(testPlayer1,fightResult.loser());
    }
}
