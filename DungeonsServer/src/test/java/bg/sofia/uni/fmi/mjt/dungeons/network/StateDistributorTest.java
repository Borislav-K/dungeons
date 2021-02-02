package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.GameMap;
import bg.sofia.uni.fmi.mjt.dungeons.PlayerManager;
import bg.sofia.uni.fmi.mjt.dungeons.io.PerformantByteArrayOutputStream;
import bg.sofia.uni.fmi.mjt.dungeons.lib.Position2D;
import bg.sofia.uni.fmi.mjt.dungeons.lib.actors.Player;
import bg.sofia.uni.fmi.mjt.dungeons.lib.network.PlayerSegment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class StateDistributorTest {

    @Mock
    private SocketChannel playerChannel;
    private PlayerSegment playerSegment;

    @Spy
    private PlayerManager playerManager = spy(new PlayerManager());
    @Spy
    private GameMap gameMap = spy(new GameMap());

    private StateDistributor testStateDistributor = new StateDistributor(playerManager, gameMap);

    @Before
    public void setUp() throws Exception {
        Player player = playerManager.createNewPlayer(playerChannel);
        player.setPosition(new Position2D(10,10));
        playerSegment = new PlayerSegment(player, gameMap.getPositionsWithActors());
    }

    @Test
    public void testStateDistributionWhenPlayerDisconnected() throws IOException {
        when(playerChannel.write(any(ByteBuffer.class))).thenThrow(IOException.class);
        testStateDistributor.distributeState();
        verify(playerManager).removePlayer(any());
        verify(gameMap).despawnActor(any(Player.class));
    }

    @Test
    public void testStateDistributionOK() throws IOException {
        testStateDistributor.distributeState();
        byte[] playerSegmentBytes = serializePlayerSegment();

        ArgumentCaptor<ByteBuffer> argumentCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verify(playerChannel).write(argumentCaptor.capture());

        ByteBuffer sentBuffer = argumentCaptor.getValue();
        byte[] sentBufferArray = Arrays.copyOfRange(sentBuffer.array(), 0, playerSegmentBytes.length);
        assertArrayEquals(playerSegmentBytes, sentBufferArray);
    }


    private byte[] serializePlayerSegment() throws IOException {
        var byteArrayOutputStream = new PerformantByteArrayOutputStream();
        var dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        playerSegment.serialize(dataOutputStream);
        dataOutputStream.flush();
        dataOutputStream.close();
        return byteArrayOutputStream.getBuf();
    }
}
