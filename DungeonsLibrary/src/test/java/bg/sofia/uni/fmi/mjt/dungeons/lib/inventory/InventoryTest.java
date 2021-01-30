package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory;

import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.HealthPotion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Item;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.ManaPotion;
import bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items.Spell;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InventoryTest {

    private static final int INVENTORY_LIMIT = 9;

    private static final Item dummyItem1 = new HealthPotion();
    private static final Item dummyItem2 = new ManaPotion();
    private static final Item dummyItem3 = new Spell(1, 10, 100);

    private Inventory testInventory;


    @Before
    public void setUp() {
        testInventory = new Inventory();
    }

    @Test
    public void testInventoryLimit() {
        for (int i = 1; i <= INVENTORY_LIMIT + 1; i++) {
            testInventory.addItemToInventory(dummyItem1);
        }
        assertEquals(INVENTORY_LIMIT,testInventory.currentSize());
    }

    @Test
    public void testGetItemWhenIndexIsOutOfBounds() {
        assertNull(testInventory.getItem(10));
    }

    @Test
    public void testGetItemWhenItemsCountIsNotEnough() {
        testInventory.addItemToInventory(dummyItem1);
        assertNull(testInventory.getItem(2));
    }

    @Test
    public void testGetItemOK() {
        testInventory.addItemToInventory(dummyItem1);
        assertEquals(dummyItem1,testInventory.getItem(1));
    }

    @Test
    public void testRemoveItemWhenIndexIsOutOfBounds() {
        assertNull(testInventory.removeItem(10));
    }

    @Test
    public void testRemoveItemWhenItemsCountIsNotEnough() {
        testInventory.addItemToInventory(dummyItem1);
        assertNull(testInventory.removeItem(2));
    }

    @Test
    public void testRemoveItemOK() {
        testInventory.addItemToInventory(dummyItem1);
        assertEquals(dummyItem1,testInventory.removeItem(1));
        assertEquals(0,testInventory.currentSize());
    }

    @Test
    public void testRemoveRandomItemWhenInventoryIsEmpty() {
        testInventory.removeRandomItem(); // should not throw an out of bounds exception
    }

    @Test
    public void testRemoveRandomItemOK() {
        testInventory.addItemToInventory(dummyItem1);
        assertEquals(1,testInventory.currentSize());
        testInventory.removeRandomItem();
        assertEquals(0,testInventory.currentSize());
    }
}
