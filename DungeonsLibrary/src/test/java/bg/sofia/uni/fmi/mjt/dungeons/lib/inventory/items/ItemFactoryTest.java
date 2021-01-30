package bg.sofia.uni.fmi.mjt.dungeons.lib.inventory.items;

import bg.sofia.uni.fmi.mjt.dungeons.lib.enums.ItemType;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ItemFactoryTest {

    @Test
    public void testItemCreationByType() {
        Arrays.stream(ItemType.values())
                .forEach(type -> assertEquals(type, ItemFactory.ofType(type).type()));
    }
}
