package bg.sofia.uni.fmi.mjt.dungeons;

import bg.sofia.uni.fmi.mjt.dungeons.exceptions.NoSuchPlayerException;
import bg.sofia.uni.fmi.mjt.dungeons.exceptions.PlayerCapacityReachedException;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.channels.SocketChannel;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PlayerManagerTest {

    private static final int PLAYER_COUNT_LIMIT = 9;

    @Mock
    private SocketChannel mockChannel1;
    @Mock
    private SocketChannel mockChannel2;

    private PlayerManager testPlayerManager;

    @Before
    public void setUp() {
        testPlayerManager = new PlayerManager();
    }

    @Test(expected = PlayerCapacityReachedException.class)
    public void testPlayerLimit() throws Exception {
        for (int i = 1; i <= PLAYER_COUNT_LIMIT + 1; i++) {
            testPlayerManager.createNewPlayer(mockChannel1);
        }
    }

    @Test
    public void testIDGeneration() throws Exception {
        Player player1 = testPlayerManager.createNewPlayer(mockChannel1);
        Player player2 = testPlayerManager.createNewPlayer(mockChannel2);
        assertEquals(1, player1.id());
        assertEquals(2, player2.id());

        testPlayerManager.removePlayer(player1);
        Player player3 = testPlayerManager.createNewPlayer(mockChannel1);
        assertEquals(1, player3.id());
    }

    @Test(expected = NoSuchPlayerException.class)
    public void testGetPlayerByChannelWhenChannelIsNotRegistered() throws Exception {
        testPlayerManager.getPlayerByChannel(mockChannel1);
    }

    @Test
    public void testGetPlayerByChannelOK() throws Exception {
        Player player1 = testPlayerManager.createNewPlayer(mockChannel1);
        assertEquals(player1, testPlayerManager.getPlayerByChannel(mockChannel1));
    }

    @Test
    public void testGetPlayerChannel() throws Exception {
        Player player = testPlayerManager.createNewPlayer(mockChannel1);
        assertEquals(mockChannel1, testPlayerManager.getPlayerChannel(player));
    }

    @Test
    public void testPlayerStorage() throws Exception {
        Player player1 = testPlayerManager.createNewPlayer(mockChannel1);
        Player player2 = testPlayerManager.createNewPlayer(mockChannel2);

        Set<Player> allPlayers = testPlayerManager.getAllPlayers();
        assertEquals(2,allPlayers.size());
        assertTrue(allPlayers.contains(player1));
        assertTrue(allPlayers.contains(player2));
    }
}
