package bg.sofia.uni.fmi.mjt.dungeons.network;

import bg.sofia.uni.fmi.mjt.dungeons.game.action.PlayerActionHandler;
import bg.sofia.uni.fmi.mjt.dungeons.game.action.PlayerConnect;
import bg.sofia.uni.fmi.mjt.dungeons.game.action.PlayerDisconnect;
import bg.sofia.uni.fmi.mjt.dungeons.game.action.PlayerMovement;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class GameServer {

    private static final String HOST = "localhost";
    private static final int PORT = 10_000;

    private SmartBuffer buffer;
    private PlayerActionHandler actionHandler;

    private Selector selector;

    public GameServer(PlayerActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        this.buffer = new SmartBuffer();
    }

    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // TODO maybe close sometime

            serverSocketChannel.bind(new InetSocketAddress(HOST, PORT));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot start the server", e);
        }
    }

    public void fetchPlayerActions() {
        try {
            int readyChannels = selector.selectNow();
            if (readyChannels == 0) {
                return;
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    int r = buffer.readFromChannel(sc);
                    if (r <= 0) {
                        System.out.println("A player disconnected manually");
                        actionHandler.publish(new PlayerDisconnect(sc));
                        break;
                    }
                    String receivedData = buffer.read();
                    actionHandler.publish(new PlayerMovement(receivedData, sc));
                    System.out.printf("Received message: %s\n", receivedData);
                } else if (key.isAcceptable()) {
                    ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                    SocketChannel playerChannel = sockChannel.accept();
                    playerChannel.configureBlocking(false);
                    actionHandler.publish(new PlayerConnect(playerChannel));
                    playerChannel.register(selector, SelectionKey.OP_READ);
                }

                keyIterator.remove();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot fetch player actions", e);
        }
    }

}
