package bg.sofia.uni.fmi.mjt.dungeons.lib.actors;

import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Spell;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Weapon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayerTest {

    private static final int MAX_PLAYER_LEVEL = 10;

    private static final Minion dummyMinion = new Minion(1);
    private static final Weapon dummyWeapon = new Weapon(2, 20);
    private static final Spell dummySpell = new Spell(2, 50, 50);

    private Player testPlayer;

    @Before
    public void setUp() {
        testPlayer = new Player(1);
        dummyMinion.setStats(150, 0, 0, 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testIncreaseXPWithNegativeValue() {
        testPlayer.increaseXP(-1);
    }

    @Test
    public void testIncreaseXPLevelOverflow() {
        testPlayer.increaseXP(1000000);
        assertEquals(MAX_PLAYER_LEVEL, testPlayer.level());
    }

    @Test
    public void testIncreaseXPOK() {
        testPlayer.increaseXP(450);
        assertEquals(4, testPlayer.level()); // See REQUIRED_XP_FOR_LEVEL map inside Player.java
    }

    @Test
    public void testXPPercentage() {
        assertEquals(0, testPlayer.XPPercentage());
        testPlayer.increaseXP(50);
        assertEquals(50, testPlayer.XPPercentage());
        testPlayer.increaseXP(50);
        assertEquals(0, testPlayer.XPPercentage());
        testPlayer.increaseXP(100000);
        assertEquals(0, testPlayer.XPPercentage());
    }

    @Test
    public void testLevelingUpRefillsHealthAndMana() {
        testPlayer.takeDamage(10);
        testPlayer.drainMana(10);
        testPlayer.increaseXP(200);
        assertEquals(testPlayer.health, testPlayer.currentHealth);
        assertEquals(testPlayer.mana, testPlayer.currentMana);
    }

    @Test
    public void testEquipWeaponWhenWeaponLevelIsTooHigh() {
        equipDummyWeapon();
        assertNull(testPlayer.weapon());
        assertEquals(1, testPlayer.inventory().currentSize());
    }

    @Test
    public void testEquipWeaponOK() {
        testPlayer.increaseXP(1000);
        equipDummyWeapon();
        assertEquals(dummyWeapon, testPlayer.weapon());
        assertEquals(0, testPlayer.inventory().currentSize());
    }

    @Test
    public void testEquipSpellWhenSpellLevelIsTooHigh() {
        equipDummySpell();
        assertNull(testPlayer.spell());
        assertEquals(1, testPlayer.inventory().currentSize());
    }

    @Test
    public void testEquipSpellOK() {
        testPlayer.increaseXP(100);
        equipDummySpell();
        assertEquals(dummySpell, testPlayer.spell());
        assertEquals(0, testPlayer.inventory().currentSize());
    }

    @Test
    public void testDealDamageWithoutWeaponAndSpell() {
        testPlayer.dealDamage(dummyMinion);
        assertEquals(dummyMinion.health - testPlayer.attack, dummyMinion.currentHealth);
    }

    @Test
    public void testDealDamageWithWeaponWithoutSpell() {
        testPlayer.increaseXP(100); // In order to be able to equip the weapon
        equipDummyWeapon();
        testPlayer.dealDamage(dummyMinion);
        assertEquals(dummyMinion.health - testPlayer.attack - dummyWeapon.attack(), dummyMinion.currentHealth);
    }

    @Test
    public void testDealDamageWithSpellStrongerThanWeapon() {
        testPlayer.increaseXP(100); // In order to be able to equip the spell&weapon
        equipDummyWeapon();
        equipDummySpell();
        testPlayer.dealDamage(dummyMinion);
        assertEquals(dummyMinion.health - testPlayer.attack - dummySpell.damage(), dummyMinion.currentHealth);
        assertEquals(testPlayer.mana-dummySpell.manaCost(),testPlayer.currentMana);
    }

    @Test
    public void testDealDamageWithSpellStrongerThanWeaponWithNotEnoughMana() {
        testPlayer.increaseXP(100);
        equipDummyWeapon();
        equipDummySpell();
        testPlayer.drainMana(testPlayer.mana);
        testPlayer.dealDamage(dummyMinion);
        assertEquals(dummyMinion.health - testPlayer.attack - dummyWeapon.attack(), dummyMinion.currentHealth);
    }

    @Test
    public void testDrinkHealthPotion() {
        testPlayer.takeDamage(20);
        assertEquals(testPlayer.currentHealth, testPlayer.health + testPlayer.defense - 20);
        drinkHealingPotion();
        assertEquals(testPlayer.health, testPlayer.currentHealth);
    }

    @Test
    public void testDrinkManaPotion() {
        testPlayer.drainMana(20);
        assertEquals(testPlayer.currentMana, testPlayer.mana - 20);
        drinkManaPotion();
        assertEquals(testPlayer.mana, testPlayer.currentMana);
    }

    @Test
    public void testSufferDeathPenalty() {
        testPlayer.increaseXP(150);
        testPlayer.addItemToInventory(dummySpell);
        testPlayer.sufferDeathPenalty();

        assertEquals(2, testPlayer.level());
        assertEquals(0, testPlayer.XPPercentage());
        assertEquals(0, testPlayer.inventory().currentSize());
        assertEquals(testPlayer.health, testPlayer.currentHealth);
        assertEquals(testPlayer.mana, testPlayer.currentMana);
    }

    private void equipDummyWeapon() {
        testPlayer.addItemToInventory(dummyWeapon);
        testPlayer.useItemFromInventory(testPlayer.inventory().currentSize());
    }

    private void equipDummySpell() {
        testPlayer.addItemToInventory(dummySpell);
        testPlayer.useItemFromInventory(testPlayer.inventory().currentSize());
    }

    private void drinkHealingPotion() {
        testPlayer.addItemToInventory(new HealthPotion());
        testPlayer.useItemFromInventory(testPlayer.inventory().currentSize());
    }

    private void drinkManaPotion() {
        testPlayer.addItemToInventory(new ManaPotion());
        testPlayer.useItemFromInventory(testPlayer.inventory().currentSize());
    }
}
