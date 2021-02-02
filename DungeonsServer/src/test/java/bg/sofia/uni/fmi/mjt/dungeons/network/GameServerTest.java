package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.action.PlayerActionHandler;
import bg.sofia.uni.fmi.mjt.dungeons.action.PlayerConnect;
import bg.sofia.uni.fmi.mjt.dungeons.action.PlayerDisconnect;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GameServerTest {
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(10_000);

    private static PlayerActionHandler playerActionHandlerMock = Mockito.mock(PlayerActionHandler.class);

    private static GameServer testGameServer;

    @BeforeClass
    public static void init() {
        testGameServer = new GameServer(playerActionHandlerMock);
        testGameServer.start();
    }

    @Test
    public void testPlayerConnection() throws IOException {
        SocketChannel.open(SERVER_ADDRESS);
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(PlayerConnect.class));
    }

    @Test
    public void testPlayerDisconnection() throws IOException, InterruptedException {
        SocketChannel playerChannel = SocketChannel.open(SERVER_ADDRESS);
        playerChannel.finishConnect();
        playerChannel.close();

        Thread.sleep(10000);
        testGameServer.fetchPlayerActions();
        verify(playerActionHandlerMock).publish(any(PlayerConnect.class));
        verify(playerActionHandlerMock).publish(any(PlayerDisconnect.class));
    }

}
